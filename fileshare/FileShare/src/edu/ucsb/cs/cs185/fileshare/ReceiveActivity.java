package edu.ucsb.cs.cs185.fileshare;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ReceiveActivity extends DialogFragment{
	
	//0 is receive and 1 is send
	int option = 0;
	Button cancel;
	

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate (R.layout.activity_receive,null);
		getDialog().setTitle(getString(R.string.receiving));
		cancel = (Button) view.findViewById(R.id.rcv_cancel);
		cancel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
			}
			
		});
		return view;
	}
	
	

}
