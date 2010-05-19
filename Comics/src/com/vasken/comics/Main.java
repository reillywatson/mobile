package com.vasken.comics;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends ListActivity {
	
	ArrayList<ComicInfo> comics;
	ArrayList<ComicInfo> favourites = new ArrayList<ComicInfo>();
	
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
//		adapter.addSection("Comics", new SimpleAdapter(this, security, R.layout.list_complex,
	//		new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
//    	ListAdapter adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, comics.toArray());
    	setListAdapter(adapter);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getListView().setTextFilterEnabled(true);
    }
    
    class ComicListAdapter extends BaseAdapter implements ListAdapter {

		@Override
		public int getCount() {
			return ComicList.comicList().size();
		}

		@Override
		public Object getItem(int position) {
			return ComicList.comicList().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				
				convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent);
			}
			TextView text = (TextView)convertView.findViewById(android.R.id.text1);
			text.setText(getItem(position).toString());
			return convertView;
		}
    
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
    				comics.remove(match);
    			}
    		}
    	}
    	return list;
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, Viewer.class);
    	intent.putExtra("com.vasken.comics.comicNo", position);
    	this.startActivity(intent);
    }
}
