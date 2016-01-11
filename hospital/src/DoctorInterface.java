import java.awt.Color;
import java.awt.GridLayout;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DoctorInterface {
	JPanel p;
	public JPanel getPanel() {
		return p;
	}
	
	public DoctorInterface(String messagedb, String hospitaldb, String uname, String pw)
	{
		while(true)
		{
			p = new JPanel();
			JTextField option = new JTextField(20);
			p.setLayout(new GridLayout(3, 0));
			p.add(new JLabel("Enter your patient's id: "));
			p.add(option);
			int result = JOptionPane.showConfirmDialog(null,p,"Patient id", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_CANCEL_OPTION)
			{
				break;
			}
			int errcount = 0;
			while(true)
			{
				if (option.getText().equals("") || option.getText() == null)
				{
					if (errcount != 1) {
					JLabel error = new JLabel("You may have entered an invalid ID");
					error.setForeground(Color.RED);
					p.add(error);
					errcount = 1;
					}
					JOptionPane.showConfirmDialog(null,p,"Patient id", JOptionPane.DEFAULT_OPTION);
					continue;
				}
				else
				{
					int id = Integer.parseInt(option.getText());
					
					try {
						Connection hospitalconnection;
						hospitalconnection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+hospitaldb,uname,pw);
						PreparedStatement ps = hospitalconnection.prepareStatement("select count(*) from Patient where PatientId = ? ");
						ps.setInt(1, id);
						ResultSet rs = ps.executeQuery();
						if(rs.next())
						{
							int count = rs.getInt("count(*)");
							if(count == 0)
							{
								if (errcount != 1) {
									JLabel error = new JLabel("You may have entered an invalid ID");
									error.setForeground(Color.RED);
									p.add(error);
									errcount = 1;
								}
								JOptionPane.showConfirmDialog(null,p,"Patient id", JOptionPane.DEFAULT_OPTION);
								continue;
							}
							else
							{
								String patientID = "";
								String substance = "";
								String reaction = "";
								String status = "";
								String planID = "";
								String activity = "";
								String scheduleDate = "";
								
								Statement s1 = (Statement) hospitalconnection.createStatement();
								Statement s2 = (Statement) hospitalconnection.createStatement();
								Statement s3 = (Statement) hospitalconnection.createStatement();
								
								ResultSet info = s1.executeQuery("select * from Allergies where PatientID = " + id);
								if(info.next())
								{
									patientID = Integer.toString(id);
									substance = info.getString("Substance");
									reaction = info.getString("Reaction");
									status = info.getString("Status");
									if(substance == null || substance.equals(""))
										substance = "None";
									if(reaction == null || reaction.equals(""))
										reaction = "None";
									if(status == null || status.equals(""))
										status = "None";
								}
								
								info = s2.executeQuery("select count(*) from Plan where PatientID = " + id);
								if(info.next())
								{
									int count2 = info.getInt("count(*)");
									if(count2 == 0)
									{
										planID = "None";
										activity = "None";
										scheduleDate = "None";
									}
									else
									{
										info = s3.executeQuery("select * from Plan where PatientID = " + id);
										if(info.next())
										{
											planID = info.getString("PlanID");
											activity = info.getString("Activity");
											scheduleDate = info.getString("scheduleDate");
										}
									}
								}
								Doctor d = new Doctor(patientID, substance, reaction, status, planID, activity, scheduleDate, hospitalconnection);
								break;
							}
						}
						
						
						
						
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
