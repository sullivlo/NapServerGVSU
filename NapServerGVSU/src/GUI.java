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

public class GUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;

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
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel.setBounds(12, 35, 415, 107);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblServerHostname = new JLabel("Server Hostname:");
		lblServerHostname.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblServerHostname.setBounds(12, 12, 133, 15);
		panel.add(lblServerHostname);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblUsername.setBounds(12, 39, 119, 15);
		panel.add(lblUsername);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPort.setBounds(268, 12, 39, 15);
		panel.add(lblPort);
		
		JLabel lblNewLabel = new JLabel("Hostname:");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel.setBounds(12, 66, 68, 15);
		panel.add(lblNewLabel);
		
		JLabel lblSpeed = new JLabel("Speed:");
		lblSpeed.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSpeed.setBounds(254, 39, 49, 15);
		panel.add(lblSpeed);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		btnConnect.setBounds(254, 66, 149, 30);
		panel.add(btnConnect);
		
		textField = new JTextField();
		textField.setBounds(130, 10, 114, 19);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(84, 39, 125, 19);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(84, 66, 125, 19);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(301, 10, 102, 19);
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Ethernet", "T1"}));
		comboBox.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox.setBounds(301, 37, 99, 19);
		panel.add(comboBox);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_1.setBounds(12, 175, 415, 162);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		TextArea textArea = new TextArea();
		textArea.setBounds(10, 31, 395, 121);
		panel_1.add(textArea);
		
		JLabel lblKeyword = new JLabel("Keyword:");
		lblKeyword.setBounds(12, 10, 63, 15);
		lblKeyword.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_1.add(lblKeyword);
		
		textField_4 = new JTextField();
		textField_4.setBounds(68, 6, 233, 19);
		panel_1.add(textField_4);
		textField_4.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(313, 6, 90, 19);
		btnSearch.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		panel_1.add(btnSearch);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_2.setBounds(12, 364, 415, 181);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		TextArea textArea_1 = new TextArea();
		textArea_1.setBounds(10, 33, 395, 137);
		panel_2.add(textArea_1);
		
		JLabel lblEnterCommand = new JLabel("Enter Command:");
		lblEnterCommand.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEnterCommand.setBounds(12, 12, 125, 15);
		panel_2.add(lblEnterCommand);
		
		textField_5 = new JTextField();
		textField_5.setBounds(119, 10, 225, 19);
		panel_2.add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnGo = new JButton("Go");
		btnGo.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		btnGo.setBounds(351, 10, 54, 17);
		panel_2.add(btnGo);
		
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

