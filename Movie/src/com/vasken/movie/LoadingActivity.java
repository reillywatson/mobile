package com.vasken.movie;

import com.vasken.movie.manager.DataManager;
import com.vasken.movie.manager.LoadListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: setup UI
		
		// Get sqlite Catalog and move it
		final DataManager theManager = new DataManager(this);
		if (!theManager.databaseIsOkay()) {
			theManager.setOnDoneListener( new LoadListener() {
				@Override
				protected void onDone() {
					Log.d(this.getClass().toString(), "Finished downloading the catalog!");
					theManager.openDataBase();
					
					theManager.test();
				}
			});
			theManager.loadCatalog();
		} else {
			Log.d(this.getClass().toString(), "The catalog was found locally");
			theManager.openDataBase();
			theManager.test();
		}
		
		
	}
}
