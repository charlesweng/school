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

public class PatientInterface {
	JPanel p;
	public JPanel getPanel() {
		return p;
	}
	
	public PatientInterface(String messagedb, String hospitaldb, String uname, String pw)
	{
		p = new JPanel();
		JTextField option = new JTextField(20);
		p.setLayout(new GridLayout(3, 0));
		p.add(new JLabel("Enter your patient id: "));
		p.add(option);
		JOptionPane.showConfirmDialog(null,p,"Patient id", JOptionPane.DEFAULT_OPTION);
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
				Connection hospitalconnection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+hospitaldb,uname,pw);
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
						
						Statement ps2 = (Statement) hospitalconnection.createStatement();
						ResultSet info = ps2.executeQuery("select * from ( Patient join  Guardians on Patient.PatientRole = Guardians.GuardianNo ) where PatientId = "+id);
						Statement ps3 = (Statement) hospitalconnection.createStatement();
						ResultSet info2 = ps3.executeQuery("select * from Patient where PatientID = "+id);
						if(info.next())
						{
							String patientID = info.getString("PatientID");
							String patientRole = info.getString("PatientRole");
							String givenName = info.getString("Patient.GivenName");
							String familyName = info.getString("Patient.FamilyName");
							String suffix = "";
							if (info.getString("Suffix")!= null)
								suffix = info.getString("Suffix");
							String gender = "";
							if (info.getString("Gender")!= null)
								gender = info.getString("Gender");
							String birthTime = info.getString("BirthTime");
							String providerID = info.getString("ProviderID");
							String xml = info.getString("xmlHealthCreationDateTime");
							String payerID = info.getString("PayerID");
							String firstname = info.getString("FirstName");
							System.out.println(firstname);
							String lastname = info.getString("LastName");
							String phone = info.getString("Phone");
							String address = info.getString("Address");
							String city = info.getString("City");
							String state = info.getString("State");
							String zip = info.getString("Zip");
							Patient patient = new Patient(patientID, patientRole, givenName, familyName, suffix, gender, birthTime, providerID, xml, payerID, phone, address, city, state, zip, firstname, lastname, hospitalconnection);
//							hospitalconnection.close();
						}
						if (info2.next())
						{
							String patientID = info2.getString("PatientID");
							String patientRole = info2.getString("PatientRole");
							String givenName = info2.getString("GivenName");
							String familyName = info2.getString("FamilyName");
							String suffix = "";
							if (info2.getString("Suffix")!= null)
								suffix = info2.getString("Suffix");
							String gender = "";
							if (info2.getString("Gender")!= null)
								gender = info2.getString("Gender");
							String birthTime = info2.getString("BirthTime");
							String providerID = info2.getString("ProviderID");
							String xml = info2.getString("xmlHealthCreationDateTime");
							String payerID = info2.getString("PayerID");
							String firstname = "";
							String lastname = "";
							String phone = "";//info2.getString("Phone");
							String address = "";//info2.getString("Address");
							String city = "";//info2.getString("City");
							String state = "";//info2.getString("State");
							String zip = "";//info2.getString("Zip");
							Patient patient = new Patient(patientID, patientRole, givenName, familyName, suffix, gender, birthTime, providerID, xml, payerID, phone, address, city, state, zip, firstname, lastname, hospitalconnection);
//							hospitalconnection.close();

						}
						break;
					}//end of big else
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		
		
	}
}
