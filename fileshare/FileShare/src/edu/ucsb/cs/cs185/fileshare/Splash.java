package edu.ucsb.cs.cs185.fileshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread timer = new Thread(){
			public void run(){ 
				try{
					sleep(3000); //sets splash screen for 3 seconds
				} catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent FileShare = new Intent("edu.ucsb.cs.cs185.fileshare.MAINACTIVITY"); 
					startActivity(FileShare); 
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish(); //removes splash activity
	}
	

}
