package com.vasken.comics;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.vasken.comics.Downloaders.ASofterWorldDownloader;
import com.vasken.comics.Downloaders.AchewoodDownloader;
import com.vasken.comics.Downloaders.ArcaMaxDownloader;
import com.vasken.comics.Downloaders.AwkwardZombieDownloader;
import com.vasken.comics.Downloaders.ComicsDotComDownloader;
import com.vasken.comics.Downloaders.DilbertDownloader;
import com.vasken.comics.Downloaders.DinosaurComicsDownloader;
import com.vasken.comics.Downloaders.Downloader;
import com.vasken.comics.Downloaders.GoComicsDownloader;
import com.vasken.comics.Downloaders.PennyArcadeDownloader;
import com.vasken.comics.Downloaders.PvPDownloader;
import com.vasken.comics.Downloaders.SharingMachineDownloader;
import com.vasken.comics.Downloaders.ShermansLagoonDownloader;
import com.vasken.comics.Downloaders.SinfestDownloader;
import com.vasken.comics.Downloaders.XKCDDownloader;
import com.vasken.util.StringUtils;
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
    	Callable<Downloader> downloaderConstructor;
    	public ComicInfo(String name, String startUrl, Callable<Downloader> constructor) {
    		this.name = name; this.startUrl = startUrl; this.downloaderConstructor = constructor;
    	}
    }
    ArrayList<ComicInfo> comics = new ArrayList<ComicInfo>();
	Downloader currentDownloader;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	comics.add(new ComicInfo("Sinfest", "http://www.sinfest.net/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SinfestDownloader(); }}));
    	comics.add(new ComicInfo("Zits", "http://www.arcamax.com/zits/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Beetle Bailey", "http://www.arcamax.com/beetlebailey/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Blondie", "http://www.arcamax.com/blondie/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Hagar the Horrible", "http://www.arcamax.com/hagarthehorrible/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Family Circus", "http://www.arcamax.com/familycircus/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Mother Goose & Grimm", "http://www.arcamax.com/mothergooseandgrimm/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Garfield Minus Garfield", "http://www.gocomics.com/garfieldminusgarfield/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("A Softer World", "http://www.asofterworld.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ASofterWorldDownloader(); }}));
    	comics.add(new ComicInfo("XKCD", "http://xkcd.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new XKCDDownloader(); }}));
    	comics.add(new ComicInfo("Natalie Dee", "http://www.nataliedee.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.nataliedee.com"); }}));
    	comics.add(new ComicInfo("Married To The Sea", "http://www.marriedtothesea.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.marriedtothesea.com"); }}));
    	comics.add(new ComicInfo("Peanuts", "http://www.comics.com/peanuts/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Marmaduke", "http://www.comics.com/marmaduke/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Sherman's Lagoon", "http://www.slagoon.com/cgi-bin/sviewer.pl", new Callable<Downloader>(){public Downloader call() throws Exception { return new ShermansLagoonDownloader(); }}));
    	comics.add(new ComicInfo("Toothpaste For Dinner", "http://www.toothpastefordinner.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.toothpastefordinner.com"); }}));
    	comics.add(new ComicInfo("Achewood", "http://www.achewood.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new AchewoodDownloader(); }}));
    	comics.add(new ComicInfo("Non Sequitur", "http://www.gocomics.com/nonsequitur/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Pickles", "http://www.gocomics.com/pickles/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Calvin and Hobbes", "http://www.gocomics.com/calvinandhobbes/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Dilbert", "http://www.dilbert.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new DilbertDownloader(); }}));
    	comics.add(new ComicInfo("FoxTrot", "http://www.gocomics.com/foxtrot/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("BC", "http://www.gocomics.com/bc/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Shoe", "http://www.gocomics.com/shoe/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Garfield", "http://www.gocomics.com/garfield/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Wizard of Id", "http://www.gocomics.com/wizardofid/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("For Better or For Worse", "http://www.gocomics.com/forbetterorforworse/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Bloom County", "http://www.gocomics.com/bloomcounty/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Penny Arcade", "http://www.penny-arcade.com/comic/", new Callable<Downloader>(){public Downloader call() throws Exception { return new PennyArcadeDownloader(); }}));
    	comics.add(new ComicInfo("PvP", "http://www.pvponline.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new PvPDownloader(); }}));
    	comics.add(new ComicInfo("Dinosaur Comics", "http://www.qwantz.com/index.php", new Callable<Downloader>(){public Downloader call() throws Exception { return new DinosaurComicsDownloader(); }}));
    	comics.add(new ComicInfo("Adam@Home", "http://www.gocomics.com/adamathome/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Andy Capp", "http://www.gocomics.com/andycapp/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Broom Hilda", "http://www.gocomics.com/broomhilda/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Daddy's Home", "http://www.gocomics.com/daddyshome/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Dick Tracy", "http://www.gocomics.com/dicktracy/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Doonesbury", "http://www.gocomics.com/doonesbury/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Frank & Ernest", "http://www.gocomics.com/shoe/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Lio", "http://www.gocomics.com/lio/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Heathcliff", "http://www.gocomics.com/heathcliff/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Overboard", "http://www.gocomics.com/overboard/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Real Life Adventures", "http://www.gocomics.com/reallifeadventures/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Red and Rover", "http://www.gocomics.com/redandrover/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Stone Soup", "http://www.gocomics.com/stonesoup/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Ziggy", "http://www.gocomics.com/ziggy/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Awkward Zombie", "http://www.awkwardzombie.com/comic1.php", new Callable<Downloader>(){public Downloader call() throws Exception { return new AwkwardZombieDownloader(); }}));
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        selectComic(comics.get(0));
    }
    
    public void selectComic(ComicInfo info) {
    	try {
			currentDownloader = info.downloaderConstructor.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	    		ImageView imgView = (ImageView)Main.this.findViewById(R.id.ImageView01);
	    		imgView.setVisibility((comic.image != null) ? View.VISIBLE : View.GONE);
	    		if (comic.image != null) {
		    		imgView.setImageBitmap(comic.image);
		    		imgView.setLongClickable(comic.altText != null);
    			}
	    		TextView alt = (TextView)Main.this.findViewById(R.id.alt_text);
	    		alt.setVisibility((comic.altText != null) ? View.VISIBLE : View.GONE);
	    		if (comic.altText != null) {
	    			alt.setText(StringUtils.unescapeHtml(comic.altText));
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
	    			tv.setText(StringUtils.unescapeHtml(comic.title));
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
