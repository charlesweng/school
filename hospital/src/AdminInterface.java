import java.awt.Font;
import java.awt.GridLayout;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;

public class AdminInterface {
	JPanel p;
	public JPanel getPanel() {
		return p;
	}
	
	public AdminInterface(String messagedb, String hospitaldb, String uname, String pw)
	{
		while(true)
		{
			p = new JPanel();
			JTextField option = new JTextField(20);
			p.setLayout(new GridLayout(3, 0));
			p.add(new JLabel("Enter 'allergyCounts', 'patientAllergies', 'plans', or 'authors': "));
			p.add(option);
			int result = JOptionPane.showConfirmDialog(null,p,"Choose your action", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_CANCEL_OPTION)
			{
				break;
			}
			int errcount = 0;
			String answer = option.getText().toLowerCase();
			if(answer.equals("allergycounts"))
			{
				try {
					Connection hospitalconnection;
					hospitalconnection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+hospitaldb,uname,pw);
					PreparedStatement ps = hospitalconnection.prepareStatement("select count(*) from (SELECT distinct(substance) from allergies where substance is not null group by substance) a");
					ResultSet rs = ps.executeQuery();
					if(rs.next())
					{
						int count = rs.getInt("count(*)");
						String[] allergyLabels = new String[count];
						String[] allergyCounts = new String[count];
						PreparedStatement ps2 = hospitalconnection.prepareStatement("select distinct(substance), count(*) from allergies where substance is not null group by substance");
						ResultSet rs2 = ps2.executeQuery();
						int index = 0;
						while(rs2.next())
						{
							String substance = rs2.getString("substance");
							int count2 = rs2.getInt("count(*)");
							allergyLabels[index] = substance;
							allergyCounts[index] = Integer.toString(count2);
							index++;
						}
						
						JPanel p2 = new JPanel();
						p2.setLayout(new GridLayout(allergyLabels.length, 1));
						for(int i = 0; i < allergyLabels.length; i++)
						{
							p2.add(new JLabel(allergyLabels[i]));
							p2.add(new JLabel(allergyCounts[i]));
						}
						JOptionPane.showMessageDialog(null, p2, "Allergy Count Information", JOptionPane.OK_CANCEL_OPTION);				
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(answer.equals("patientallergies"))
			{
				try {
					Connection hospitalconnection;
					hospitalconnection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+hospitaldb,uname,pw);
					PreparedStatement ps = hospitalconnection.prepareStatement("select PatientID, count(*) from allergies group by PatientID having count(*) > 1");
					ResultSet rs = ps.executeQuery();
					PreparedStatement psc = hospitalconnection.prepareStatement("select count(*) from (select a.PatientID, count(*) from allergies a group by a.PatientID having count(*) > 1) AS MultiAllergy");
					ResultSet rsc = psc.executeQuery();
					int count = 0;
					if (rsc.next())
					{
						count = rsc.getInt("count(*)");
					}
					if(rs.next() && count != 0)
					{
						String[] names = new String[count];
						String[] ids = new String[count];
						PreparedStatement ps2 = hospitalconnection.prepareStatement("select p.PatientID, p.GivenName, p.FamilyName from Patient p where PatientID in (select PatientID from allergies group by PatientID having count(*) > 1)");
						ResultSet rs2 = ps2.executeQuery();
						int index = 0;
						while(rs2.next())
						{
							String pid = rs2.getString("PatientID");
							String fname = rs2.getString("GivenName");
							String lname = rs2.getString("FamilyName");
							names[index] = fname + " " + lname;
							ids[index] = pid;
							index++;
						}
						JPanel p2 = new JPanel();
						p2.setLayout(new GridLayout(names.length + 2, 1));
						p2.add(new JLabel("Patient ID"));
						p2.add(new JLabel("Patient Name"));
						p2.add(new JLabel(""));
						p2.add(new JLabel(""));
						for(int i = 0; i < names.length; i++)
						{
							p2.add(new JLabel(ids[i]));
							p2.add(new JLabel(names[i]));
						}
						JOptionPane.showMessageDialog(null, p2, "Patients with multiple allergies", JOptionPane.OK_CANCEL_OPTION);
					}
					else
					{
						JPanel p2 = new JPanel();
						p2.add(new JLabel("No patients with multiple allergies"));
						JOptionPane.showMessageDialog(null, p2, "Patients with multiple allergies", JOptionPane.OK_CANCEL_OPTION);				
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(answer.equals("plans"))
			{
				try {
					Connection hospitalconnection;
					hospitalconnection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+hospitaldb,uname,pw);
					PreparedStatement ps = hospitalconnection.prepareStatement("select * from Plan pl, Patient pt where pl.PatientID = pt.PatientID and DATE(pl.ScheduleDate) = DATE(CURDATE())");
					ResultSet rs = ps.executeQuery();
					ArrayList<ArrayList<String>> listOfPtElements = new ArrayList<ArrayList<String>>();
					while (rs.next())
					{
						ArrayList<String> ptElements = new ArrayList<String>();
						ptElements.add(rs.getString("PatientID"));
						ptElements.add(rs.getString("PlanID"));
						ptElements.add(rs.getString("GivenName"));
						ptElements.add(rs.getString("FamilyName"));
						ptElements.add(rs.getString("Activity"));
						ptElements.add(rs.getString("ScheduleDate"));
						listOfPtElements.add(ptElements);
					}
					if (listOfPtElements.isEmpty())
					{
						JPanel p2 = new JPanel();
						p2.add(new JLabel("No patients scheduled for today!"));
						JOptionPane.showMessageDialog(null, p2, "Patients scheduled for today", JOptionPane.OK_CANCEL_OPTION);	
					}
					else
					{
						int rows = listOfPtElements.size();
						int cols = listOfPtElements.get(0).size();
						JPanel p1 = new JPanel();
						p1.setLayout(new GridLayout(rows+1,cols));
						JLabel pilbl = new JLabel("<html><u>PatientID</u></html>");
						JLabel pllbl = new JLabel("<html><u>PlanID</u></html>");
						JLabel gnlbl = new JLabel("<html><u>Name</u></html>");
						JLabel lnlbl = new JLabel("<html><u>LastName</u></html>");
						JLabel aclbl = new JLabel("<html><u>Activity</u></html>");
						JLabel sclbl = new JLabel("<html><u>ScheduleDate</u></html>");
						p1.add(pilbl);
						p1.add(pllbl);
						p1.add(gnlbl);
						p1.add(lnlbl);
						p1.add(aclbl);
						p1.add(sclbl);
						for (int i = 0; i < rows; i++)
						{
							for (int j = 0; j < cols; j++)
							{
								p1.add(new JLabel(listOfPtElements.get(i).get(j)));
							}
						}
						JOptionPane.showMessageDialog(null, p1, "Patients scheduled for today", JOptionPane.OK_CANCEL_OPTION);	
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(answer.equals("authors"))
			{
				try {
					Connection hospitalconnection;
					hospitalconnection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+hospitaldb,uname,pw);
					PreparedStatement ps = hospitalconnection.prepareStatement("select a.AuthorID, a.AuthorTitle, a.AuthorFirstName, a.AuthorLastName, a.ParticipatingRole from Author a where a.AuthorID in (select AuthorID from Assigned group by AuthorID having count(*) > 1)");
					ResultSet rs = ps.executeQuery();
					ArrayList<ArrayList<String>> listOfAuthElements = new ArrayList<ArrayList<String>>();
					while (rs.next())
					{
						ArrayList<String> authElements = new ArrayList<String>();
						authElements.add((rs.getString("AuthorID")).toString());
//						authElements.add(rs.getString("AuthorTitle"));
						authElements.add(rs.getString("AuthorFirstName"));
						authElements.add(rs.getString("AuthorLastName"));
						authElements.add(rs.getString("ParticipatingRole"));
						listOfAuthElements.add(authElements);
					}
					if (listOfAuthElements.size() == 0)
					{
						JPanel p2 = new JPanel();
						p2.add(new JLabel("No authors with multiple patients"));
						JOptionPane.showMessageDialog(null, p2, "Authors with multiple patients", JOptionPane.OK_CANCEL_OPTION);	
					}
					else
					{
						int rows = listOfAuthElements.size();
						int cols = listOfAuthElements.get(0).size();
						JPanel p1 = new JPanel();
						p1.setLayout(new GridLayout(rows+1,cols));
						JLabel idlbl = new JLabel("<html><u>AuthorID</u></html>");
//						JLabel ttlbl = new JLabel("<html><u>AuthorTitle</u></html>");
						JLabel fnlbl = new JLabel("<html><u>Name</u></html>");
						JLabel lnlbl = new JLabel("<html><u>LastName</u></html>");
						JLabel prlbl = new JLabel("<html><u>Role</u></html>");
						p1.add(idlbl);
//						p1.add(ttlbl);
						p1.add(fnlbl);
						p1.add(lnlbl);
						p1.add(prlbl);
						for (int i = 0; i < rows; i++)
						{
							for (int j = 0; j < cols; j++)
							{
								p1.add(new JLabel(listOfAuthElements.get(i).get(j)));
							}
						}
						JOptionPane.showMessageDialog(null, p1, "Authors with multiple patients", JOptionPane.OK_OPTION);
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, p, "Please enter 'allergyCounts', 'patientAllergies', 'plans', or 'authors' ", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
}
