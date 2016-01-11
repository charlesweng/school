import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class MainMenu implements ActionListener {
	
	private JFrame frame;
	private JPanel login,startmenu,patient,admin,guardian;
	private JButton  loginButton, loadButton, startButton, backButton, exitButton;
	
	JTextField loadingdb, loadingtbl, targetdb, ufield, pfield;
	
	Hospital myHospital;
	public MainMenu() {
		frame = new JFrame("Hospital Program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		/* login logic - getting the db connection */
		login = new JPanel();
		loadingdb = new JTextField(20);
		loadingtbl = new JTextField(20);
		targetdb = new JTextField(20);
		ufield = new JTextField(20);
		pfield = new JPasswordField(20);
		login.add(new JLabel("Loading Database: "));
		login.add(loadingdb);
		login.add(new JLabel("Loading Table: "));
		login.add(loadingtbl);
		login.add(new JLabel("Target Database: "));
		login.add(targetdb);
		login.add(new JLabel("Username: "));
		login.add(ufield);
		login.add(new JLabel("Password: "));
		login.add(pfield);
		login.setLayout(new GridLayout(11,0));
		loginButton = new JButton("LOGIN");
		loginButton.addActionListener(this);
		login.add(loginButton);
		login.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		/* eo login logic */
		
		/* start menu */
		startmenu = new JPanel();
		loadButton = new JButton("LOAD DB");
		loadButton.addActionListener(this);
		startmenu.add(loadButton);
		startButton = new JButton("START");
		startButton.addActionListener(this);
		startmenu.add(startButton);
		exitButton = new JButton("EXIT");
		exitButton.addActionListener(this);
		startmenu.add(exitButton);
		/* eo start menu */
		
		
		
		patient = new JPanel();
		admin = new JPanel();
		guardian = new JPanel();
		
		
		
		frame.setSize(600, 400);
		resetFrame();
		frame.setContentPane(login);
		frame.setVisible(true);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton) e.getSource();
		if (button == loginButton)
		{
			frame.remove(login);
			frame.setContentPane(startmenu);
			frame.setSize(300, 75);
			resetFrame();
		}
		else if (button == loadButton)
		{
			myHospital = new Hospital(loadingdb.getText(), loadingtbl.getText(), targetdb.getText(),ufield.getText(),pfield.getText());
			System.out.println("Finished loading hospital database");
		}
		else if (button == startButton)
		{
			frame.remove(startmenu);
			doInterface(loadingdb.getText(),targetdb.getText(),ufield.getText(),pfield.getText());
			resetFrame();
		}
		else if (button == exitButton)
		{
			frame.dispose();
			System.exit(0);
		}
		
		frame.validate();
		frame.repaint();
	}
	
	public void resetFrame() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	}
	
	public JPanel doInterface(String messagedb, String hospitaldb, String uname, String pw)
	{
		JPanel p = new JPanel();
		JTextField option = new JTextField(20);
		p.add(new JLabel("Enter 'patient', 'doctor', or 'administrator'"));
		p.add(option);
		JOptionPane.showConfirmDialog(null,p,"Choose your interface: ", JOptionPane.OK_CANCEL_OPTION);

			String chosen = option.getText().toLowerCase();
			if(chosen.equals("patient"))
			{
				PatientInterface pi = new PatientInterface(messagedb, hospitaldb, uname, pw);

			}
			else if(chosen.equals("doctor"))
			{
				DoctorInterface di = new DoctorInterface(messagedb, hospitaldb, uname, pw);
			}
			else if(chosen.equals("administrator"))
			{
				AdminInterface ai = new AdminInterface(messagedb, hospitaldb, uname, pw);
			}
			else
			{
				
			}
		return p;
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
		@Override
		public void run() {
			new MainMenu();
		}//run
		}//runnable
		);//swing utilities
	}
}
