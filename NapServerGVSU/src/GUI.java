import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI {

	private JFrame frame;
	private JTextField textFieldServerHostname;
	private JTextField textFieldUsername;
	private JTextField textFieldHostname;
	private JTextField textFieldPort;
	private JTextField textField_4;
	private JTextField textField_5;
	private JComboBox comboBoxSpeed;
	
	/* CHANGED */
	private Host host = new Host();
	private HostServer hostServer;
	/* Holds the condition of whether Host-as-Server is setup */
	private boolean alreadySetupFTPServer = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 451, 590);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panelConnection = new JPanel();
		panelConnection.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panelConnection.setBounds(12, 35, 415, 107);
		frame.getContentPane().add(panelConnection);
		panelConnection.setLayout(null);
		
		JLabel lblServerHostname = new JLabel("Server Hostname:");
		lblServerHostname.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblServerHostname.setBounds(12, 12, 133, 15);
		panelConnection.add(lblServerHostname);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblUsername.setBounds(12, 39, 119, 15);
		panelConnection.add(lblUsername);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPort.setBounds(268, 12, 39, 15);
		panelConnection.add(lblPort);
		
		JLabel lblNewLabel = new JLabel("Hostname:");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel.setBounds(12, 66, 68, 15);
		panelConnection.add(lblNewLabel);
		
		JLabel lblSpeed = new JLabel("Speed:");
		lblSpeed.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSpeed.setBounds(254, 39, 49, 15);
		panelConnection.add(lblSpeed);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				String serverHostname = textFieldServerHostname.getText();
				
				String username = textFieldUsername.getText(); 
				username = username.replaceAll(" ", "@@");
				
				String port = textFieldPort.getText();
				
				String hostname = textFieldHostname.getText();
				hostname = hostname.replaceAll(" ", "@@");
				
				String speed = comboBoxSpeed.getSelectedItem().toString();
				
				/* Make sure the user has typed in a username */
				if (!username.equals("  ")) {
				    /* 
				     This internally handles the user clicking "connect"
				     multiple times.
				    */
				    host.connectToServer(serverHostname, port, username, hostname, speed);
				}
				
				/* Makes the host a FTP server as well. */
				if (alreadySetupFTPServer == false) {
		            try {
		                /* 
		                 This starts a new thread for FTP-as-Server-Handling 
			             This allows for the GUI to act separately from the
			             actions of the FTP part of the client 
			            */
			            hostServer = new HostServer();
                        hostServer.start();
                        
                        /* 
                         Next "Connect" click will now NOT try this
                         again. This important so that a multitude of 
                         actions aren't being opened on the same port.
                         */
                        alreadySetupFTPServer = true;
                        
                        /* For debugging */
		                // System.out.println("DEBUG-01: Initializing Host as FTP Server!");
		                
		            } catch(Exception f) {
			            System.out.println("  ERROR-03: Failure to setup" +
			             " client as a FTP-Server.");
		            }
		        }
		        else {
		            System.out.println("  DEBUG-03: Already setup as a FTP-Server!");
		        }
		        
			}
		});
		btnConnect.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		btnConnect.setBounds(254, 66, 149, 30);
		panelConnection.add(btnConnect);
		
		textFieldServerHostname = new JTextField();
		textFieldServerHostname.setText("127.0.0.1");
		textFieldServerHostname.setBounds(130, 10, 114, 19);
		panelConnection.add(textFieldServerHostname);
		textFieldServerHostname.setColumns(10);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setText("  ");
		textFieldUsername.setBounds(84, 39, 152, 19);
		panelConnection.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		textFieldHostname = new JTextField();
		textFieldHostname.setText("DCCLIENT");
		textFieldHostname.setBounds(84, 66, 158, 19);
		panelConnection.add(textFieldHostname);
		textFieldHostname.setColumns(10);
		
		textFieldPort = new JTextField();
		textFieldPort.setText("1234");
		textFieldPort.setBounds(301, 10, 102, 19);
		panelConnection.add(textFieldPort);
		textFieldPort.setColumns(10);
		
		comboBoxSpeed = new JComboBox();
		comboBoxSpeed.setModel(new DefaultComboBoxModel(new String[] {"Ethernet", "T1"}));
		comboBoxSpeed.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBoxSpeed.setBounds(301, 37, 99, 19);
		panelConnection.add(comboBoxSpeed);
		
		JPanel panelSearch = new JPanel();
		panelSearch.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panelSearch.setBounds(12, 175, 415, 162);
		frame.getContentPane().add(panelSearch);
		panelSearch.setLayout(null);
		
		TextArea textArea = new TextArea();
		textArea.setBounds(10, 31, 395, 121);
		panelSearch.add(textArea);
		
		JLabel lblKeyword = new JLabel("Keyword:");
		lblKeyword.setBounds(12, 10, 63, 15);
		lblKeyword.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelSearch.add(lblKeyword);
		
		textField_4 = new JTextField();
		textField_4.setBounds(68, 6, 233, 19);
		panelSearch.add(textField_4);
		textField_4.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(313, 6, 90, 19);
		btnSearch.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		panelSearch.add(btnSearch);
		
		JPanel panelFTP = new JPanel();
		panelFTP.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panelFTP.setBounds(12, 364, 415, 181);
		frame.getContentPane().add(panelFTP);
		panelFTP.setLayout(null);
		
		TextArea textArea_1 = new TextArea();
		textArea_1.setBounds(10, 33, 395, 137);
		panelFTP.add(textArea_1);
		
		JLabel lblEnterCommand = new JLabel("Enter Command:");
		lblEnterCommand.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEnterCommand.setBounds(12, 12, 125, 15);
		panelFTP.add(lblEnterCommand);
		
		textField_5 = new JTextField();
		textField_5.setBounds(119, 10, 225, 19);
		panelFTP.add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnGo = new JButton("Go");
		btnGo.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		btnGo.setBounds(351, 10, 54, 17);
		panelFTP.add(btnGo);
		
		JLabel lblConnection = new JLabel("Connection");
		lblConnection.setBounds(12, 22, 80, 15);
		frame.getContentPane().add(lblConnection);
		
		JLabel lblSearch = new JLabel("Search");
		lblSearch.setBounds(12, 161, 70, 15);
		frame.getContentPane().add(lblSearch);
		
		JLabel lblFtp = new JLabel("FTP");
		lblFtp.setBounds(12, 349, 70, 15);
		frame.getContentPane().add(lblFtp);
	}
}
