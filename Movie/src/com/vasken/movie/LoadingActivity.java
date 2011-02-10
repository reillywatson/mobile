package com.vasken.movie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.vasken.movie.manager.DataManager;
import com.vasken.movie.manager.LoadListener;

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
					showQuestionActivity(LoadingActivity.this);
				}
			});
			theManager.loadCatalog();
		} else {
			Log.d(this.getClass().toString(), "The catalog was found locally");
			showQuestionActivity(LoadingActivity.this);
		}
		
	}

	protected void showQuestionActivity(LoadingActivity context) {
		Intent intent = new Intent(context, Trivia.class);
		context.startActivity(intent);
		finish();
	}
}
