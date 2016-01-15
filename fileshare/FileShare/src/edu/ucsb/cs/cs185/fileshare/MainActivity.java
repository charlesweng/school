package edu.ucsb.cs.cs185.fileshare;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;



public class MainActivity extends Activity implements ExpandableListAdapter.ExpandableListAdapterListner{
	
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	//record expanded headers
    ArrayList<Integer> expandedHeaders;

	//used for each expandable list
	List<String> removedFiles;
	List<String> listDataHeader;
	
	//used for each file under the listDataHeader
	HashMap<String, List<String>> listDataChild;
	
	//used to grab the path of the file name
	HashMap<String, String> listDataPath;
	
	//list of selected files and their path names
	HashMap<String, String> selectedFiles;
	
	HashMap<String, String> delFileToPath;
	
	//checkbox
	//initialize to false to hide the send button
	boolean Box=false;
	boolean delete=false;
	boolean delete1=false;
	boolean loop=true;
	//sendButton
	MenuItem sendButton;
	MenuItem deleteButton; 
	MenuItem undoButton;
	
	//menu variable
	Menu myMenu;
	
	//this is my actionbar for my button
	ActionBar actionBar;
	
	//send fragment manager
	SendFragment send;
	ReceiveActivity load;
	FragmentManager manager;
	
	//checks for checkboxes
	int numberofCheck=0;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //get the actionbar for the buttons
        actionBar = getActionBar();
        
        //getting the expandable list view from activity_main.xml
        expListView = (ExpandableListView) findViewById(R.id.explist);
        
        //instantiating all the lists and hashmaps
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataPath = new HashMap<String, String>();
        selectedFiles = new HashMap<String, String>();
        delFileToPath = new HashMap<String, String>();
        
        //setting things up for the expandable list view
        File storageDir = Environment.getExternalStorageDirectory();
        findDirectories(storageDir);
        mapFilesToDirectories();

        //creating the listadapter
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        
        //setting list adapter
        expListView.setAdapter(listAdapter);
        
        //instantiating send fragment
		send = new SendFragment();
		load = new ReceiveActivity();
		//our manager
		manager = getFragmentManager();
		
		
		//record expanded things
		expandedHeaders = new ArrayList<Integer>();
		
	    // click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
            	
                return false;
            }
        });
 
        // expand listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
            	if (!expandedHeaders.contains(groupPosition))
            		expandedHeaders.add(groupPosition);
                for (int i = 0; i < expandedHeaders.size(); i++)
                	Log.e("adding expansions", expandedHeaders.get(i)+"");
                
            }
        });
 
        // collapse listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
                
    			for (int i = 0; i < expandedHeaders.size(); i++)
    			{
    				if (groupPosition == expandedHeaders.get(i))
    					expandedHeaders.remove(i);
    				expandedHeaders.remove((Integer) groupPosition);
    				if (i >= listDataHeader.size())
    					break;
    			}
    			for (int i = 0; i < expandedHeaders.size(); i++)
                	Log.e("removing expansions", expandedHeaders.get(i)+"");
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });
    }
        

    private void mapFilesToDirectories() {
    	for (String dirName: listDataHeader)
    	{
    		File dir = new File(Environment.getExternalStorageDirectory()+File.separator+dirName);
    		List<String> fileList = new ArrayList<String>();
    		for (File f: dir.listFiles())
    		{
    			if (f.isFile())
    			{
    				String filename  = f.getName();
    				String filepath = f.getAbsolutePath();
    				if (filename != null && filepath != null)
    				{
    					fileList.add(filename);
    					listDataPath.put(dirName+File.separator+filename, filepath);
    				}
    			}
    		}
    		Collections.sort(fileList);
    		listDataChild.put(dirName, fileList);
    	}
	}

	private void findDirectories(File storageDir)
    {
    	for (File f: storageDir.listFiles()) {
    		if (f.isDirectory())
    		{
    			String filename = f.getName();
    			String filepath = f.getAbsolutePath();
    			if (filename != null && filepath != null)
    			{
    				listDataHeader.add(filename);
    				listDataPath.put(filename, filepath);
    			}
    		}
    	}
    	Collections.sort(listDataHeader);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    myMenu=menu;
	    sendButton=myMenu.findItem(R.id.send);
	    sendButton.setVisible(Box);
	    deleteButton=myMenu.findItem(R.id.delete);
	    deleteButton.setVisible(Box);
	    undoButton=myMenu.findItem(R.id.undo);
	    undoButton.setVisible(delete);
	    return super.onCreateOptionsMenu(menu);
	}
	
	//our menu bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.send:
	    		send = new SendFragment();
	    		send.loadFileInfo(selectedFiles);
	    		send.show(manager,"Send");
	    		return true;
	    	case R.id.delete:
	    		confirmDeletion();
				invalidateOptionsMenu(); 
				return true;
	    	case R.id.search:
	    		searchDialog();
	    		return true;
	    	case R.id.undo:
	    		undoDelete();
	    		return true;
	    	case R.id.receive:
	    		load.show(manager, "Receive");
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	//this a quick undo function
	//this is not perfect
	public void undoDelete(){
		if (delete)
		{
			
			for(String filename:removedFiles){
				if (!listDataChild.get(delFileToPath.get(filename)).contains(filename))
				{
					listDataChild.get(delFileToPath.get(filename)).add(filename);
					Toast.makeText(getApplicationContext(), "Undo Deletion", Toast.LENGTH_SHORT).show();
					Collections.sort(listDataChild.get(delFileToPath.get(filename)));
				}
			}
			removedFiles.clear();
			
			delete=false;
			this.invalidateOptionsMenu();
			listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
			expListView.setAdapter(listAdapter);
			for (int i = 0; i < expandedHeaders.size(); i++)
			{
				expListView.expandGroup(expandedHeaders.get(i));
				if (i > listDataHeader.size())
				{
					break;
				}
			}
		}
	}
	
	//this provide a simple dialog to let users know that our search feature is still in development
	//this part of the code will be replace with a real implementation 
	public void searchDialog(){
		AlertDialog search =  new AlertDialog.Builder(this).create();
		search.setTitle(R.string.confirmation);
		search.setTitle(R.string.searchDev);
		search.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		search.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		search.show();
	}
	//this provide a dialog window for users to comfirm deletion
	public void confirmDeletion()
	{
		AlertDialog confirmation =  new AlertDialog.Builder(this).create();
		//setting the dialog properties
		confirmation.setTitle(R.string.confirmation);
		confirmation.setTitle(R.string.confirm_message);
		confirmation.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
	    		deleteTheSelectedFile();


				dialog.dismiss();
			}
		});
		confirmation.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		confirmation.show();
	}
	
	//This method recursively finds the require files and delete them from the view
	public void deleteTheSelectedFile(){
		removedFiles = new ArrayList<String>();

		//this is taken from Charles's idea of getting the file name
		for (String dirName: listDataHeader){
			if (!selectedFiles.entrySet().isEmpty())
			{
	    		for(String fileName: selectedFiles.keySet()){
	    			if (listDataPath.get(fileName) == selectedFiles.get(fileName))
	    			{
	    				if(listDataChild.get(dirName).remove(fileName)){
	    					removedFiles.add(fileName);
	    					delFileToPath.put(fileName, dirName);
	    				}
	    			}  
	    		}
			}
		}
		
		for (String removed: removedFiles)
		{
			selectedFiles.remove(removed);
		}
		
		Toast.makeText(getApplicationContext(), "File(s) Deleted", Toast.LENGTH_SHORT).show();
		delete = true;
		numberofCheck=0;
		Box=false;
		
		invalidateOptionsMenu();
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		expListView.setAdapter(listAdapter);
		for (int i = 0; i < expandedHeaders.size(); i++)
		{
			expListView.expandGroup(expandedHeaders.get(i));
			if (i >= listDataHeader.size())
				break;
		}
	}
	
	//a function that use to set the value of the Box
	//this will make either make some of the checkbox visible or make some of the buttons hidden
	@Override
	public void checkBoxStatus(boolean value) {
		// TODO Auto-generated method stub
		Box=value;	
	}
	
	//This is a onclick function that the checkbox use for varies tasks
	public void checkbox(View v){
		//initialize attributes
		CheckBox myCheckBox= (CheckBox)v;
		String fileName = myCheckBox.getText().toString();
		//when clicked and the checkbox is checked
		if(myCheckBox.isChecked()){
			numberofCheck++;
			selectedFiles.put(fileName, listDataPath.get(fileName));
		}
		//when clicked and checkbox is not checked
		else{
			numberofCheck--;
			selectedFiles.remove(fileName);
		}
		//when the number of checked checkbox is greater than 0
		if(numberofCheck>0){
			Box=true;
			this.invalidateOptionsMenu(); 
		}
		//when the number of checked checkbox is not greater than 0
		//this will hide some of the buttons
		else{
			Box=false;
			this.invalidateOptionsMenu();
		}
	}
}
