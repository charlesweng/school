package com.example.charleswengscoring;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
//import android.widget.Toast;

public class TeamFragment extends Fragment{
	
	TextView team1text, team2text, score1text, score2text;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_team, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		team1text = (TextView) getActivity().findViewById(R.id.team1text);
		team2text = (TextView) getActivity().findViewById(R.id.team2text);
		score1text = (TextView) getActivity().findViewById(R.id.score1text);
		score2text = (TextView) getActivity().findViewById(R.id.score2text);
	}
	public void changeTexts(String team1, String score1, String team2, String score2)
	{
			if (team1.length()!=0)
			{
//				Toast.makeText(getActivity(), "Team Changed", Toast.LENGTH_SHORT).show();
				team1text.setText(team1);
			}
			if (score1.length()!=0)
			{
//				Toast.makeText(getActivity(), "Score Changed", Toast.LENGTH_SHORT).show();
				score1text.setText(score1);
			}
			if (team2.length()!=0)
			{
//				Toast.makeText(getActivity(), "Team Changed", Toast.LENGTH_SHORT).show();
				team2text.setText(team2);
			}
			if (score2.length()!=0)
			{
//				Toast.makeText(getActivity(), "Score Changed", Toast.LENGTH_SHORT).show();
				score2text.setText(score2);
			}
	}
//	public void makeSmall()
//	{
//		team1text.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
//		team2text.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
//		score1text.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
//		score2text.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
//	}
//	
//	public void makeLarge()
//	{
//		team1text.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
//		team2text.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
//		score1text.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
//		score2text.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
//	}
}