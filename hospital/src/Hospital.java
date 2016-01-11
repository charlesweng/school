
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Hospital {
	public enum Tables {
		guardians, insurancecompany, author, labtestreports, patient, assigned, familyhistory, describedby, allergies, plan, visits
	}
	Connection messageconnection;
	Connection hospitalconnection;
	int recordcount = 1;
	boolean allinserted = true;
	public Hospital(String messagedb, String messagetbl, String hospitaldb, String uname, String pword) {
		Statement messagestmt = null;
		Statement hospitalstmt = null;
		PreparedStatement updateMessages = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			messageconnection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+messagedb, uname,pword);
			System.out.println("connecting to messages db...");
			System.out.println("=================================================");
			updateMessages = messageconnection.prepareStatement("update "+messagetbl+" set Last_Accessed = ?");
			String timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
			updateMessages.setString(1, timeStamp);
			updateMessages.executeUpdate();
			
			String sql;
			messagestmt = (Statement) messageconnection.createStatement();
			sql = "SELECT * FROM "+messagetbl;
			ResultSet messagers = messagestmt.executeQuery(sql);
			
			hospitalconnection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+hospitaldb,uname,pword);
			hospitalstmt = (Statement) hospitalconnection.createStatement();
			hospitalstmt.executeUpdate("delete from Allergies");
			hospitalstmt.executeUpdate("delete from DescribedBy");
			hospitalstmt.executeUpdate("delete from FamilyHistory");
			hospitalstmt.executeUpdate("delete from Assigned");
			hospitalstmt.executeUpdate("delete from Plan");
			hospitalstmt.executeUpdate("delete from Visits");
			hospitalstmt.executeUpdate("delete from Patient");
			hospitalstmt.executeUpdate("delete from Guardians");
			hospitalstmt.executeUpdate("delete from InsuranceCompany");
			hospitalstmt.executeUpdate("delete from Author");
			hospitalstmt.executeUpdate("delete from LabTestReports");
			
			while (messagers.next()) {
				PreparedStatement insert;
				String Activity = messagers.getString("Activity");
				String address = messagers.getString("address");
				String age = messagers.getString("age");
				String AuthorFirstName = messagers.getString("AuthorFirstName");
				String AuthorId = messagers.getString("AuthorId");
				if ( AuthorId!= null)
					AuthorId = AuthorId.substring(4);
				String AuthorLastName = messagers.getString("AuthorLastName");
				String AuthorTitle = messagers.getString("AuthorTitle");
				String BirthTime = messagers.getString("BirthTime");
				String city = messagers.getString("city");
				String Diagnosis = messagers.getString("Diagnosis");
				String FamilyName = messagers.getString("FamilyName");
				String FirstName = messagers.getString("FirstName");
				String givenName = messagers.getString("GivenName");
				String GuardianNo = messagers.getString("GuardianNo");
				String Id = messagers.getString("Id");
				String LabTestPerformedDate = messagers.getString("LabTestPerformedDate");
				String LabTestResultId = messagers.getString("LabTestResultId");
				String LabTestType = messagers.getString("LabTestType");
				String Last_Accessed = messagers.getString("Last_Accessed");
				String LastName = messagers.getString("LastName");
				String MsgId = messagers.getString("MsgId");
				String Name = messagers.getString("Name");
				String ParticipatingRole = messagers.getString("ParticipatingRole");
				String patientId = messagers.getString("patientId");
				String PatientVisitId = messagers.getString("PatientVisitId");
				String PayerId = messagers.getString("PayerId");
				String phone = messagers.getString("phone");
				String PlanId = messagers.getString("PlanId");
				String PolicyHolder = messagers.getString("PolicyHolder");
				String PolicyType = messagers.getString("PolicyType");
				String providerId = messagers.getString("providerId");
				String Purpose = messagers.getString("Purpose");
				String Reaction = messagers.getString("Reaction");
				String ReferenceRangeHigh = messagers.getString("ReferenceRangeHigh");
				String ReferenceRangeLow = messagers.getString("ReferenceRangeLow");
				String Relation = messagers.getString("Relation");
				String Relationship = messagers.getString("Relationship");
				String RelativeId = messagers.getString("RelativeId");
				String ScheduledDate = messagers.getString("ScheduledDate");
				String state = messagers.getString("state");
				String Status = messagers.getString("Status");
				String Substance = messagers.getString("Substance");
				String TestResultValue = messagers.getString("TestResultValue");
				String zip = messagers.getString("zip");

				allinserted = true;
				System.out.println("Info inside record "+recordcount+"");

				//Guardians
				insert = hospitalconnection.prepareStatement("insert into Guardians values(?,?,?,?,?,?,?,?)");
				if (notNull(insert, 1, GuardianNo, Types.INTEGER, Tables.guardians))
				{
					insert.setInt(1, Integer.parseInt(GuardianNo));
					if (notNull(insert, 2, givenName,Types.CHAR, Tables.guardians))
					insert.setString(2, FirstName);
					if (notNull(insert, 3, FamilyName,Types.CHAR, Tables.guardians))
					insert.setString(3, LastName);
					if (notNull(insert, 4, phone,Types.CHAR, Tables.guardians))
					insert.setString(4, phone);
					if (notNull(insert, 5, address,Types.CHAR, Tables.guardians))
					insert.setString(5, address);
					if (notNull(insert, 6, city,Types.CHAR, Tables.guardians))
					insert.setString(6, city);
					if (notNull(insert, 7, state,Types.CHAR, Tables.guardians))
					insert.setString(7, state);
					if (notNull(insert, 8, zip,Types.INTEGER, Tables.guardians))
					insert.setInt(8, Integer.parseInt(zip));
				insert.executeUpdate();
				}
				
				//InsuranceCompany
				insert = hospitalconnection.prepareStatement("insert into InsuranceCompany values(?,?,?,?)");
				if (notNull(insert, 1, PayerId, Types.INTEGER, Tables.insurancecompany))
				{
					insert.setInt(1, Integer.parseInt(PayerId));
					if (notNull(insert, 2, Name,Types.CHAR, Tables.insurancecompany))
					insert.setString(2, Name);
					if (notNull(insert, 3, Purpose,Types.CHAR, Tables.insurancecompany))
					insert.setString(3, Purpose);
					if (notNull(insert, 4, PolicyType,Types.CHAR, Tables.insurancecompany))
					insert.setString(4, PolicyType);
				insert.executeUpdate();
				}
				//Author
				insert = hospitalconnection.prepareStatement("insert into Author values(?,?,?,?,?)");
				if (notNull(insert, 1, AuthorId, Types.INTEGER, Tables.author))
				{
					insert.setInt(1, Integer.parseInt(AuthorId));
					if (notNull(insert, 2, AuthorTitle,Types.CHAR, Tables.author))
					insert.setString(2, AuthorTitle);
					if (notNull(insert, 3, AuthorFirstName,Types.CHAR, Tables.author))
					insert.setString(3, AuthorFirstName);
					if (notNull(insert, 4, AuthorLastName,Types.CHAR, Tables.author))
					insert.setString(4, AuthorLastName);
					if (notNull(insert, 5, ParticipatingRole,Types.CHAR, Tables.author))
					insert.setString(5, ParticipatingRole);
				insert.executeUpdate();
				}
				//LabTestReports
				insert = hospitalconnection.prepareStatement("insert into LabTestReports values(?,?,?,?,?,?,?)");
				if (notNull(insert, 1, LabTestResultId, Types.INTEGER, Tables.labtestreports))
				{
					if(LabTestPerformedDate != null)
					{
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						Date d = df.parse(LabTestPerformedDate);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						LabTestPerformedDate = sdf.format(d);
						insert.setString(3, LabTestPerformedDate);
					}
					else
						insert.setNull(3, Types.DATE);
					insert.setInt(1, Integer.parseInt(LabTestResultId));
					if (notNull(insert, 2, PatientVisitId,Types.INTEGER, Tables.labtestreports))
					insert.setInt(2, Integer.parseInt(PatientVisitId));
					if (notNull(insert, 4, LabTestType,Types.CHAR, Tables.labtestreports))
					insert.setString(4, LabTestType);
					if(TestResultValue != null)
						insert.setInt(5, Integer.parseInt(TestResultValue));
					else
						insert.setNull(5, Types.INTEGER);
					if(ReferenceRangeHigh != null)
						insert.setString(6, ReferenceRangeHigh);
					else
						insert.setNull(6, Types.INTEGER);
					if(ReferenceRangeLow != null)
						insert.setString(7, ReferenceRangeLow);
					else
						insert.setNull(7, Types.INTEGER);
				insert.executeUpdate();
				}
				//Patient
				insert = hospitalconnection.prepareStatement("insert into Patient values(?,?,?,?,?,?,?,?,?,?)");
					insert.setInt(1, Integer.parseInt(patientId));
					if (notNull(insert, 3, GuardianNo,Types.INTEGER, Tables.patient))
					insert.setInt(2, Integer.parseInt(GuardianNo));
					else
					insert.setNull(2, Types.INTEGER);
					if (notNull(insert, 3, givenName,Types.CHAR, Tables.patient))
					insert.setString(3, givenName);
					if (notNull(insert, 4, FamilyName,Types.CHAR, Tables.patient))
					insert.setString(4, FamilyName);
					insert.setNull(5, Types.CHAR);//suffix?
					insert.setNull(6, Types.CHAR);//gender?
					if(BirthTime != null)
					{
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						Date d = df.parse(BirthTime);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						BirthTime = sdf.format(d);
						insert.setString(7, BirthTime);
					}
					else
						insert.setNull(7, Types.DATE);
					insert.setString(8, providerId);
					if(Last_Accessed != null)
					{
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						Date d = df.parse(Last_Accessed);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Last_Accessed = sdf.format(d);
						insert.setString(9, Last_Accessed);
					}
					else
						insert.setNull(9, Types.DATE);
					if (notNull(insert, 10, PayerId,Types.INTEGER, Tables.patient))
					insert.setInt(10, Integer.parseInt(PayerId));
					else
					insert.setNull(10, Types.INTEGER);
					insert.executeUpdate();			
					
					//Plan
					if (PlanId != null) {
					if (notNull(insert, 1, PlanId, Types.INTEGER, Tables.plan))
					{
					insert = hospitalconnection.prepareStatement("insert into Plan values(?,?,?,?)");
						insert.setInt(1,Integer.parseInt(PlanId));
						if (Activity != null)
							insert.setString(2,Activity);
						else
							insert.setNull(2, Types.CHAR);
						if (ScheduledDate != null)
						{
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
							Date d = df.parse(ScheduledDate);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							ScheduledDate = sdf.format(d);
							insert.setString(3, ScheduledDate);
						}
						else
							insert.setNull(3, Types.DATE);
					insert.setInt(4, Integer.parseInt(patientId));
					insert.executeUpdate();
					}
					}
					
					//Allergies
					insert = hospitalconnection.prepareStatement("insert into Allergies values(?,?,?,?,?)");
//						if (notNull(insert, 1, Name,Types.INTEGER))
					if (notNull(insert, 1, Id, Types.INTEGER, Tables.allergies))
					{
						insert.setInt(1, Integer.parseInt(Id));
						insert.setInt(2, Integer.parseInt(patientId));
						if (notNull(insert, 3, Substance,Types.CHAR, Tables.allergies))
						insert.setString(3, Substance);
						if (notNull(insert, 4, Reaction,Types.CHAR, Tables.allergies))
						insert.setString(4, Reaction);
						if (notNull(insert, 5, Status,Types.CHAR, Tables.allergies))
						insert.setString(5, Status);
					insert.executeUpdate();
					}
					
					//FamilyHistory
					insert = hospitalconnection.prepareStatement("insert into FamilyHistory values(?,?,?,?)");
					if (notNull(insert, 1, RelativeId, Types.INTEGER, Tables.familyhistory))
					{
						insert.setInt(1, Integer.parseInt(RelativeId));
						if (notNull(insert, 2, Relationship,Types.CHAR, Tables.familyhistory))
						insert.setString(2, Relationship);
						if (notNull(insert, 3, age,Types.CHAR, Tables.familyhistory))
						insert.setInt(3, Integer.parseInt(age));
						if (notNull(insert, 4, Diagnosis,Types.CHAR, Tables.familyhistory))
						insert.setString(4, Diagnosis);
					insert.executeUpdate();			

					
					//DescribedBy
					insert = hospitalconnection.prepareStatement("insert into DescribedBy values(?,?)");
						insert.setInt(1, Integer.parseInt(patientId));
						insert.setInt(2, Integer.parseInt(RelativeId));
					insert.executeUpdate();
					}
					

					
					//Visits
					insert = hospitalconnection.prepareStatement("insert into Visits values(?,?,?)");
					if (notNull(insert, 1, PatientVisitId, Types.INTEGER, Tables.visits)&&notNull(insert, 3, LabTestResultId, Types.INTEGER, Tables.visits))
					{
						insert.setInt(1, Integer.parseInt(PatientVisitId));
						insert.setInt(2, Integer.parseInt(patientId));
						insert.setInt(3, Integer.parseInt(LabTestResultId));
					insert.executeUpdate();
					}
					
					//Assigned
					insert = hospitalconnection.prepareStatement("insert into Assigned values(?,?)");
					if (notNull(insert, 2, AuthorId, Types.INTEGER, Tables.assigned))
					{	
						insert.setInt(1, Integer.parseInt(patientId));
						insert.setInt(2, Integer.parseInt(AuthorId));
						insert.executeUpdate();
					}
					if (allinserted == true)
						System.out.println("Inserted into all tables");
					System.out.println("=================================================");
					recordcount++;

			}
			hospitalconnection.close();
			messageconnection.close();
		   }catch(SQLException se){
			      //Handle errors for JDBC
			      se.printStackTrace();
			   }catch(Exception e){
			      //Handle errors for Class.forName
			      e.printStackTrace();
		   }//end try
	}
	
	public boolean notNull(PreparedStatement insert, int col, String attr, int type, Hospital.Tables table) {
		if (type == Types.INTEGER && attr == null)
		{
			allinserted = false;
			if (table == Tables.allergies) System.out.println("Primary Key (AllergyID) is NULL row not inserted to Allergy table");
			else if (table == Tables.assigned) System.out.println("Primary Key (AuthorID or PatientID) is NULL row not inserted to Assigned table");
			else if (table == Tables.author) System.out.println("Primary Key (AuthorID) is NULL row not inserted to Author table");
			else if (table == Tables.describedby) System.out.println("Primary Key (LabTestResultsID) is NULL row not inserted to DescribedBy table");
			else if (table == Tables.familyhistory) System.out.println("Primary Key (RelativeID) is NULL row not inserted to FamilyHistory table");
			else if (table == Tables.guardians) System.out.println("Primary Key (GuardianNo) is NULL row not inserted to Guardians table");
			else if (table == Tables.insurancecompany) System.out.println("Primary Key (ProviderID) is NULL row not inserted to InsuranceCompany table");
			else if (table == Tables.labtestreports) System.out.println("Primary Key (LabTestResultsID) is NULL row not inserted to LabTestResults table");
			else if (table == Tables.patient) System.out.println("Primary Key (PatientID) is NULL row not inserted to Patient table");
			else if (table == Tables.plan) System.out.println("Primary Key (PlanID) is NULL row not inserted to Plan table");
			else if (table == Tables.visits)System.out.println("Primary Key (LabTestResultsID) is NULL row not inserted to Visits table");

			return false;
		}
		else if (attr == null)
		{
			try {
				insert.setNull(col, type);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		else
			return true;
	}
}
