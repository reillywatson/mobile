package com.vasken.comics;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Main extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	ArrayList<ComicInfo> comics = ComicList.comicList();
    	Log.d("NUM COMICS", Integer.toString(comics.size()));
    	moveFavoritesToFront(comics);
    	ListAdapter adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, comics.toArray());
    	setListAdapter(adapter);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    // TODO: we need to put these in a different section, which I suppose means making a ListAdapter class rather than
    // just using ArrayAdapter
    private void moveFavoritesToFront(ArrayList<ComicInfo> comics) {
    	String favoritesList = getPreferences(Context.MODE_PRIVATE).getString("favorites", null);
    	if (favoritesList != null) {
    		String[] favorites = favoritesList.split(",");
    		for(String favorite : favorites) {
    			ComicInfo match = null;
    			for (ComicInfo info : comics) {
    				if (info.name.equals(favorite)) {
    					match = info;
    					break;
    				}
    			}
    			if (match != null) {
    				comics.remove(match);
    				comics.add(0, match);
    			}
    		}
    	}
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, Viewer.class);
    	intent.putExtra("com.vasken.comics.comicNo", position);
    	this.startActivity(intent);
    }
}
