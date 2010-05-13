package com.vasken.comics;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Main extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Object[] comics = ComicList.comicList().toArray();
    	ListAdapter adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, comics);
    	setListAdapter(adapter);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, Viewer.class);
    	intent.putExtra("com.vasken.comics.comicNo", position);
    	this.startActivity(intent);
    }
}
