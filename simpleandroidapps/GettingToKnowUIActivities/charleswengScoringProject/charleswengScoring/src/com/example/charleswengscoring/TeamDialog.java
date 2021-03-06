package com.example.charleswengscoring;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Toast;

public class TeamDialog extends DialogFragment implements View.OnClickListener {
	
	AutoCompleteTextView editTeam1, editTeam2;
	EditText editScore1, editScore2;
	Button doneButton;
	boolean changeText = false;
//	ChangeText ct;
	View.OnClickListener myListner;
	String [] teamNames;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_teamdialog, container, false);
		getDialog().setTitle("Enter Game");
		teamNames = getResources().getStringArray(R.array.team_name);
		ArrayAdapter <String> adapter = 
				new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, teamNames);
		
		editTeam1 = (AutoCompleteTextView) view.findViewById(R.id.editTeam1);
		editTeam2 = (AutoCompleteTextView) view.findViewById(R.id.editTeam2);
		editScore1 = (EditText) view.findViewById(R.id.editScore1);
		editScore2 = (EditText) view.findViewById(R.id.editScore2);
		doneButton = (Button) view.findViewById(R.id.doneButton);
		doneButton.setOnClickListener(this);
		editTeam1.setAdapter(adapter);
		editTeam2.setAdapter(adapter);
//		ct = (ChangeText) view;
//		setArguments(savedInstanceState);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		editTeam1 = (EditText) getActivity().findViewById(R.id.editTeam1);
//		editTeam2 = (EditText) getActivity().findViewById(R.id.editTeam2);
//		editScore1 = (EditText) getActivity().findViewById(R.id.editScore1);
//		editScore2 = (EditText) getActivity().findViewById(R.id.editScore2);
//		doneButton = (Button) getActivity().findViewById(R.id.doneButton);
//		doneButton.setOnClickListener(this);
//		ct = (ChangeText) getActivity();
	}
	
	public void editTexts(View v)
	{
//		ct.setText(editTeam1.getText().toString(), editScore1.getText().toString(), editTeam2.getText().toString(), editScore1.getText().toString());
//		Log.d("editTexts onclick", "the method is working");
//		changeText = true;
//		this.dismiss();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ChangeText ct = (ChangeText) getActivity();
		Log.d("hello", editTeam1.getText().toString());
//			Toast.makeText(getActivity(), "my input field is empty", Toast.LENGTH_SHORT);
		String team1 = editTeam1.getText().toString();
		String team2 = editTeam2.getText().toString();
		String score1 = editScore1.getText().toString();
		String score2 = editScore2.getText().toString();
		
//		if (team1.length()==0 || team2.length()==0 || score1.length()==0 | score2.length()==0)
//		{
//			
//		}
//		else
//		{
//			ct.setText(team1, score1, team2, score2);
//		}
		ct.setText(team1, score1, team2, score2);

		this.dismiss();
	}

//	public void onStop()
//	{
//		super.onStop();
//		if (changeText==true)
//			ct.setText(editTeam1.getText().toString(), editScore1.getText().toString(), editTeam2.getText().toString(), editScore1.getText().toString());
//		changeText = false;
//	}
	
}
