package edu.ucsb.cs.cs185.charlesweng.charleswengGPSCam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity implements android.location.LocationListener {

	Button photoButton;
	ImageView image;
	int photoCounter;
	SharedPreferences myPref;
	Editor editor;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	String imagePath;
	String gpsPath;
	String photoName;
	LocationManager locationManager;
	Location l;
	double longitude;
	double lattitude;
	View myMain;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		myMain = (View)findViewById(R.layout.activity_main);
		image = (ImageView)findViewById(R.id.image);
		myPref = this.getSharedPreferences(getString(R.string.photoCounter), MODE_PRIVATE);
		editor = myPref.edit();
		photoCounter = myPref.getInt(getString(R.string.photoCounter), 1);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
            	settingsAction(this.findViewById(R.layout.activity_main));
                return true;
            case R.id.action_take_photo:
				takePhoto(this.findViewById(R.layout.activity_main));
                return true;
            case R.id.action_help:
            	try {
				helpAction(this.findViewById(R.layout.activity_main));
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//	
	public void takePhoto(View v)
	{
		Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(takePicIntent.resolveActivity(getPackageManager())!=null) 
		{
			File photo = null;
			try {
				photo = createImageFile();
				
			} catch (IOException e) {
//				 TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (photo!=null)
			{
				takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
				startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
				photoName = photo.getName();
			}
		}
				
	}
	
	public void onActivityResult(int requestcode, int resultcode, Intent data){
    	if (requestcode==REQUEST_IMAGE_CAPTURE && resultcode==RESULT_OK)
    	{
    		//Decoding/Setting Background Picture
//    		BitmapFactory factory = new BitmapFactory();
    		@SuppressWarnings("deprecation")
			BitmapDrawable myPic= new BitmapDrawable(BitmapFactory.decodeFile(imagePath));
    		View main = (View) this.findViewById(R.id.main_layout);
    		main.setBackground(myPic);
    		
    		//Getting Longitude and Lattitude
    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    		Location currLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    		lattitude=currLoc.getLatitude();
    		longitude=currLoc.getLongitude();
    		
    		//Making string into
    		String imgXML = "<image>\n<name>"+photoName+"</name>\n<lat>"+lattitude+"</lat>\n<long>"+longitude+"</long>\n</image>\n\n";
    		
    		//Storing the image names inside the xml
    		File PicListGPS = new File(gpsPath+File.separator+getString(R.string.PicListXML));
    		if (!PicListGPS.exists())
    		{
    			try {
					PicListGPS.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		//File writer
    		try {
    			BufferedWriter writer = new BufferedWriter(new FileWriter(PicListGPS,true));
    			writer.append(imgXML);
    			writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		//increment photoCounter
    		photoCounter++;
    		editor.putInt(getString(R.string.photoCounter),photoCounter);
        	editor.commit();
    	}
	}
	
	
    public File createImageFile() throws IOException {
    	// Counter for photos taken
    	photoCounter = myPref.getInt(getString(R.string.photoCounter), 1);
    	
    	// Create an image file name
        String imageFileName = getString(R.string.photo) + String.format("%03d", photoCounter) + getString(R.string.jpg);
        File storageDir = new File(Environment.getExternalStorageDirectory()+File.separator+getString(R.string.GPSPics));
        storageDir.mkdir();
       
        File image = new File(storageDir, imageFileName);
        // Save a file: path for use with ACTION_VIEW intents
        gpsPath = storageDir.getAbsolutePath();
        imagePath = image.getAbsolutePath();
        return image;
    }
    
    public void helpAction(View v) throws NameNotFoundException
    {
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle(getString(R.string.myname));
    	alertDialog.setMessage(getString(R.string.myname)+"\n\nVersion: "+getPackageManager().getPackageInfo(getPackageName(), 0).versionName+getString(R.string.joke));
    	alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
    	{
    		public void onClick(DialogInterface dialog, int which)
    		{
    			dialog.dismiss();
    		}
    	});
    	alertDialog.show();
    }
    public void settingsAction(View v)
    {
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle(getString(R.string.settings));
    	alertDialog.setMessage(getString(R.string.haha));
    	alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener()
    	{
    		public void onClick(DialogInterface dialog, int which)
    		{
    			dialog.dismiss();
    		}
    	});
    	alertDialog.show();
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}
