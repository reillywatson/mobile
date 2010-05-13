package com.vasken.comics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

import com.vasken.comics.Downloaders.ASofterWorldDownloader;
import com.vasken.comics.Downloaders.AchewoodDownloader;
import com.vasken.comics.Downloaders.ArcaMaxDownloader;
import com.vasken.comics.Downloaders.AwkwardZombieDownloader;
import com.vasken.comics.Downloaders.ComicsDotComDownloader;
import com.vasken.comics.Downloaders.CreatorsDotComDownloader;
import com.vasken.comics.Downloaders.CtrlAltDelDownloader;
import com.vasken.comics.Downloaders.CyanideAndHappinessDownloader;
import com.vasken.comics.Downloaders.DilbertDownloader;
import com.vasken.comics.Downloaders.DinosaurComicsDownloader;
import com.vasken.comics.Downloaders.Downloader;
import com.vasken.comics.Downloaders.GoComicsDownloader;
import com.vasken.comics.Downloaders.LeastICouldDoDownloader;
import com.vasken.comics.Downloaders.LookingForGroupDownloader;
import com.vasken.comics.Downloaders.PennyArcadeDownloader;
import com.vasken.comics.Downloaders.PvPDownloader;
import com.vasken.comics.Downloaders.SharingMachineDownloader;
import com.vasken.comics.Downloaders.ShermansLagoonDownloader;
import com.vasken.comics.Downloaders.SinfestDownloader;
import com.vasken.comics.Downloaders.VGCatsDownloader;
import com.vasken.comics.Downloaders.XKCDDownloader;
import com.vasken.util.StringUtils;
import com.vasken.util.UserTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {
	
	class ComicInfo implements Comparable<ComicInfo> {
    	String name;
    	String startUrl;
    	Callable<Downloader> downloaderConstructor;
    	public ComicInfo(String name, String startUrl, Callable<Downloader> constructor) {
    		this.name = name; this.startUrl = startUrl; this.downloaderConstructor = constructor;
    	}
		@Override
		public int compareTo(ComicInfo another) {
			return name.compareTo(another.name);
		}
    }
    ArrayList<ComicInfo> comics = new ArrayList<ComicInfo>();
	Downloader currentDownloader;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// creators.com comics
       	comics.add(new ComicInfo("Agnes", "http://www.creators.com/comics/agnes.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Archie", "http://www.creators.com/comics/archie.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Ask Shagg", "http://www.creators.com/comics/ask-shagg.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Ballard Street", "http://www.creators.com/comics/ballard-street.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("The Barn", "http://www.creators.com/comics/the-barn.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Cafe con Leche", "http://www.creators.com/comics/cafe-con-leche.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Chuckle Bros", "http://www.creators.com/comics/chuckle-bros.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Daddy's Home", "http://www.creators.com/comics/daddys-home.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Dinette Set", "http://www.creators.com/comics/dinette-set.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Dog Eat Doug", "http://www.creators.com/comics/dog-eat-doug.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Flo and Friends", "http://www.creators.com/comics/flo-and-friends.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Flare", "http://www.creators.com/comics/flare.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Flight Deck", "http://www.creators.com/comics/flight-deck.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("For Heaven's Sake", "http://www.creators.com/comics/for-heavens-sake.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Free Range", "http://www.creators.com/comics/free-range.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Girls and Sports", "http://www.creators.com/comics/girls-and-sports.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Herb and Jamaal", "http://www.creators.com/comics/herb-and-jamaal.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Liberty Meadows", "http://www.creators.com/comics/liberty-meadows.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("The Meaning of Lila", "http://www.creators.com/comics/meaning-of-lila.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Momma", "http://www.creators.com/comics/momma.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Nest Heads", "http://www.creators.com/comics/nest-heads.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("One Big Happy", "http://www.creators.com/comics/one-big-happy.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("On a Claire Day", "http://www.creators.com/comics/on-a-claire-day.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("The Other Coast", "http://www.creators.com/comics/the-other-coast.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("The Quigmans", "http://www.creators.com/comics/the-quigmans.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Rubes", "http://www.creators.com/comics/rubes.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Rugrats", "http://www.creators.com/comics/rugrats.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Scary Gary", "http://www.creators.com/comics/scary-gary.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Speed Bump", "http://www.creators.com/comics/speed-bump.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("State of the Union", "http://www.creators.com/comics/state-of-the-union.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Strange Brew", "http://www.creators.com/comics/strange-brew.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Thin Lines", "http://www.creators.com/comics/thin-lines.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Wee Pals", "http://www.creators.com/comics/wee-pals.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Working it Out", "http://www.creators.com/comics/working-it-out.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));

    	// arcamax comics
    	comics.add(new ComicInfo("Baby Blues", "http://www.arcamax.com/babyblues/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Beetle Bailey", "http://www.arcamax.com/beetlebailey/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Blondie", "http://www.arcamax.com/blondie/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Boondocks", "http://www.arcamax.com/boondocks/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Cathy", "http://www.arcamax.com/cathy/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Dustin", "http://www.arcamax.com/dustin/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Family Circus", "http://www.arcamax.com/familycircus/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Hagar the Horrible", "http://www.arcamax.com/hagarthehorrible/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Jerry King Cartoons", "http://www.arcamax.com/humorcartoon/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Momma", "http://www.arcamax.com/momma/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Mother Goose & Grimm", "http://www.arcamax.com/mothergooseandgrimm/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Mutts", "http://www.arcamax.com/mutts/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Zits", "http://www.arcamax.com/zits/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	
    	// gocomics comics
    	comics.add(new ComicInfo("Adam@Home", "http://www.gocomics.com/adamathome/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Andy Capp", "http://www.gocomics.com/andycapp/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("BC", "http://www.gocomics.com/bc/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Bloom County", "http://www.gocomics.com/bloomcounty/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Broom Hilda", "http://www.gocomics.com/broomhilda/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Calvin and Hobbes", "http://www.gocomics.com/calvinandhobbes/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Daddy's Home", "http://www.gocomics.com/daddyshome/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Dick Tracy", "http://www.gocomics.com/dicktracy/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Doonesbury", "http://www.gocomics.com/doonesbury/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("For Better or For Worse", "http://www.gocomics.com/forbetterorforworse/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("FoxTrot", "http://www.gocomics.com/foxtrot/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Frank & Ernest", "http://www.gocomics.com/shoe/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Garfield", "http://www.gocomics.com/garfield/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Garfield Minus Garfield", "http://www.gocomics.com/garfieldminusgarfield/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Lio", "http://www.gocomics.com/lio/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Heathcliff", "http://www.gocomics.com/heathcliff/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Non Sequitur", "http://www.gocomics.com/nonsequitur/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Overboard", "http://www.gocomics.com/overboard/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Pickles", "http://www.gocomics.com/pickles/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Real Life Adventures", "http://www.gocomics.com/reallifeadventures/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Red and Rover", "http://www.gocomics.com/redandrover/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Shoe", "http://www.gocomics.com/shoe/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Stone Soup", "http://www.gocomics.com/stonesoup/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Wizard of Id", "http://www.gocomics.com/wizardofid/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Ziggy", "http://www.gocomics.com/ziggy/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	
    	// comics.com comics
    	comics.add(new ComicInfo("Marmaduke", "http://www.comics.com/marmaduke/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Peanuts", "http://www.comics.com/peanuts/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	
    	// web/misc comics
    	comics.add(new ComicInfo("A Softer World", "http://www.asofterworld.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ASofterWorldDownloader(); }}));
    	comics.add(new ComicInfo("Achewood", "http://www.achewood.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new AchewoodDownloader(); }}));
    	comics.add(new ComicInfo("Awkward Zombie", "http://www.awkwardzombie.com/comic1.php", new Callable<Downloader>(){public Downloader call() throws Exception { return new AwkwardZombieDownloader(); }}));
    	comics.add(new ComicInfo("Ctrl+Alt+Del", "http://www.cad-comic.com/cad/", new Callable<Downloader>(){public Downloader call() throws Exception { return new CtrlAltDelDownloader(); }}));
    	comics.add(new ComicInfo("Ctrl+Alt+Del Sillies", "http://www.cad-comic.com/sillies/", new Callable<Downloader>(){public Downloader call() throws Exception { return new CtrlAltDelDownloader(); }}));
    	comics.add(new ComicInfo("Cyanide and Happiness", "http://www.explosm.net/comics/", new Callable<Downloader>(){public Downloader call() throws Exception { return new CyanideAndHappinessDownloader(); }}));
    	comics.add(new ComicInfo("Dilbert", "http://www.dilbert.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new DilbertDownloader(); }}));
    	comics.add(new ComicInfo("Dinosaur Comics", "http://www.qwantz.com/index.php", new Callable<Downloader>(){public Downloader call() throws Exception { return new DinosaurComicsDownloader(); }}));
    	comics.add(new ComicInfo("Looking For Group", "http://www.lfgcomic.com/page/latest", new Callable<Downloader>(){public Downloader call() throws Exception { return new LookingForGroupDownloader(); }}));
    	comics.add(new ComicInfo("Married To The Sea", "http://www.marriedtothesea.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.marriedtothesea.com"); }}));
    	comics.add(new ComicInfo("Natalie Dee", "http://www.nataliedee.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.nataliedee.com"); }}));
    	comics.add(new ComicInfo("Penny Arcade", "http://www.penny-arcade.com/comic/", new Callable<Downloader>(){public Downloader call() throws Exception { return new PennyArcadeDownloader(); }}));
    	comics.add(new ComicInfo("PvP", "http://www.pvponline.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new PvPDownloader(); }}));
    	comics.add(new ComicInfo("Sherman's Lagoon", "http://www.slagoon.com/cgi-bin/sviewer.pl", new Callable<Downloader>(){public Downloader call() throws Exception { return new ShermansLagoonDownloader(); }}));
    	comics.add(new ComicInfo("Sinfest", "http://www.sinfest.net/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SinfestDownloader(); }}));
    	comics.add(new ComicInfo("The Least I Could Do", "http://leasticoulddo.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new LeastICouldDoDownloader(); }}));
    	comics.add(new ComicInfo("Toothpaste For Dinner", "http://www.toothpastefordinner.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.toothpastefordinner.com"); }}));
    	comics.add(new ComicInfo("VG Cats", "http://www.vgcats.com/comics/", new Callable<Downloader>(){public Downloader call() throws Exception { return new VGCatsDownloader(); }}));
    	comics.add(new ComicInfo("XKCD", "http://xkcd.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new XKCDDownloader(); }}));
    	
    	Collections.sort(comics);
    	Log.d("NUMBER OF COMICS", Integer.toString(comics.size()));
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
    			WebView webView = (WebView)Main.this.findViewById(R.id.WebView);
	    		webView.setVisibility((comic.image != null) ? View.VISIBLE : View.GONE);
	    		if (comic.image != null) {
	    			webView.loadUrl(comic.image);
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
    			TextView tv = (TextView)Main.this.findViewById(R.id.title);
	    		if (comic.title != null) {
	    			tv.setText(StringUtils.unescapeHtml(comic.title));
	    		} else {
	    			tv.setText("");
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
