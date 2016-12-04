package js.services;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;


public class EavesdropService {

	static EavesdropService singleton;
	
	private EavesdropService(){}
	
	//singleton
	public static EavesdropService getEavesdrop(){
		if(singleton == null)
			singleton = new EavesdropService();
		return singleton;
	}
	public Document getDoc(String URL) throws IOException {
		Document doc = Jsoup.connect(URL).get();
		return doc;
	}
	
	public String getHello() {
		return "Hello from Eavesdrop service.";
	}
	public String getXML(Elements elems) {
		String XML = "";
		
		String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"><tests><test><id>xxx</id><status>xxx</status></test><test><id>xxx</id><status>xxx</status></test></tests></xml>";
		Document doc = Jsoup.parse(html, "", Parser.xmlParser());
		for (Element e : doc.select("test")) {
		    XML += e;
		}
		return XML;
	}
	public Elements getElements(Document doc) {
		Elements elems = new Elements();
		//href
		Elements titles = doc.getElementsByTag("a");
		//system.out.println("-- EavesdropService getElements() titles:\n " + titles.text());
		for (Element elm : titles){
			String title = elm.html();
			
			if (title.equals("Name") || title.equals("Last modified") || title.equals("Size") || title.equals("Description") || title.equals("Parent Directory"))
				continue;
			
			Element elem = new Element(Tag.valueOf("Project"), "");
			elem.html(elm.html().replace("/",""));
			elems.add(elem);
		}
		return elems;
	}
	
	//implement
	public Map<String, Integer> getAllMeetingCounts() {
		
		//this list is what we turn, its a list of meetings		
		Map<String, Integer> newmap = new TreeMap<String, Integer>();
		
		return newmap;
	}
}
