package com.droidz.homemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.droidz.homemanager.R;

public class HomeManagerList extends Activity {
    @Override
    protected void onStart() {
    	super.onStart();
    	HomeManager homeManager = new HomeManager(this);
    	
	    HomeManagerListAdapter adapter = new HomeManagerListAdapter(this,
	        	R.layout.row, homeManager.getLaunchers(), homeManager);
	        
	    ListView listView = (ListView)findViewById(R.id.homemanager_list);
	    listView.setAdapter(adapter);
	    
	    if(homeManager.getNumberOfLaunchersInstalled() > 1) {
		    if(!homeManager.defaultFound) {
		    	Intent launchHome = new Intent(Intent.ACTION_MAIN);
		    	launchHome.addCategory(Intent.CATEGORY_HOME);
		    	
		    	startActivity(launchHome);
		    }
	    }
	    else
	    	homeManager.showGetMoreLaunchersDialog();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_home_manager__list);
    }
}
