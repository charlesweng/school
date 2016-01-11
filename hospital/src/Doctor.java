import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Doctor {
	private String patientID;
	private String substance;
	private String reaction;
	private String status;
	private String planID;
	private String activity;
	private String scheduleDate;
	
	private JPanel p;
	
	public Doctor(String patientid, String substance, String reaction, String status, String planID, String activity, String scheduleDate, Connection hospitalconnection)
	{
		this.patientID = patientid;
		this.substance = substance;
		this.reaction = reaction;
		this.status = status;
		this.planID = planID;
		this.activity = activity;
		this.scheduleDate = scheduleDate;
		
		p = new JPanel();
		JTextField option = new JTextField(20);
		p.add(new JLabel("Enter 'view', 'edit', or 'exit': "));
		p.add(option);
		JOptionPane.showConfirmDialog(null,p,"Would you like to edit or view your patient's information?", JOptionPane.DEFAULT_OPTION);
		
		while(true)
		{
			String answer = option.getText().toLowerCase();
			if(answer.equals("view"))
			{
				String[] infoArray = {
						"Patient ID: " , this.patientID,
						"Substance: ", this.substance,
						"Reaction: ", this.reaction,
						"Status: ", this.status,
						"Plan ID: ", this.planID,
						"Activity: ", this.activity,
						"Schedule Date: ", this.scheduleDate
				};
				
				JPanel p2 = new JPanel();
				p2.setLayout(new GridLayout(infoArray.length/2 + 4,1));
				p2.add(new JLabel(infoArray[0]));
				p2.add(new JLabel(infoArray[1]));
				p2.add(new JLabel(""));
				p2.add(new JLabel(""));
				p2.add(new JLabel("Allergy Information"));
				p2.add(new JLabel("-------------------"));
				for (int i = 2; i < 8; i++)
				{
					p2.add(new JLabel(infoArray[i]));
				}
				p2.add(new JLabel(""));
				p2.add(new JLabel(""));
				p2.add(new JLabel("Plan Information"));
				p2.add(new JLabel("-------------------"));
				for (int i = 8; i < infoArray.length; i++)
				{
					p2.add(new JLabel(infoArray[i]));
				}
				JOptionPane.showMessageDialog(null, p2, "Patient Information", JOptionPane.OK_CANCEL_OPTION);				
				JOptionPane.showConfirmDialog(null,p,"Would you like to edit or view your information?", JOptionPane.DEFAULT_OPTION);

			}
			else if(answer.equals("edit"))
			{
				JPanel p3 = new JPanel();
				String[] allergyLabels = {
						"Substance: ",
						"Reaction: ",
						"Status: "
				};
				String[] planLabels = {
						"Plan ID: ",
						"Activity: ",
						"Schedule Date: "
				};
				String[] allergyInfo = {
						this.substance,
						this.reaction,
						this.status
				};
				String[] planInfo = {
						this.planID,
						this.activity,
						this.scheduleDate
				};
				p3.setLayout(new GridLayout(allergyLabels.length+planLabels.length+4,1));
				
				p3.add(new JLabel("Patient ID: "));
				p3.add(new JLabel(this.patientID));
				p3.add(new JLabel(""));
				p3.add(new JLabel(""));
				p3.add(new JLabel("--------------------------Allergy------"));
				p3.add(new JLabel("--Information--------------------------"));
				
				JTextField[] input = new JTextField[allergyLabels.length];
				for (int i = 0; i < allergyInfo.length; i++)
				{
					input[i] = new JTextField(20);
					input[i].setText(allergyInfo[i]);
				}
				for (int i = 0; i < allergyInfo.length; i++)
				{
					p3.add(new JLabel(allergyLabels[i]));
					p3.add(input[i]);
				}
				JTextField[] input2 = new JTextField[planLabels.length];
				for (int i = 0; i < planInfo.length; i++)
				{
					input2[i] = new JTextField(20);
					input2[i].setText(planInfo[i]);
				}
				for (int i = 0; i < planInfo.length; i++)
				{
					p3.add(new JLabel(planLabels[i]));
					if(i == 0)
					{
						//if there is not an entry in the plan table - can insert one
						if(this.planID.equals("None"))
							p3.add(input2[i]);
						else
							p3.add(new JLabel(planInfo[i]));
					}
					else
						p3.add(input2[i]);
				}
				int result = JOptionPane.showConfirmDialog(null, p3, "Update Patient Information", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					try {
						PreparedStatement ps = (PreparedStatement) hospitalconnection.prepareStatement("Update Allergies set Substance = ?, Reaction = ?, Status = ? where PatientID = ?");
						ps.setString(1, input[0].getText());
						this.substance = input[0].getText();
						ps.setString(2, input[1].getText());
						this.reaction = input[1].getText();
						ps.setString(3, input[2].getText());
						this.status = input[2].getText();
						ps.setString(4, this.patientID);
						ps.executeUpdate();
						
						if(this.planID.equals("None"))
						{
							//if there was originally no plan but now the doctor adds one
							if(!input2[0].getText().equals("None") && !input2[0].getText().equals(""))
							{
								int pid = Integer.parseInt(input2[0].getText());
								PreparedStatement ps3 = (PreparedStatement) hospitalconnection.prepareStatement("select count(*) from Plan where PlanID = ?");
								ps3.setInt(1, pid);
								ResultSet rs = ps3.executeQuery();
								if(rs.next())
								{
									int count = Integer.parseInt(rs.getString("count(*)"));
									if(count == 0)
									{
										PreparedStatement ps2 = (PreparedStatement) hospitalconnection.prepareStatement("insert into Plan values(?,?,?,?)");
										ps2.setInt(1, Integer.parseInt(input2[0].getText()));
										this.planID = input2[0].getText();
										ps2.setString(2, input2[1].getText());
										this.activity = input2[1].getText();
										if(input2[2].getText().equals("None"))
											ps2.setNull(3, Types.DATE);
										else
										{
											try {
												DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
												Date d;
												d = df.parse(input2[2].getText());
												SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												String newDate = sdf.format(d);
												ps2.setString(3, newDate);
												this.scheduleDate = newDate;
											} catch (ParseException e) {
												System.out.println("Illegal Date Value, inserting null");
												ps2.setNull(3, Types.DATE);
											}
										}
										ps2.setInt(4, Integer.parseInt(this.patientID));
										ps2.executeUpdate();
									}
									else
									{
										//should let the doctor know that the planID has already been used
									}
								}
							}		
						}
						else
						{
							PreparedStatement ps2 = (PreparedStatement) hospitalconnection.prepareStatement("Update Plan set Activity = ?, scheduleDate = ? where PatientID = ?");
							ps2.setString(1, input2[1].getText());
							this.activity = input2[1].getText();
							ps2.setString(2, input2[2].getText());
							this.scheduleDate = input2[2].getText();
							ps2.setString(3, this.patientID);
							ps2.executeUpdate();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (result == JOptionPane.CANCEL_OPTION)
				{
					System.out.println("update cancelled");
				}
				JOptionPane.showConfirmDialog(null,p,"Would you like to edit or view your information?", JOptionPane.DEFAULT_OPTION);
			}
			else if(answer.equals("exit"))
			{
				return;
			}
			else
			{
				JOptionPane.showMessageDialog(null, p, "Please enter 'view', 'edit', or 'exit' ", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
