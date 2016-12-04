package js.domain;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "meeting")
@XmlAccessorType(XmlAccessType.FIELD)
public class Meeting {
	
	@XmlElement
	private String year;
	
	@XmlElement(name = "i")
	private Integer i;
	
	public Meeting() {
		year = "";
		i = new Integer(0);
	}
	public void setYear(String year) {
		if( year.matches("^\\d+$") )
			this.year = year;
		else
			this.year = "-1";
	}
	public String getYear() {
		return year;
	}
	public void setInteger(int i) {
        this.i = i;
    }
	public Integer getInteger() {
        return i;
    }
    public String toString(){
    	return year;
    }
}