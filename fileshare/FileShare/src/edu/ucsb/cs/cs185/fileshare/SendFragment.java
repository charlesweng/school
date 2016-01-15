package edu.ucsb.cs.cs185.fileshare;


import java.util.HashMap;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

public class SendFragment extends DialogFragment {
	//Button and Booleans
	Button cancel, send;
	RadioButton brian, charles, dennis;
	boolean checkBrian, checkCharles, checkDennis;
	//selected files
	HashMap<String, String> selectedFiles;
	
	//fragment
	FragmentManager manager;
	NFCSendFragment nfc_send;
	SendActivity send_activity;
	
	//listeners
	OnClickListener brianListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			checkBrian = !checkBrian;
			brian.setChecked(checkBrian);
		}
	};
	
	OnClickListener charlesListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			checkCharles = !checkCharles;
			charles.setChecked(checkCharles);
		}
	};
	
	OnClickListener dennisListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			checkDennis = !checkDennis;
			dennis.setChecked(checkDennis);
		}
	};
	
	OnClickListener cancelListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			getDialog().dismiss();
		}
		
	};
	OnClickListener sendListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			getDialog().dismiss();
			manager = getActivity().getFragmentManager();
			send_activity = new SendActivity();
			send_activity.show(manager, "Sending");
		}
		
	};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate (R.layout.send_fragment,null);
		getDialog().setTitle(getString(R.string.selectdevice));
		
		brian = (RadioButton) view.findViewById(R.id.brian);
		checkBrian = brian.isChecked();
		brian.setOnClickListener(brianListener);
		
		charles = (RadioButton) view.findViewById(R.id.charles);
		checkCharles = charles.isChecked();
		charles.setOnClickListener(charlesListener);
		
		dennis = (RadioButton) view.findViewById(R.id.dennis);
		checkDennis = dennis.isChecked();
		dennis.setOnClickListener(dennisListener);
		
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(cancelListener);
		
		send = (Button) view.findViewById(R.id.send);
		send.setOnClickListener(sendListener);
		
		return view;
	}
	
	public void loadFileInfo(HashMap<String, String> selectedFiles)
	{
		this.selectedFiles = selectedFiles; 
	}
	
}
