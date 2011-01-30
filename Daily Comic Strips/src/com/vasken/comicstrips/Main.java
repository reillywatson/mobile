package com.vasken.comicstrips;

import java.util.ArrayList;
import java.util.Collections;

import com.vasken.comicstrips.R;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends ListActivity {
	
	static ArrayList<ComicInfo> comics;
	static ArrayList<ComicInfo> favourites = new ArrayList<ComicInfo>();
	SeparatedListAdapter adapter;
	
	Dialog theAboutDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Main rotatedSelf = (Main)getLastNonConfigurationInstance();
		if (rotatedSelf == null) {
	    	comics = ComicList.comicList();
	    	if (comics.isEmpty()) {
	    		ComicList.init(this);
	    		comics = ComicList.comicList();
	    	}
	    	Log.d("NUM COMICS", Integer.toString(comics.size()));
	    	favourites = extractFavourites(comics);
		}
		adapter = new SeparatedListAdapter(this, R.layout.list_header);
		adapter.addSection("Favorites", new ArrayAdapter<ComicInfo>(this, android.R.layout.simple_list_item_1, favourites));
		adapter.addSection("Comics", new ArrayAdapter<ComicInfo>(this, android.R.layout.simple_list_item_1, comics));
    	setListAdapter(adapter);

    	prepareAboutDialog();
    	
        setContentView(R.layout.main);
        getListView().setTextFilterEnabled(true);
        registerForContextMenu(getListView());
    }
    
    /******* DO PREPARATION WORK *******/
    /***********************************/
    
	private void prepareAboutDialog() {
		theAboutDialog = new Dialog(this);
		theAboutDialog.setContentView(R.layout.about_dialog);
		theAboutDialog.setTitle("About");
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return this;
	}
	
	void saveFavourites() {
		StringBuilder favstr = new StringBuilder();
		for (ComicInfo info : favourites) {
			favstr.append(info.name);
			favstr.append(",");
		}
		String prefstr = "";
		if (favstr.length() > 0)
			prefstr = favstr.substring(0, favstr.length() - 1);
		Log.d("SAVING FAVORITES", prefstr);
		getPreferences(Context.MODE_PRIVATE).edit().putString("favorites", prefstr).commit();
	}
    
    private ArrayList<ComicInfo> extractFavourites(ArrayList<ComicInfo> comics) {
    	ArrayList<ComicInfo> list = new ArrayList<ComicInfo>();
    	String favoritesList = getPreferences(Context.MODE_PRIVATE).getString("favorites", null);
    	if (favoritesList != null) {
    		Log.d("FAVORITES LIST", favoritesList);
    		String[] favorites = favoritesList.split(",");
    		for(String favorite : favorites) {
    			ComicInfo match = infoForComic(favorite);
    			if (match != null) {
    				list.add(match);
    			}
    		}
    	}
    	Collections.sort(list);
    	return list;
    }
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add("About");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("About")) {
			theAboutDialog.show();
		}
		return true;
	}
    
	public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		Log.d("CONTEXT MENU", menu.toString());
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		View listItem = info.targetView;
		TextView tv = (TextView)listItem.findViewById(android.R.id.text1);
		if (tv == null)
			return;
		Log.d("ITEM", tv.getText().toString());
		boolean isFavourite = false;
		for (ComicInfo comicInfo : favourites) {
			if (comicInfo.name.equals(tv.getText().toString())) {
				isFavourite = true;
				break;
			}
		}
		if (isFavourite) {
			menu.add("Remove from favorites");			
		}
		else {
			menu.add("Add to favorites");
		}
		menu.add("Cancel");
	}
	
	ComicInfo infoForComic(String name) {
		for (ComicInfo comicInfo : comics) {
			if (comicInfo.name.equals(name)) {
				return comicInfo;
			}
		}
		return null;
	}
	
	public boolean onContextItemSelected (MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		View listItem = info.targetView;
		TextView tv = (TextView)listItem.findViewById(android.R.id.text1);
		if (tv == null)
			return false;
		String comicName = tv.getText().toString();
		ComicInfo comicInfo = infoForComic(comicName);
		if (comicInfo != null) {
			Log.d("COMIC", comicInfo.toString());
			if (item.getTitle().equals("Add to favorites")) {
				favourites.add(comicInfo);
				Collections.sort(favourites);
			} else if (item.getTitle().equals("Remove from favorites")) {
				favourites.remove(comicInfo);
			} else if (item.getTitle().equals("About")) {
				theAboutDialog.show();
			}
			saveFavourites();
			adapter.notifyDataSetChanged();
		}
		return true;
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
