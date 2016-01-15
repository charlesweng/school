package com.example.colormeprogrammatically;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LinearLayout myLayout = new LinearLayout(this);
		myLayout.setOrientation(0);
		@SuppressWarnings	("deprecation")
		LayoutParams myLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
		myLayout.setLayoutParams(myLayoutParams);
		myLayout.setWeightSum(3);
		
		Button redButton = new Button(this);
		redButton.setText(getResources().getString(R.string.Red));
		redButton.setBackgroundColor(getResources().getColor(R.color.Red));
		android.widget.LinearLayout.LayoutParams myButtonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
		redButton.setLayoutParams(myButtonParams);
		redButton.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		    	clickOnRed(v);
		    }
		});
		
		Button greenButton = new Button(this);
		greenButton.setText(getResources().getString(R.string.Green));
		greenButton.setBackgroundColor(getResources().getColor(R.color.Green));
		greenButton.setLayoutParams(myButtonParams);
		greenButton.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		    	clickOnGreen(v);
		    }
		});
		
		Button blueButton = new Button(this);
		blueButton.setText(getResources().getString(R.string.Blue));
		blueButton.setBackgroundColor(getResources().getColor(R.color.Blue));
		blueButton.setLayoutParams(myButtonParams);
		blueButton.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		    	clickOnBlue(v);
		    }
		});
		
		myLayout.addView(redButton);
		myLayout.addView(greenButton);
		myLayout.addView(blueButton);
		
		setContentView(myLayout);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    public void clickOnRed(View v)
    {
    	View view = this.getWindow().getDecorView();
		view.setBackgroundColor(getResources().getColor(R.color.Red));
    }
    public void clickOnGreen(View v)
    {
    	View view = this.getWindow().getDecorView();
		view.setBackgroundColor(getResources().getColor(R.color.Green));
    }
    public void clickOnBlue(View v)
    {
    	View view = this.getWindow().getDecorView();
		view.setBackgroundColor(getResources().getColor(R.color.Blue));
    }
}
