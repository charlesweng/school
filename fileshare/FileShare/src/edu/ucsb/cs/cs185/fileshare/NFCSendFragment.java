package edu.ucsb.cs.cs185.fileshare;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * Attempt to actually do the sending files through NFC, but we do not have 2 devices with NFC and for some reason the new API does not detect my phone's NFC
 */
public class NFCSendFragment extends DialogFragment {

	NfcAdapter mNfcAdapter;
	boolean mAndroidBeamAvailable = false;
	
	Activity main;
	
	
    // Instance that returns available files from this app
	Uri fileUri;
    
    @SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_receive, null);
        
        main = getActivity();
        PackageManager pm = main.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC))
        {
        	//disable transferring features
        } 
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
        // If Android Beam isn't available, don't continue.
        mAndroidBeamAvailable = false;
        /*
         * Disable Android Beam file transfer features here.
         */
        // Android Beam file transfer is available, continue
        } 
        else 
        {
        	mNfcAdapter = NfcAdapter.getDefaultAdapter(main);
            /*
             * Instantiate a new FileUriCallback to handle requests for
             * URIs
             */
//            mFileUriCallback = new FileUriCallback();
            // Set the dynamic callback for URI requests.
//            mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback,main);
        }

        return v;
    }
    // List of URIs to provide to Android Beam
    private Uri[] mFileUris = new Uri[10];
    /**
     * Callback that Android Beam file transfer calls to get
     * files to share
     */
    @SuppressLint("NewApi")
	private class FileUriCallback implements NfcAdapter.CreateBeamUrisCallback {
        
    	/**
         * Create content URIs as needed to share with another device
         */
        @Override
        public Uri[] createBeamUris(NfcEvent event) {
        	 mFileUris = new Uri[10];
             String transferFile = "transferimage.jpg";
             File extDir = main.getExternalFilesDir(null);
             File requestFile = new File(extDir, transferFile);
             requestFile.setReadable(true, false);
             // Get a URI for the File and add it to the list of URIs
             fileUri = Uri.fromFile(requestFile);
             if (fileUri != null) {
                 mFileUris[0] = fileUri;
             } else {
                 Log.e("My Activity", "No File URI available for file.");
             }
            return mFileUris;
        }
    }

}
