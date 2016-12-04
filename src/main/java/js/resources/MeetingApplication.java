package js.resources;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/meetings")
public class MeetingApplication extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();
	
	public MeetingApplication() {		
		System.out.println("-- MeetingApplication constructor");
	}
	@Override
	public Set<Class<?>> getClasses() {
		classes.add(MeetingResource.class);
		return classes;
	}
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}