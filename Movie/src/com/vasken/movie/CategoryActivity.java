package com.vasken.movie;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.vasken.movie.manager.UpdateManager;

public class CategoryActivity extends Activity {
	public static final String CATEGORY = "CATEGORY";
	public static final String CATEGORY_ACTORS = "CATEGORY_ACTORS";
	public static final String CATEGORY_ACTRESSES = "CATEGORY_ACTRESSES";
	public static final String CATEGORY_DIRECTORS = "CATEGORY_DIRECTORS";
	public static final String CATEGORY_MOVIES = "CATEGORY_MOVIES";

	Dialog theLoadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set layout
		setContentView(R.layout.categories);
		
		// Set dialog
		theLoadingDialog = new Dialog(this);
		theLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		theLoadingDialog.setContentView(R.layout.loading_dialog);
		theLoadingDialog.setCancelable(false);
		theLoadingDialog.show();
		
		// Prepare sqlite catalog, when we have it, init buttons
		prepareDatabaseAndButtons();
	}
	
	private void prepareDatabaseAndButtons() {
		UpdateManager theManager = new UpdateManager(this);
		if (!theManager.databaseIsOkay()) {
			theManager.setOnDoneListener( theManager.new LoadListener() {
				@Override
				protected void onDone() {
					Log.d(this.getClass().toString(), "Finished downloading the catalog!");
					// Set a category on button click and load questions
					prepareButtons();
					theLoadingDialog.hide();
				}
			});
			theManager.loadCatalog(this);
		} else {
			Log.d(this.getClass().toString(), "The catalog was found locally");
			// Set a category on button click and load questions
			prepareButtons();
			theLoadingDialog.hide();
		}
	}

	private void prepareButtons() {
		Button actorsBtn = (Button)findViewById(R.id.categories_actors);
		actorsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showQuestionActivity(CategoryActivity.this, CategoryActivity.CATEGORY_ACTORS);
			}
		});
		
		Button actressesBtn = (Button)findViewById(R.id.categories_actresses);
		actressesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showQuestionActivity(CategoryActivity.this, CategoryActivity.CATEGORY_ACTRESSES);
			}
		});
		
		Button directorsBtn = (Button)findViewById(R.id.categories_directors);
		directorsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showQuestionActivity(CategoryActivity.this, CategoryActivity.CATEGORY_DIRECTORS);
			}
		});
		
		Button moviesBtn = (Button)findViewById(R.id.categories_movies);
		moviesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showQuestionActivity(CategoryActivity.this, CategoryActivity.CATEGORY_MOVIES);
			}
		});
	}

	protected void showQuestionActivity(CategoryActivity context, String value) {
		Intent intent = new Intent(context, Trivia.class);
		intent.putExtra(CategoryActivity.CATEGORY, value);
		context.startActivity(intent);
	}
}
