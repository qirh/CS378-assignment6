package js.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import js.domain.Error;
import js.domain.Meeting;
import js.domain.MeetingFile;
import js.domain.MeetingFiles;
import js.domain.Meetings;
import js.services.EavesdropService;

@Path("/myeavesdrop")
public class MeetingResource {
	
	//	http://localhost:8080/assignment3/myeavesdrop/projects/
	//URL's don't terminate with a '/'
	private final String URL = "http://eavesdrop.openstack.org/meetings/solum_team_meeting/";
	Meetings meetings = new Meetings();
	MeetingFiles meetingfiles = new MeetingFiles();
	final private String[] years = {"2013", "2014", "2015", "2016"};
	private EavesdropService ser = null;

	public MeetingResource() {
		startService();
	}
	
	/* EAVESDROP	*/
	void setEavesdropService(EavesdropService ser){
		this.ser = ser;
	}
	String getURL(){
		return URL;
	}
	EavesdropService startService(){
		ser =  EavesdropService.getEavesdrop();
		return ser;
	}
	@GET
	@Path("/eavesdrophelloworld")
	@Produces("text/html")
	public String helloEaves() {
		return ser.getHello();		
	}
	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		return "Hello world";		
	}	
	@GET
	@Path("/meetings")
	@Produces("application/xml")
	public StreamingOutput getMeetings()  {
		try{
			System.out.println("1");
			for(String x : years){
				System.out.println("2 == " + x);
				Document doc = ser.getDoc(URL+"/"+x);
				final Elements elems = ser.getElements(doc);
				final Meeting meeting = new Meeting();
				meeting.setYear(x);
				for(Element e : elems){
	        		MeetingFile m = new MeetingFile();
	        		m.setName(e.text());
	        		meetingfiles.addMeeting(m);
	        	 } 
				 meeting.setInteger(meetingfiles.size4());
				 meetingfiles.clearMeetings();
				 System.out.println("add meeting == " + meeting.getYear() + " with int == " + meeting.getInteger());
				 meetings.addMeeting(meeting);
			}
				return new StreamingOutput() {
			         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
			        	
			        	 if(meetings.isEmpty())
			        		 outputError(outputStream, new Error(meetings.toString()));
			        	 else
			        		 outputMeetings(outputStream, meetings);
			        	 	 //outputMeetingNumber(outputStream, new MeetingNumber(meetings.size()/4));
			        	 
			        	 System.out.println("MEETINGS = " + meetings.size()/4);
			        	 meetings.clearMeetings();
			         }
			     };
		}
		catch (IOException i){
			return new StreamingOutput() {
		         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
		        	outputError(outputStream, new Error(meetings.toString()));
		         }
		     };
		}
		catch (WebApplicationException i){
			return new StreamingOutput() {
		         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
		        	outputError(outputStream, new Error(meetings.toString()));
		         }
		     };
		}
	}
	protected void outputError(OutputStream os, Error error) throws IOException {
		
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Error.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(error, os);
		} 
		catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	protected void outputMeetings(OutputStream os, Meetings meetings) throws IOException {
		
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Meetings.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(meetings, os);
		} 
		catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	protected void outputMeeting(OutputStream os, Meetings meeting) throws IOException {
		
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Meeting.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(meetings, os);
		} 
		catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	protected void outputMeetingNumber(OutputStream os, MeetingFile i) throws IOException {
		
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(MeetingFile.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(i, os);
		} 
		catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
	@GET
	@Path("/counts")
	@Produces("application/xml")
	public StreamingOutput outputCount() throws JAXBException{
		/** ERROR CHECKING **/
		final Map<String, Integer> tmp = new TreeMap<String, Integer>(ser.getAllMeetingCounts());
		
		return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {  
	        		 outputCounts(outputStream, tmp);	         }
	      };
	}

	/** OUTPUT COUNTS **/
	protected void outputCounts(OutputStream os, Map<String, Integer> tmp) {
	      PrintStream writer = new PrintStream(os);   
	      
	      writer.println("<meetings>");
	      for(Entry<String, Integer> entry: tmp.entrySet()){
	    	  String name = entry.getKey();
	    	  int value = entry.getValue();
		      writer.println("<meeting>");
	    	  writer.println("<name>" + name + "</name>");
	    	  writer.println("     <count>" + value + "</count>");
		      writer.println("</meeting>");
	      }
	      writer.println("</meetings>");
	}
}