package com.vasken.comics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

import com.vasken.comics.Downloaders.ASofterWorldDownloader;
import com.vasken.comics.Downloaders.AchewoodDownloader;
import com.vasken.comics.Downloaders.AmazingSuperPowersDownloader;
import com.vasken.comics.Downloaders.ArcaMaxDownloader;
import com.vasken.comics.Downloaders.AwkwardZombieDownloader;
import com.vasken.comics.Downloaders.BookOfBiffDownloader;
import com.vasken.comics.Downloaders.ButtersafeDownloader;
import com.vasken.comics.Downloaders.CapesNBabesDownloader;
import com.vasken.comics.Downloaders.ComicsDotComDownloader;
import com.vasken.comics.Downloaders.CreatorsDotComDownloader;
import com.vasken.comics.Downloaders.CtrlAltDelDownloader;
import com.vasken.comics.Downloaders.CyanideAndHappinessDownloader;
import com.vasken.comics.Downloaders.DilbertDownloader;
import com.vasken.comics.Downloaders.DinosaurComicsDownloader;
import com.vasken.comics.Downloaders.Downloader;
import com.vasken.comics.Downloaders.GirlsWithSlingshotsDownloader;
import com.vasken.comics.Downloaders.GoComicsDownloader;
import com.vasken.comics.Downloaders.LeastICouldDoDownloader;
import com.vasken.comics.Downloaders.LookingForGroupDownloader;
import com.vasken.comics.Downloaders.NedroidDownloader;
import com.vasken.comics.Downloaders.PennyArcadeDownloader;
import com.vasken.comics.Downloaders.PerryBibleFellowshipDownloader;
import com.vasken.comics.Downloaders.PvPDownloader;
import com.vasken.comics.Downloaders.SharingMachineDownloader;
import com.vasken.comics.Downloaders.SharkAttackDownloader;
import com.vasken.comics.Downloaders.ShermansLagoonDownloader;
import com.vasken.comics.Downloaders.SinfestDownloader;
import com.vasken.comics.Downloaders.UserFriendlyDownloader;
import com.vasken.comics.Downloaders.VGCatsDownloader;
import com.vasken.comics.Downloaders.SMBCDownloader;
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
    	comics.add(new ComicInfo("Shark Attack!", "http://www.sharkattackcomics.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharkAttackDownloader(); }}));
    	comics.add(new ComicInfo("The Book of Biff", "http://www.thebookofbiff.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new BookOfBiffDownloader(); }}));
    	comics.add(new ComicInfo("Nedroid", "http://nedroid.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new NedroidDownloader(); }}));
    	comics.add(new ComicInfo("Capes -N- Babes", "http://www.capesnbabes.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new CapesNBabesDownloader(); }}));
    	comics.add(new ComicInfo("Girls with Slingshots", "http://www.gwscomic.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new GirlsWithSlingshotsDownloader(); }}));
    	comics.add(new ComicInfo("AmazingSuperPowers", "http://www.amazingsuperpowers.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new AmazingSuperPowersDownloader(); }}));
    	comics.add(new ComicInfo("Saturday Morning Breakfast Cereal", "http://www.smbc-comics.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SMBCDownloader(); }}));
       	comics.add(new ComicInfo("Buttersafe", "http://buttersafe.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ButtersafeDownloader(); }}));
    	
    	
    	// creators.com comics
       	comics.add(new ComicInfo("Archie", "http://www.creators.com/comics/archie.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Ask Shagg", "http://www.creators.com/comics/ask-shagg.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Flare", "http://www.creators.com/comics/flare.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("For Heaven's Sake", "http://www.creators.com/comics/for-heavens-sake.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("The Quigmans", "http://www.creators.com/comics/the-quigmans.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Rugrats", "http://www.creators.com/comics/rugrats.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("State of the Union", "http://www.creators.com/comics/state-of-the-union.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Thin Lines", "http://www.creators.com/comics/thin-lines.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Wee Pals", "http://www.creators.com/comics/wee-pals.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));
       	comics.add(new ComicInfo("Winnie the Pooh", "http://www.creators.com/comics/winnie-the-pooh.html", new Callable<Downloader>(){public Downloader call() throws Exception { return new CreatorsDotComDownloader(); }}));

       	// arcamax comics
    	comics.add(new ComicInfo("Baby Blues", "http://www.arcamax.com/babyblues/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Beetle Bailey", "http://www.arcamax.com/beetlebailey/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Blondie", "http://www.arcamax.com/blondie/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("The Boondocks", "http://www.arcamax.com/boondocks/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Cathy", "http://www.arcamax.com/cathy/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Dustin", "http://www.arcamax.com/dustin/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Family Circus", "http://www.arcamax.com/familycircus/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Hagar the Horrible", "http://www.arcamax.com/hagarthehorrible/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Jerry King Cartoons", "http://www.arcamax.com/humorcartoon/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Mother Goose & Grimm", "http://www.arcamax.com/mothergooseandgrimm/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Mutts", "http://www.arcamax.com/mutts/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	comics.add(new ComicInfo("Zits", "http://www.arcamax.com/zits/", new Callable<Downloader>(){public Downloader call() throws Exception { return new ArcaMaxDownloader(); }}));
    	
    	// gocomics comics
    	comics.add(new ComicInfo("The Academia Waltz", "http://www.gocomics.com/academiawaltz", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Adam@Home", "http://www.gocomics.com/adamathome", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("The Argyle Sweater", "http://www.gocomics.com/theargylesweater", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Back in the Day", "http://www.gocomics.com/backintheday", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Bad Reporter", "http://www.gocomics.com/badreporter", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Baldo", "http://www.gocomics.com/baldo", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Barkeater Lake", "http://www.gocomics.com/barkeaterlake", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Basic Instructions", "http://www.gocomics.com/basicinstructions", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Bewley", "http://www.gocomics.com/bewley", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Big Top", "http://www.gocomics.com/bigtop", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Biographic", "http://www.gocomics.com/biographic", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Birdbrains", "http://www.gocomics.com/birdbrains", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Bleeker: The Rechargeable Dog", "http://www.gocomics.com/bleeker", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Bloom County", "http://www.gocomics.com/bloomcounty", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Bo Nanas", "http://www.gocomics.com/bonanas", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Bob the Squirrel", "http://www.gocomics.com/bobthesquirrel", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("The Boiling Point", "http://www.gocomics.com/theboilingpoint", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Boomerangs", "http://www.gocomics.com/boomerangs", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Brainwaves", "http://www.gocomics.com/brainwaves", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Calvin and Hobbes", "http://www.gocomics.com/calvinandhobbes", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("C'est la Vie", "http://www.gocomics.com/cestlavie", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Citizen Dog", "http://www.gocomics.com/citizendog", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("The City", "http://www.gocomics.com/thecity", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Cleats", "http://www.gocomics.com/cleats", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Close to Home", "http://www.gocomics.com/closetohome", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Compu-toon", "http://www.gocomics.com/compu-toon", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Cornered", "http://www.gocomics.com/cornered", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Cul de Sac", "http://www.gocomics.com/culdesac", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Deep Cover", "http://www.gocomics.com/deepcover", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Domestic Abuse", "http://www.gocomics.com/domesticabuse", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Doodles", "http://www.gocomics.com/doodles", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Doonesbury", "http://www.gocomics.com/doonesbury", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("The Doozies", "http://www.gocomics.com/thedoozies", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("The Duplex", "http://www.gocomics.com/duplex", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Eek!", "http://www.gocomics.com/eek", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("The Elderberries", "http://www.gocomics.com/theelderberries", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("The Flying McCoys", "http://www.gocomics.com/theflyingmccoys", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("For Better or For Worse", "http://www.gocomics.com/forbetterorforworse", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("FoxTrot", "http://www.gocomics.com/foxtrot", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("FoxTrot Classics", "http://www.gocomics.com/foxtrotclassics", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Fred Basset", "http://www.gocomics.com/fredbasset", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Frog Applause", "http://www.gocomics.com/frogapplause", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("The Fusco Brothers", "http://www.gocomics.com/thefuscobrothers", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Garfield", "http://www.gocomics.com/garfield", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Garfield Minus Garfield", "http://www.gocomics.com/garfieldminusgarfield", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("GET A LIFE!", "http://www.gocomics.com/getalife", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Ginger Meggs", "http://www.gocomics.com/gingermeggs", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Haiku Ewe", "http://www.gocomics.com/haikuewe", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Heart of the City", "http://www.gocomics.com/heartofthecity", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Housebroken", "http://www.gocomics.com/housebroken", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Hubert and Abby", "http://www.gocomics.com/hubertandabby", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Imagine This", "http://www.gocomics.com/imaginethis", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("In the Bleachers", "http://www.gocomics.com/inthebleachers", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("In the Sticks", "http://www.gocomics.com/inthesticks", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Ink Pen", "http://www.gocomics.com/inkpen", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Joe Vanilla", "http://www.gocomics.com/joevanilla", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("La Cucaracha", "http://www.gocomics.com/lacucaracha", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Last Kiss", "http://www.gocomics.com/lastkiss", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Legend of Bill", "http://www.gocomics.com/legendofbill", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Lio", "http://www.gocomics.com/lio", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Little Otto", "http://www.gocomics.com/littleotto", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Maintaining", "http://www.gocomics.com/maintaining", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Mal and Chad", "http://www.gocomics.com/malandchad", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Mutt & Jeff", "http://www.gocomics.com/muttandjeff", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Mythtickle", "http://www.gocomics.com/mythtickle", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("NEUROTICA", "http://www.gocomics.com/neurotica", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("New Adventures of Queen Victoria", "http://www.gocomics.com/thenewadventuresofqueenvictoria", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Non Sequitur", "http://www.gocomics.com/nonsequitur", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("The Norm", "http://www.gocomics.com/thenorm", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Out of the Gene Pool Re-Runs", "http://www.gocomics.com/outofthegenepool", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Overboard", "http://www.gocomics.com/overboard", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Pibgorn", "http://www.gocomics.com/pibgorn", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Pibgorn Sketches", "http://www.gocomics.com/pibgornsketches", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Pinkerton", "http://www.gocomics.com/pinkerton", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Pooch Cafe", "http://www.gocomics.com/poochcafe", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("PreTeena", "http://www.gocomics.com/preteena", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Rabbits Against Magic", "http://www.gocomics.com/rabbitsagainstmagic", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Real Life Adventures", "http://www.gocomics.com/reallifeadventures", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Red Meat", "http://www.gocomics.com/redmeat", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Reynolds Unwrapped", "http://www.gocomics.com/reynoldsunwrapped", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Ronaldinho Gaucho", "http://www.gocomics.com/ronaldinhogaucho", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Shoe", "http://www.gocomics.com/shoe", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Shoecabbage", "http://www.gocomics.com/shoecabbage", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Skin Horse", "http://www.gocomics.com/skinhorse", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Slowpoke", "http://www.gocomics.com/slowpoke", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Stone Soup", "http://www.gocomics.com/stonesoup", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Tank McNamara", "http://www.gocomics.com/tankmcnamara", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Tiny Sepuku", "http://www.gocomics.com/tinysepuku", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("TOBY", "http://www.gocomics.com/toby", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Tom the Dancing Bug", "http://www.gocomics.com/tomthedancingbug", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Too Much Coffee Man", "http://www.gocomics.com/toomuchcoffeeman", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("W.T. Duck", "http://www.gocomics.com/wtduck", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	comics.add(new ComicInfo("Yenny", "http://www.gocomics.com/yenny", new Callable<Downloader>(){public Downloader call() throws Exception { return new GoComicsDownloader(); }}));
    	
    	// comics.com comics
    	comics.add(new ComicInfo("9 Chickweed Lane", "http://www.comics.com/9_chickweed_lane", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("9 to 5", "http://www.comics.com/9_to_5", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Agnes", "http://www.comics.com/agnes", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Alley Oop", "http://www.comics.com/alley_oop", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Andy Capp", "http://www.comics.com/andy_capp", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Animal Crackers", "http://www.comics.com/animal_crackers", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Annie", "http://www.comics.com/annie", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Arlo & Janis", "http://www.comics.com/arlo&janis", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("B.C.", "http://www.comics.com/bc", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Ballard Street", "http://www.comics.com/ballard_street", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Ben", "http://www.comics.com/ben", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Betty", "http://www.comics.com/betty", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Big Nate", "http://www.comics.com/big_nate", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Bliss", "http://www.comics.com/bliss", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Bottom Liners", "http://www.comics.com/bottom_liners", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Bound and Gagged", "http://www.comics.com/bound_and_gagged", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Brenda Starr", "http://www.comics.com/brenda_starr", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Brevity", "http://www.comics.com/brevity", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Brewster Rockit", "http://www.comics.com/brewster_rockit", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Broom-Hilda", "http://www.comics.com/broom-hilda", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Cafe Con Leche", "http://www.comics.com/cafe_con_leche", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Candorville", "http://www.comics.com/candorville", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Cheap Thrills", "http://www.comics.com/cheap_thrills", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Chuckle Bros", "http://www.comics.com/chuckle_bros", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Committed", "http://www.comics.com/committed", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Cow & Boy", "http://www.comics.com/cow&boy", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Daddy's Home", "http://www.comics.com/daddys_home", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Dick Tracy", "http://www.comics.com/dick_tracy", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Dog eat Doug", "http://www.comics.com/dog_eat_doug", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Drabble", "http://www.comics.com/drabble", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("F Minus", "http://www.comics.com/f_minus", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Family Tree", "http://www.comics.com/family_tree", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Farcus", "http://www.comics.com/farcus", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Fat Cats Classics", "http://www.comics.com/fat_cats_classics", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Ferd'nand", "http://www.comics.com/ferdnand", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Flight Deck", "http://www.comics.com/flight_deck", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Flo & Friends", "http://www.comics.com/flo&friends", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Fort Knox", "http://www.comics.com/fort_knox", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Frank & Ernest", "http://www.comics.com/frank&ernest", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Frazz", "http://www.comics.com/frazz", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Free Range", "http://www.comics.com/free_range", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Gasoline Alley", "http://www.comics.com/gasoline_alley", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Geech Classics", "http://www.comics.com/geech_classics", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Get Fuzzy", "http://www.comics.com/get_fuzzy", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Gil Thorp", "http://www.comics.com/gil_thorp", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Girls & Sports", "http://www.comics.com/girls&sports", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Graffiti", "http://www.comics.com/graffiti", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Grand Avenue", "http://www.comics.com/grand_avenue", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Heathcliff", "http://www.comics.com/heathcliff", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Herb and Jamaal", "http://www.comics.com/herb_and_jamaal", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Herman", "http://www.comics.com/herman", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Home and Away", "http://www.comics.com/home_and_away", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("It's All About You", "http://www.comics.com/its_all_about_you", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Jane's World", "http://www.comics.com/janes_world", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Jump Start", "http://www.comics.com/jump_start", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Kit 'N' Carlyle", "http://www.comics.com/kit_n_carlyle", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Li'l Abner Classics", "http://www.comics.com/lil_abner_classics", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Liberty Meadows", "http://www.comics.com/liberty_meadows", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Little Dog Lost", "http://www.comics.com/little_dog_lost", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Lola", "http://www.comics.com/lola", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Loose Parts", "http://www.comics.com/loose_parts", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Love Is...", "http://www.comics.com/love_is", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Luann", "http://www.comics.com/luann", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Marmaduke", "http://www.comics.com/marmaduke", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Meg! Classics", "http://www.comics.com/meg_classics", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Minimum Security", "http://www.comics.com/minimum_security", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Moderately Confused", "http://www.comics.com/moderately_confused", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Momma", "http://www.comics.com/momma", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Monty", "http://www.comics.com/monty", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Motley Classics", "http://www.comics.com/motley_classics", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Nancy", "http://www.comics.com/nancy", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Natural Selection", "http://www.comics.com/natural_selection", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Nest Heads", "http://www.comics.com/nest_heads", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Off The Mark", "http://www.comics.com/off_the_mark", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("On a Claire Day", "http://www.comics.com/on_a_claire_day", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("One Big Happy Classics", "http://www.comics.com/one_big_happy_classics", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Over the Hedge", "http://www.comics.com/over_the_hedge", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("PC and Pixel", "http://www.comics.com/pc_and_pixel", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Peanuts", "http://www.comics.com/peanuts", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Pearls Before Swine", "http://www.comics.com/pearls_before_swine", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Pickles", "http://www.comics.com/pickles", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Pluggers", "http://www.comics.com/pluggers", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Prickly City", "http://www.comics.com/prickly_city", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Raising Duncan Classics", "http://www.comics.com/raising_duncan_classics", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Reality Check", "http://www.comics.com/reality_check", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Red & Rover", "http://www.comics.com/red&rover", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Rip Haywire", "http://www.comics.com/rip_haywire", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Ripley's Believe It or Not!", "http://www.comics.com/ripleys_believe_it_or_not", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Rose Is Rose", "http://www.comics.com/rose_is_rose", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Rubes", "http://www.comics.com/rubes", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Rudy Park", "http://www.comics.com/rudy_park", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Scary Gary", "http://www.comics.com/scary_gary", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Shirley and Son Classics", "http://www.comics.com/shirley_and_son_classics", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Soup To Nutz", "http://www.comics.com/soup_to_nutz", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Speed Bump", "http://www.comics.com/speed_bump", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Spot The Frog", "http://www.comics.com/spot_the_frog", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Strange Brew", "http://www.comics.com/strange_brew", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Sylvia", "http://www.comics.com/sylvia", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Tarzan Classics", "http://www.comics.com/tarzan_classics", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("That's Life", "http://www.comics.com/thats_life", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Barn", "http://www.comics.com/the_barn", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Born Loser", "http://www.comics.com/the_born_loser", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Buckets", "http://www.comics.com/the_buckets", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Dinette Set", "http://www.comics.com/the_dinette_set", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Grizzwells", "http://www.comics.com/the_grizzwells", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Humble Stumble", "http://www.comics.com/the_humble_stumble", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Knight Life", "http://www.comics.com/the_knight_life", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Meaning of Lila", "http://www.comics.com/the_meaning_of_lila", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Middletons", "http://www.comics.com/the_middeltons", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Other Coast", "http://www.comics.com/the_other_coast", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("The Sunshine Club", "http://www.comics.com/the_sunshine_club", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Unstrange Phenomena", "http://www.comics.com/unstrange_phenomena", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Watch Your Head", "http://www.comics.com/watch_your_head", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Wizard of Id", "http://www.comics.com/wizard_of_id", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Working Daze", "http://www.comics.com/working_daze", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Working It Out", "http://www.comics.com/working_it_out", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("Zack Hill", "http://www.comics.com/zack_hill", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));
    	comics.add(new ComicInfo("(Th)ink", "http://www.comics.com/think", new Callable<Downloader>(){public Downloader call() throws Exception { return new ComicsDotComDownloader(); }}));

    	
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
    	comics.add(new ComicInfo("The Perry Bible Fellowship", "http://pbfcomics.com/?cid=PBF247-Catch_Phrase.jpg", new Callable<Downloader>(){public Downloader call() throws Exception { return new PerryBibleFellowshipDownloader(); }}));
    	comics.add(new ComicInfo("Toothpaste For Dinner", "http://www.toothpastefordinner.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.toothpastefordinner.com"); }}));
    	comics.add(new ComicInfo("User Friendly", "http://ars.userfriendly.org/cartoons/?mode=classic", new Callable<Downloader>(){public Downloader call() throws Exception { return new UserFriendlyDownloader(); }}));
    	comics.add(new ComicInfo("VG Cats", "http://www.vgcats.com/comics/", new Callable<Downloader>(){public Downloader call() throws Exception { return new VGCatsDownloader(); }}));
    	comics.add(new ComicInfo("XKCD", "http://xkcd.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new XKCDDownloader(); }}));
    	
    	//Collections.sort(comics);
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
