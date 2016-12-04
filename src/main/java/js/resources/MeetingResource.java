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
			//system.out.println("1");
			for(String x : years){
				//system.out.println("2 == " + x);
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
				 //system.out.println("add meeting == " + meeting.getYear() + " with int == " + meeting.getInteger());
				 meetings.addMeeting(meeting);
			}
				return new StreamingOutput() {
			         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
			        	
			        	 if(meetings.isEmpty())
			        		 outputError(outputStream, new Error(meetings.toString()));
			        	 else
			        		 outputMeetings(outputStream, meetings);
			        	 	 //outputMeetingNumber(outputStream, new MeetingNumber(meetings.size()/4));
			        	 
			        	 //system.out.println("MEETINGS = " + meetings.size()/4);
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
}