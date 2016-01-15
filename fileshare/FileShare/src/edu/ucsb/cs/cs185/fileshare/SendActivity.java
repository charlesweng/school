package edu.ucsb.cs.cs185.fileshare;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SendActivity extends DialogFragment{
	Button cancel;
	
	OnClickListener cancelListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			getDialog().dismiss();
		}
		
	};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate (R.layout.activity_send,null);
		getDialog().setTitle(getString(R.string.sending));
		cancel = (Button) view.findViewById(R.id.snd_cancel);
		cancel.setOnClickListener(cancelListener);
		return view;
	}
	
}
