import java.util.ArrayList;

public class HostedDescriptions {
	
	private ArrayList<String> speeds;
	private ArrayList<String> hostnames;
	private ArrayList<String> filenames;
	private ArrayList<String> keywords;
    private ArrayList<String> usernames;
    private ArrayList<String> userIPs;
    private ArrayList<String> userPorts;    

	public HostedDescriptions() {
	    userIPs = new ArrayList<String>();		
	    userPorts = new ArrayList<String>();		
	    usernames = new ArrayList<String>();		
		speeds = new ArrayList<String>();
		hostnames = new ArrayList<String>();
		filenames = new ArrayList<String>();
		keywords = new ArrayList<String>();
	}
	
	public void addValues (String addThisToSpeeds, String addThisToHostname, String addThisToFilename, String addThisToKeywords, String addThisToUsernames, String addThisToUserIPs, String addThisToUserPorts) {
		this.speeds.add(addThisToSpeeds);
		this.hostnames.add(addThisToHostname);
		this.filenames.add(addThisToFilename);
		this.keywords.add(addThisToKeywords);
		this.usernames.add(addThisToUsernames);
		this.userIPs.add(addThisToUserIPs);
		this.userPorts.add(addThisToUserPorts);
	}
	
	/* Removes all elements that belong to the client with a specific username */
	public void remove (String username) {
		for(int i = 0; i < usernames.size(); i++) {
			if(usernames.get(i).equals(username)) {
			    usernames.remove(i);
				hostnames.remove(i);
				speeds.remove(i);
				filenames.remove(i);
				userIPs.remove(i);
				keywords.remove(i);
				userPorts.remove(i);
				i--;
			}
		}
	}
	
	/* Returns true if the username is already taken */
	public boolean isUsernameTaken(String username) {
	    if (usernames.contains(username)) {
	        return true;
	    } 
	    else {
	        return false;
	    }	    
	}
	
	/* Displays some information about the current state of the main tables */
	public void showData() {
	    ArrayList<String> variousUsernames = new ArrayList<String>();
	    int numberOfUsers;
	    try {
            variousUsernames.add( usernames.get(0) );l
            for(int i = 0; i < usernames.size(); i++) {
                if (!variousUsernames.contains( usernames.get(i) )) {
                    variousUsernames.add( usernames.get(i) );
                }
	        }
	        numberOfUsers = variousUsernames.size();
	    }
	    catch (Exception e) {
	        numberOfUsers = 0;
	    }
	    
	    System.out.println("Showing Current Server Data...");
	    System.out.print(" Total Current Users: " + numberOfUsers);
	    if (numberOfUsers > 1) {
	        System.out.print(" [");
	        for(int j = 0; j < variousUsernames.size() - 1; j++) {
	            System.out.print( variousUsernames.get(j) + ", ");
	        }
	        System.out.print( variousUsernames.get(numberOfUsers - 1) + "]");
	    }
	    System.out.print("\n");
	    
	    if (numberOfUsers != 0) {
	        System.out.println(" Total Files Represented: " + usernames.size() );	    
            System.out.print(" List of Files: \n");
            System.out.println(" [Username, IP-Address, FTP-Port-Num, Speed, Image]");
            for(int k = 0; k < filenames.size(); k++) {
                System.out.print(" [" + usernames.get(k) + ", " + userIPs.get(k) + ", " + userPorts.get(k) + ", " + speeds.get(k) + ", " + filenames.get(k) + "] \n");
            }
        }
        
    /* End of showData() */
	}
	
/* End of class HostedDescriptions */
}
