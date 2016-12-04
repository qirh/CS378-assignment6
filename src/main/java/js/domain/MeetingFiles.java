package js.domain;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "meetings")
@XmlAccessorType(XmlAccessType.FIELD)
public class MeetingFiles {
	
	@XmlElement(name= "meeting")
    private List<MeetingFile> meetings;
    
    public MeetingFiles(){
    	meetings = new ArrayList<MeetingFile>();
    }
    public List<MeetingFile> getMeetings() {
    	return meetings;
    }
    public int size() {
        return meetings.size();
    }
    public int size4() {
        return meetings.size()/4;
    }
    public void addMeeting(MeetingFile meeting) {
    	if( ! meetings.contains(meeting) )
    		meetings.add(meeting);
    }
    public boolean isEmpty() {
    	return meetings.isEmpty();
    }
    public void clearMeetings() {
    	meetings.clear();
    }
}