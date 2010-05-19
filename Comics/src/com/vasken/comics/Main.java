package com.vasken.comics;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends ListActivity {
	
	static ArrayList<ComicInfo> comics;
	static ArrayList<ComicInfo> favourites = new ArrayList<ComicInfo>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	comics = ComicList.comicList();
    	if (comics.isEmpty()) {
    		ComicList.init(this);
    		comics = ComicList.comicList();
    	}
    	Log.d("NUM COMICS", Integer.toString(comics.size()));
    	favourites = extractFavourites(comics);

		SeparatedListAdapter adapter = new SeparatedListAdapter(this, R.layout.list_header);
		adapter.addSection("Favorites", new ArrayAdapter<Object>(this,
				android.R.layout.simple_list_item_1, favourites.toArray()));
		adapter.addSection("Comics", new ArrayAdapter<Object>(this,
				android.R.layout.simple_list_item_1, comics.toArray()));
    	setListAdapter(adapter);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getListView().setTextFilterEnabled(true);
    }
    
    private ArrayList<ComicInfo> extractFavourites(ArrayList<ComicInfo> comics) {
    	ArrayList<ComicInfo> list = new ArrayList<ComicInfo>();
    	String favoritesList = getPreferences(Context.MODE_PRIVATE).getString("favorites", null);
    	favoritesList = "XKCD,Dinosaur Comics,A Softer World,Toothpaste For Dinner,FoxTrot Classics";
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
    				list.add(match);
    			}
    		}
    	}
    	return list;
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	position--;
    	if (position >= favourites.size())
    		position--;
    	Intent intent = new Intent(this, Viewer.class);
    	intent.putExtra("com.vasken.comics.comicNo", position);
    	this.startActivity(intent);
    }
}
