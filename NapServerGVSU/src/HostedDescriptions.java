import java.util.ArrayList;

public class HostedDescriptions {
	
	private ArrayList<String> speeds;
	private ArrayList<String> hostname;
	private ArrayList<String> filename;

	public HostedDescriptions() {
			
		ArrayList<String> speeds = new ArrayList<String>();
		ArrayList<String> hostname = new ArrayList<String>();
		ArrayList<String> filename = new ArrayList<String>();
			
		String TEST = new String();
	}
	
	public void addValue (String addThis) {
		speeds.add (addThis);
	}
		
	public void remove (String IP) {
		
	}

	public String testMethod() {
		// TODO Auto-generated method stub
		return "Great, it works!";
		// return null;
	}
}
