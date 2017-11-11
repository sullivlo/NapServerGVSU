import java.util.ArrayList;

public class HostedDescriptions {
	
	private ArrayList<String> speeds;
	private ArrayList<String> hostnames;
	private ArrayList<String> filenames;

	public HostedDescriptions() {
			
		speeds = new ArrayList<String>();
		hostnames = new ArrayList<String>();
		filenames = new ArrayList<String>();
	}
	
	public void addValues (String addThisToSpeeds, String addThisToHostname, String addThisToFilename) {
		this.speeds.add(addThisToSpeeds);
		this.hostnames.add(addThisToHostname);
		this.filenames.add(addThisToFilename);
	}
		
	public void remove (String hostname) {
		for(int i = 0; i < hostnames.size(); i++) {
			if(hostnames.get(i).equals(hostname)) {
				hostnames.remove(i);
				speeds.remove(i);
				filenames.remove(i);
				i--;
			}
		}
	}
}
