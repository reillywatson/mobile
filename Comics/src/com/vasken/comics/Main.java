package com.vasken.comics;

import java.util.ArrayList;

import com.vasken.comics.Downloaders.ASofterWorldDownloader;
import com.vasken.comics.Downloaders.DilbertDownloader;
import com.vasken.comics.Downloaders.DinosaurComicsDownloader;
import com.vasken.comics.Downloaders.Downloader;
import com.vasken.comics.Downloaders.GoComicsDownloader;
import com.vasken.comics.Downloaders.PennyArcadeDownloader;
import com.vasken.comics.Downloaders.PvPDownloader;
import com.vasken.comics.Downloaders.SharingMachineDownloader;
import com.vasken.comics.Downloaders.XKCDDownloader;
import com.vasken.util.UserTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity {
	
	class ComicInfo {
    	String name;
    	String startUrl;
    	Downloader downloader;
    	public ComicInfo(String name, String startUrl, Downloader downloader) {
    		this.name = name; this.startUrl = startUrl; this.downloader = downloader;
    	}
    }
    ArrayList<ComicInfo> comics = new ArrayList<ComicInfo>();
	Downloader currentDownloader;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    // TODO: find out why JPEG decoding is failing!
    //	comics.add(new ComicInfo("A Softer World", "http://www.asofterworld.com/", new ASofterWorldDownloader()));
    //	comics.add(new ComicInfo("Natalie Dee", "http://www.nataliedee.com/", new SharingMachineDownloader("http://www.nataliedee.com")));
    	comics.add(new ComicInfo("Married To The Sea", "http://www.marriedtothesea.com/", new SharingMachineDownloader("http://www.marriedtothesea.com")));
    	comics.add(new ComicInfo("Toothpaste For Dinner", "http://www.toothpastefordinner.com/", new SharingMachineDownloader("http://www.toothpastefordinner.com")));
    	comics.add(new ComicInfo("Calvin and Hobbes", "http://www.gocomics.com/calvinandhobbes/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("Dilbert", "http://www.dilbert.com/", new DilbertDownloader()));
    	comics.add(new ComicInfo("FoxTrot", "http://www.gocomics.com/foxtrot/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("BC", "http://www.gocomics.com/bc/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("Shoe", "http://www.gocomics.com/shoe/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("Garfield", "http://www.gocomics.com/garfield/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("Wizard of Id", "http://www.gocomics.com/wizardofid/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("For Better or For Worse", "http://www.gocomics.com/forbetterorforworse/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("Bloom County", "http://www.gocomics.com/bloomcounty/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("Non Sequitur", "http://www.gocomics.com/nonsequitur/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("Pickles", "http://www.gocomics.com/pickles/", new GoComicsDownloader()));
    	comics.add(new ComicInfo("Penny Arcade", "http://www.penny-arcade.com/comic/", new PennyArcadeDownloader()));
    	comics.add(new ComicInfo("XKCD", "http://xkcd.com/", new XKCDDownloader()));
    	comics.add(new ComicInfo("PvP", "http://www.pvponline.com/", new PvPDownloader()));
    	comics.add(new ComicInfo("Dinosaur Comics", "http://www.qwantz.com/index.php", new DinosaurComicsDownloader()));
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        selectComic(comics.get(0));
    }
    
    public void selectComic(ComicInfo info) {
    	currentDownloader = info.downloader;
		downloadComic(info.startUrl);
    }
    
    public void downloadComic(String url) {
    	currentDownloader.setUrl(url);
    	new DownloadTask().execute(currentDownloader);
    }

    class DownloadTask extends UserTask<Downloader, Void, Comic> {
    	@Override
    	public Comic doInBackground(Downloader... params) {
    		return params[0].getComic();
    	}
    	
    	@Override
    	public void onPostExecute(final Comic comic) {
    		if (comic != null) {
    			if (comic.image != null) {
		    		ImageView imgView = (ImageView)Main.this.findViewById(R.id.ImageView01);
		    		imgView.setImageBitmap(comic.image);
		    		imgView.setLongClickable(comic.altText != null);
		    		TextView alt = (TextView)Main.this.findViewById(R.id.alt_text);
		    		alt.setVisibility((comic.altText != null) ? View.VISIBLE : View.GONE);
		    		if (comic.altText != null) {
		    			alt.setText(comic.altText);
		    		}
		    		Button prev = (Button)Main.this.findViewById(R.id.prev_comic);
		    		Button next = (Button)Main.this.findViewById(R.id.next_comic);
		    		boolean enablePrev = comic.prevUrl != null && !comic.prevUrl.equals(comic.url);
		    		boolean enableNext = comic.nextUrl != null && !comic.nextUrl.equals(comic.url);
		    		prev.setEnabled(enablePrev);
		    		next.setEnabled(enableNext);
		    		if (enablePrev) {
		    			prev.setOnClickListener(new OnClickListener() {
		    				public void onClick(View arg0) {
								Main.this.downloadComic(comic.prevUrl);
							}});
		    		}
		    		if (enableNext) {
		    			next.setOnClickListener(new OnClickListener() {
		    				public void onClick(View arg0) {
								Main.this.downloadComic(comic.nextUrl);
							}});
		    		}
		    		if (comic.title != null) {
		    			TextView tv = (TextView)Main.this.findViewById(R.id.title);
		    			tv.setText(comic.title);
		    		}
	    		}
    		}
    	}
    }
    
    
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	for (ComicInfo info : comics) {
    		menu.add(0, info.name.hashCode(), 0, info.name);
    	}
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	for (ComicInfo info : comics) { 
    		if (item.getItemId() == info.name.hashCode()) {
    			selectComic(info);
    			return true;
    		}
    	}
        return false;
    }
}
