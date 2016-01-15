KLM Analysis Conclusion
=======================
According to the tables made using Raskin's KLM analysis, the overall efficiency of my application (charleswengScoring.apk) is better than the overall efficiency of the given application (KeepScore1S.apk). However, that is assuming that the user uses all the widgets. Furthermore, we have to assume the user is a intermediate user (user between expert/novice).

KeepScore1S.apk:			49.05 seconds
charleswengScoring.apk:		32.7  seconds

Approach To Solving Programming Assignment
==========================================
1. Create the main XML (activity_main.xml) with three buttons on it
2. Add 2 dynamic fragments
	a. 1 dynamic fragment for date (fragment_date.xml)
	b. 1 dynamic fragment for team/scores (fragment_team.xml)
3. Create 2 dialog fragments when the buttons Enter Game and Enter Date are pressed
	a. Used built in DatePickerDialog
		i. 		assigned an DatePickerDialog.onDateListener
		ii. 	assigned an DialogInterface.onClickListener
		iii.	initialized DatePickerDialog with the onDateListener/onClickListener
	b. Created java class: TeamDialogFragment.java xml file: fragment_teamdialog.xml
		i. Made the xml file
		ii.Inflated the class with the xml
4. Created an interface called changeText that changes the text values inside the activity_main.xml

Extra Credit
============
1. Override onSavedInstanceState function in MainActivity.java
	a. put [bundle.putString(key, value)] all the values inside the DateFragment and TeamFragment into the Bundle savedInstanceState
2. Override onRestoreSavedInstanceState function in MainActivity.java
	a. get all values from the Bundle savedInstanceState [bundle.get(key).toString()]
	b. call the interface methods and set the 2 dynamic fragments again

More Information
================
- The application works better on the latest 4.4.2 android phone (I tested using the Nexus 4)
- Make sure to have 4.4.2 sdk installed inside eclipse
- apk is named charleswengScoring.apk