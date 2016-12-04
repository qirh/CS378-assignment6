package js.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "meeting_count")
@XmlAccessorType(XmlAccessType.FIELD)
public class MeetingFile {
	
	@XmlElement
	private String name;
	
	
	public MeetingFile() {
		name = new String();
	}
	public MeetingFile(String name) {
		this.name = new String(name);
	}
	public void setName(String name) {
		this.name = new String(name);
	}
	public String getName() {
		return this.name;
	}
}