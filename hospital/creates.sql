USE Hospital;

DROP TABLE IF EXISTS Visits;

DROP TABLE IF EXISTS Plan;

DROP TABLE IF EXISTS Allergies;

DROP TABLE IF EXISTS DescribedBy;

DROP TABLE IF EXISTS FamilyHistory;

DROP TABLE IF EXISTS Assigned;

DROP TABLE IF EXISTS Patient;

DROP TABLE IF EXISTS LabTestReports;

DROP TABLE IF EXISTS Author;

DROP TABLE IF EXISTS InsuranceCompany;

DROP TABLE IF EXISTS Guardians;

USE Hospital;

CREATE TABLE IF NOT EXISTS Guardians(GuardianNo int, FirstName char(100), LastName char(50), Phone char(20), Address char(100), City char(50),
									 State char(20), Zip int, PRIMARY KEY(GuardianNo));

CREATE TABLE IF NOT EXISTS InsuranceCompany(PayerID int, Name char(50), Purpose char(50), PolicyType char(50), PRIMARY KEY(PayerID));

CREATE TABLE IF NOT EXISTS Author(AuthorID int, AuthorTitle char(100), AuthorFirstName char(50), AuthorLastName char(50), ParticipatingRole char(50),
								  PRIMARY KEY(AuthorID));


CREATE TABLE IF NOT EXISTS LabTestReports(LabTestResultsID int, PatientVisitID int, LabTestPerformedDate datetime, LabTestType char(50), 
										  TestResultValue int, ReferenceRangeHigh char(50), ReferenceRangeLow char(50), PRIMARY KEY(LabTestResultsID), UNIQUE(LabTestResultsID, PatientVisitID));

CREATE TABLE IF NOT EXISTS Patient(PatientID int, PatientRole int, GivenName char(100), FamilyName char(50), Suffix char(10), Gender char(8),
									Birthtime datetime, ProviderID char(50), xmlHealthCreationDateTime datetime, PayerID int,
                                    PRIMARY KEY(PatientID), FOREIGN KEY(PatientRole) REFERENCES Guardians(GuardianNo), 
                                    FOREIGN KEY(PayerID) REFERENCES InsuranceCompany(PayerID));
							                                          
CREATE TABLE IF NOT EXISTS Assigned(PatientID int, AuthorID int, PRIMARY KEY(PatientID, AuthorID), FOREIGN KEY(PatientID) REFERENCES Patient(PatientID),
									FOREIGN KEY(AuthorID) REFERENCES Author(AuthorID));
                                    
CREATE TABLE IF NOT EXISTS FamilyHistory(RelativeID int, Relationship char(50), Age int, Diagnosis char(100), PRIMARY KEY(RelativeID));

CREATE TABLE IF NOT EXISTS DescribedBy(PatientID int, RelativeID int, PRIMARY KEY(PatientID, RelativeID), FOREIGN KEY(PatientID) REFERENCES Patient(PatientID),
										FOREIGN KEY(RelativeID) REFERENCES FamilyHistory(RelativeID));
                                        
CREATE TABLE IF NOT EXISTS Allergies(AllergyID int, PatientID int NOT NULL, Substance char(50), Reaction char(50), Status char(20), PRIMARY KEY(AllergyID), FOREIGN KEY(PatientID) REFERENCES Patient(PatientID));

CREATE TABLE IF NOT EXISTS Plan(PlanID int, Activity char(50), ScheduleDate datetime, PatientID int NOT NULL, PRIMARY KEY(PlanID), 
								FOREIGN KEY(PatientID) REFERENCES Patient(PatientID));
     
CREATE TABLE IF NOT EXISTS Visits(PatientVisitId int, PatientID int, LabTestResultsID int, PRIMARY KEY(PatientVisitId), FOREIGN KEY(PatientID) REFERENCES Patient(PatientID),
								  FOREIGN KEY(LabTestResultsID) REFERENCES LabTestReports(LabTestResultsID));
                                  
