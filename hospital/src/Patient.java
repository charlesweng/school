



import java.awt.GridLayout;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Patient {
	private String patientid;
	private String patientrole;
	private String givenname;
	private String familyname;
	private String firstname;
	private String lastname;
	private String suffix;
	private String gender;
	private String birthtime;
	private String providerId;
	private String xmlCreationdate;
	private String payerID;
	private String phone;
	private String address;
	private String city;
	private String state;
	private String zip;
	private JPanel p;
	public Patient(String ID, String patientrole, String givenname, String familyname, String suffix, String gender, String birthtime, String providerid, String xmlCreationDate, String payerID, String phone, String address, String city, String state, String zip, String firstname, String lastname, Connection hospitalConnection)
	{
		this.patientid = ID;
		this.patientrole = patientrole;
		this.givenname = givenname;
		this.familyname = familyname;
		this.suffix = suffix;
		this.gender = gender;
		this.birthtime = birthtime;
		this.providerId = providerid;
		this.xmlCreationdate = xmlCreationDate;
		this.payerID = payerID;
		this.phone = phone;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.firstname = firstname;
		this.lastname = lastname;
		
		p = new JPanel();
		JTextField option = new JTextField(20);
		p.add(new JLabel("Enter 'view', 'edit', or 'exit': "));
//		p.add(new JButton("View"));
//		p.add(new JButton("Edit"));
//		p.add(new JButton("Exit"));
		p.add(option);
		JOptionPane.showConfirmDialog(null,p,"Would you like to edit or view your information?", JOptionPane.DEFAULT_OPTION);
		
		while(true)
		{
			String answer = option.getText().toLowerCase();
			if(answer.equals("view"))
			{
				
				String[] infoArray = 
						{"Patient ID: " , this.patientid,

			             "XML Creation Date:  " , this.xmlCreationdate,
			             "Given Name:  " , this.givenname,
			             "Family Name:  " , this.familyname,
			             "Suffix:  " , this.suffix,
			             "Gender:  " , this.gender,
			             "Birth Time:  " , this.birthtime,
			             "Provider ID:  " , this.providerId,
			             "PayerID:  " , this.payerID,
			             "Guardian No:  " , this.patientrole,
			             "FirstName: "	, this.firstname,
			             "LastName: " , this.lastname,
			             "Phone: ", this.phone,
			             "Address: ", this.address,
			             "City: ", this.city,
			             "State: ", this.state,
			             "Zip: ", this.zip };
				
				JPanel p2 = new JPanel();
				p2.setLayout(new GridLayout(infoArray.length/2,1));
				for (int i = 0; i < infoArray.length; i++)
				{
					p2.add(new JLabel(infoArray[i]));
				}
				JOptionPane.showMessageDialog(null, p2, "Patient Information", JOptionPane.OK_CANCEL_OPTION);

				
				JOptionPane.showConfirmDialog(null,p,"Would you like to edit or view your information?", JOptionPane.DEFAULT_OPTION);
			}
			else if(answer.equals("edit"))
			{
				JPanel p3 = new JPanel();
		        String[] infoArray = 
		            { this.patientid,
		              this.xmlCreationdate,
		              this.givenname,
		              this.familyname,
		              this.suffix,
		              this.gender,
		              this.birthtime,
		              this.providerId,
		              this.payerID,
		              this.patientrole
		              };
				String[] labelArray =
					{    "Patient ID: " , 
			             "XML Creation Date:  ",
			             "Given Name:  " , 
			             "Family Name:  " , 
			             "Suffix:  ",
			             "Gender:  ",
			             "Birth Time:  " , 
			             "Provider ID:  " ,
			             "PayerID:  ",
			             "Guardian No:  "};
				String [] guardianInfo = {
						this.firstname,
						this.lastname,
						this.phone,
						this.address,
						this.city,
						this.state,
						this.zip
				};
				String [] guardianLabel = {
						"FirstName: ",
						"LastName: ",
						"Phone: ",
						"Address: ",
						"City: ",
						"State: ",
						"Zip: "
				};
				p3.setLayout(new GridLayout(labelArray.length+guardianLabel.length,1));
				JTextField[] input = new JTextField[labelArray.length];
				if (this.birthtime == null)
				{
					infoArray[6] = "0000-00-00 00:00:00";
				}
				for (int i = 0; i < infoArray.length; i++)
				{
					input[i] = new JTextField(20);
					input[i].setText(infoArray[i]);
				}
				for (int i = 0; i < infoArray.length; i++)
				{
					p3.add(new JLabel(labelArray[i]));
					if (i != 0 && i != 1 && i != 9 && i !=8)
					p3.add(input[i]);
					else
					p3.add(new JLabel(infoArray[i]));
				}
				JTextField[] input2 = new JTextField[guardianLabel.length];
				for (int i = 0; i < guardianLabel.length;i++) {
					input2[i] = new JTextField(20);
					input2[i].setText(guardianInfo[i]);
				}
				for (int i = 0; i < guardianLabel.length;i++) {
					p3.add(new JLabel(guardianLabel[i]));
					p3.add(input2[i]);
				}
				int result = JOptionPane.showConfirmDialog(null, p3, "Update Patient Information", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{				
					
					try {
						
						PreparedStatement updatexmldate = (PreparedStatement) hospitalConnection.prepareStatement("update Patient set xmlHealthCreationDateTime = ? where PatientID = "+this.patientid);
						String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
						this.xmlCreationdate = timeStamp;
						updatexmldate.setString(1, timeStamp);
						updatexmldate.executeUpdate();
						
						PreparedStatement ps = (PreparedStatement) hospitalConnection.prepareStatement("Update Patient set GivenName = ?, FamilyName = ?, Suffix = ?, Gender = ?, Birthtime = ?, ProviderID = ?, PayerID = ? where PatientID = "+this.patientid);
							ps.setString(1, input[2].getText());
							this.givenname = input[2].getText();
							ps.setString(2, input[3].getText());
							this.familyname = input[3].getText();
							ps.setString(3, input[4].getText());
							this.suffix = input[4].getText();
							ps.setString(4, input[5].getText());
							this.gender = input[5].getText();
							String birth = input[6].getText();
							
							if (birth=="0000-00-00 00:00:00")
							{
								ps.setNull(5, Types.DATE);
								System.out.println("empty birth date");
							}
							else
							{
//							try {
//								DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//								Date d;
//								if (birth.length() > 19)
//								{
//									birth = birth.substring(0, birth.length()-(birth.length()-19));
//								}
//								d = df.parse(birth);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//								birth = sdf.format(birth);
								ps.setString(5,birth);
//							} catch (ParseException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							}

							this.birthtime = birth;
							ps.setString(6, input[7].getText());
							this.providerId = input[7].getText();
							if (!input[8].getText().equals(""))
							{
								ps.setInt(7, Integer.parseInt(input[8].getText()));
							}
							else
								ps.setNull(7, Types.INTEGER);
							this.payerID = input[8].getText();
						ps.executeUpdate();
							if (this.patientrole != null){
								PreparedStatement ps2 = (PreparedStatement) hospitalConnection.prepareStatement("Update Guardians set Phone = ?, Address = ?, City = ?, State = ?, Zip = ?, FirstName = ?, LastName = ? where GuardianNo = "+patientrole);
									ps2.setString(1, input2[2].getText());
									this.phone = input2[2].getText();
									ps2.setString(2, input2[3].getText());
									this.address = input2[3].getText();
									ps2.setString(3, input2[4].getText());
									this.city = input2[4].getText();
									ps2.setString(4, input2[5].getText());
									this.state = input2[5].getText();
									ps2.setString(5, input2[6].getText());
									this.zip = input2[6].getText();
									ps2.setString(6, this.firstname);
									this.firstname = input2[0].getText();
									ps2.setString(7, this.lastname);
									this.lastname = input2[1].getText();
		//							ps.setInt(6, Integer.parseInt(patientrole));
								ps2.executeUpdate();
							}
							else
							{
								System.out.println("Cannot update guardian table because patientrole is null");
							}
							
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					System.out.println("updating db");
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
