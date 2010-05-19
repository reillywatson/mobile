package com.vasken.comics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

import android.content.Context;

import com.vasken.comics.Downloaders.Downloader;
import com.vasken.comics.Downloaders.SharingMachineDownloader;

public class ComicList {
	
	static ArrayList<ComicInfo> comics = new ArrayList<ComicInfo>();

	static ArrayList<ComicInfo> comicList() { return comics; }
	
	static void init(Context context) {
		comics.add(new ComicInfo(context, R.raw.achewood));
		comics.add(new ComicInfo(context, R.raw.amazingsuperpowers));
		comics.add(new ComicInfo(context, R.raw.asofterworld));
		comics.add(new ComicInfo(context, R.raw.awkwardzombie));
		comics.add(new ComicInfo(context, R.raw.bookofbiff));
		comics.add(new ComicInfo(context, R.raw.buttersafe));
		comics.add(new ComicInfo(context, R.raw.capesnbabes));
		comics.add(new ComicInfo(context, R.raw.ctrlaltdel));
		comics.add(new ComicInfo(context, R.raw.ctrlaltdelsillies));
		comics.add(new ComicInfo(context, R.raw.cyanideandhappiness));
		comics.add(new ComicInfo(context, R.raw.dilbert));
		comics.add(new ComicInfo(context, R.raw.dinosaurcomics));
		comics.add(new ComicInfo(context, R.raw.girlgenius));
		comics.add(new ComicInfo(context, R.raw.girlswithslingshots));
		comics.add(new ComicInfo(context, R.raw.leasticoulddo));
		comics.add(new ComicInfo(context, R.raw.lookingforgroup));
		comics.add(new ComicInfo(context, R.raw.megatokyo));
		comics.add(new ComicInfo(context, R.raw.nedroid));
		comics.add(new ComicInfo(context, R.raw.pennyarcade));
		comics.add(new ComicInfo(context, R.raw.perrybiblefellowship));
		comics.add(new ComicInfo(context, R.raw.pvp));
		comics.add(new ComicInfo(context, R.raw.questionablecontent));
		comics.add(new ComicInfo(context, R.raw.sharkattack));
		comics.add(new ComicInfo(context, R.raw.shermanslagoon));
		comics.add(new ComicInfo(context, R.raw.sinfest));
		comics.add(new ComicInfo(context, R.raw.smbc));
		comics.add(new ComicInfo(context, R.raw.userfriendly));
		comics.add(new ComicInfo(context, R.raw.vgcats));
		comics.add(new ComicInfo(context, R.raw.wondermark));
		comics.add(new ComicInfo(context, R.raw.xkcd));
		// These three can't be parsed with the standard Downloader, on account of their lack of prev/next links
		comics.add(new ComicInfo("Married To The Sea", "http://www.marriedtothesea.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.marriedtothesea.com"); }}));
		comics.add(new ComicInfo("Natalie Dee", "http://www.nataliedee.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.nataliedee.com"); }}));
		comics.add(new ComicInfo("Toothpaste For Dinner", "http://www.toothpastefordinner.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.toothpastefordinner.com"); }}));
				
		comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "Archie", "http://www.creators.com/comics/archie.html"));
	   	comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "Ask Shagg", "http://www.creators.com/comics/ask-shagg.html"));
		comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "Flare", "http://www.creators.com/comics/flare.html"));
	   	comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "For Heaven's Sake", "http://www.creators.com/comics/for-heavens-sake.html"));
	   	comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "The Quigmans", "http://www.creators.com/comics/the-quigmans.html"));
	   	comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "Rugrats", "http://www.creators.com/comics/rugrats.html"));
	   	comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "State of the Union", "http://www.creators.com/comics/state-of-the-union.html"));
	   	comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "Thin Lines", "http://www.creators.com/comics/thin-lines.html"));
	   	comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "Wee Pals", "http://www.creators.com/comics/wee-pals.html"));
	   	comics.add(new ComicInfo(context, R.raw.creatorsdotcom, "Winnie the Pooh", "http://www.creators.com/comics/winnie-the-pooh.html"));
	   	comics.add(new ComicInfo(context, R.raw.arcamax, "Baby Blues", "http://www.arcamax.com/babyblues/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Beetle Bailey", "http://www.arcamax.com/beetlebailey/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Blondie", "http://www.arcamax.com/blondie/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "The Boondocks", "http://www.arcamax.com/boondocks/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Cathy", "http://www.arcamax.com/cathy/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Dustin", "http://www.arcamax.com/dustin/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Family Circus", "http://www.arcamax.com/familycircus/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Hagar the Horrible", "http://www.arcamax.com/hagarthehorrible/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Jerry King Cartoons", "http://www.arcamax.com/humorcartoon/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Mother Goose & Grimm", "http://www.arcamax.com/mothergooseandgrimm/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Mutts", "http://www.arcamax.com/mutts/"));
		comics.add(new ComicInfo(context, R.raw.arcamax, "Zits", "http://www.arcamax.com/zits/"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The Academia Waltz", "http://www.gocomics.com/academiawaltz"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Adam@Home", "http://www.gocomics.com/adamathome"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The Argyle Sweater", "http://www.gocomics.com/theargylesweater"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Back in the Day", "http://www.gocomics.com/backintheday"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Bad Reporter", "http://www.gocomics.com/badreporter"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Baldo", "http://www.gocomics.com/baldo"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Barkeater Lake", "http://www.gocomics.com/barkeaterlake"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Basic Instructions", "http://www.gocomics.com/basicinstructions"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Bewley", "http://www.gocomics.com/bewley"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Big Top", "http://www.gocomics.com/bigtop"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Biographic", "http://www.gocomics.com/biographic"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Birdbrains", "http://www.gocomics.com/birdbrains"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Bleeker: The Rechargeable Dog", "http://www.gocomics.com/bleeker"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Bloom County", "http://www.gocomics.com/bloomcounty"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Bo Nanas", "http://www.gocomics.com/bonanas"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Bob the Squirrel", "http://www.gocomics.com/bobthesquirrel"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The Boiling Point", "http://www.gocomics.com/theboilingpoint"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Boomerangs", "http://www.gocomics.com/boomerangs"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Brainwaves", "http://www.gocomics.com/brainwaves"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Calvin and Hobbes", "http://www.gocomics.com/calvinandhobbes"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "C'est la Vie", "http://www.gocomics.com/cestlavie"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Citizen Dog", "http://www.gocomics.com/citizendog"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The City", "http://www.gocomics.com/thecity"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Cleats", "http://www.gocomics.com/cleats"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Close to Home", "http://www.gocomics.com/closetohome"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Compu-toon", "http://www.gocomics.com/compu-toon"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Cornered", "http://www.gocomics.com/cornered"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Cul de Sac", "http://www.gocomics.com/culdesac"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Deep Cover", "http://www.gocomics.com/deepcover"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Domestic Abuse", "http://www.gocomics.com/domesticabuse"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Doodles", "http://www.gocomics.com/doodles"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Doonesbury", "http://www.gocomics.com/doonesbury"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The Doozies", "http://www.gocomics.com/thedoozies"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The Duplex", "http://www.gocomics.com/duplex"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Eek!", "http://www.gocomics.com/eek"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The Elderberries", "http://www.gocomics.com/theelderberries"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The Flying McCoys", "http://www.gocomics.com/theflyingmccoys"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "For Better or For Worse", "http://www.gocomics.com/forbetterorforworse"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "FoxTrot", "http://www.gocomics.com/foxtrot"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "FoxTrot Classics", "http://www.gocomics.com/foxtrotclassics"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Fred Basset", "http://www.gocomics.com/fredbasset"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Frog Applause", "http://www.gocomics.com/frogapplause"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The Fusco Brothers", "http://www.gocomics.com/thefuscobrothers"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Garfield", "http://www.gocomics.com/garfield"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Garfield Minus Garfield", "http://www.gocomics.com/garfieldminusgarfield"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "GET A LIFE!", "http://www.gocomics.com/getalife"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Ginger Meggs", "http://www.gocomics.com/gingermeggs"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Haiku Ewe", "http://www.gocomics.com/haikuewe"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Heart of the City", "http://www.gocomics.com/heartofthecity"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Housebroken", "http://www.gocomics.com/housebroken"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Hubert and Abby", "http://www.gocomics.com/hubertandabby"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Imagine This", "http://www.gocomics.com/imaginethis"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "In the Bleachers", "http://www.gocomics.com/inthebleachers"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "In the Sticks", "http://www.gocomics.com/inthesticks"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Ink Pen", "http://www.gocomics.com/inkpen"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Joe Vanilla", "http://www.gocomics.com/joevanilla"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "La Cucaracha", "http://www.gocomics.com/lacucaracha"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Last Kiss", "http://www.gocomics.com/lastkiss"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Legend of Bill", "http://www.gocomics.com/legendofbill"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Lio", "http://www.gocomics.com/lio"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Little Otto", "http://www.gocomics.com/littleotto"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Maintaining", "http://www.gocomics.com/maintaining"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Mal and Chad", "http://www.gocomics.com/malandchad"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Mutt & Jeff", "http://www.gocomics.com/muttandjeff"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Mythtickle", "http://www.gocomics.com/mythtickle"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "NEUROTICA", "http://www.gocomics.com/neurotica"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "New Adventures of Queen Victoria", "http://www.gocomics.com/thenewadventuresofqueenvictoria"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Non Sequitur", "http://www.gocomics.com/nonsequitur"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "The Norm", "http://www.gocomics.com/thenorm"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Out of the Gene Pool Re-Runs", "http://www.gocomics.com/outofthegenepool"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Overboard", "http://www.gocomics.com/overboard"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Pibgorn", "http://www.gocomics.com/pibgorn"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Pibgorn Sketches", "http://www.gocomics.com/pibgornsketches"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Pinkerton", "http://www.gocomics.com/pinkerton"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Pooch Cafe", "http://www.gocomics.com/poochcafe"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "PreTeena", "http://www.gocomics.com/preteena"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Rabbits Against Magic", "http://www.gocomics.com/rabbitsagainstmagic"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Real Life Adventures", "http://www.gocomics.com/reallifeadventures"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Red Meat", "http://www.gocomics.com/redmeat"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Reynolds Unwrapped", "http://www.gocomics.com/reynoldsunwrapped"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Ronaldinho Gaucho", "http://www.gocomics.com/ronaldinhogaucho"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Shoe", "http://www.gocomics.com/shoe"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Shoecabbage", "http://www.gocomics.com/shoecabbage"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Skin Horse", "http://www.gocomics.com/skinhorse"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Slowpoke", "http://www.gocomics.com/slowpoke"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Stone Soup", "http://www.gocomics.com/stonesoup"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Tank McNamara", "http://www.gocomics.com/tankmcnamara"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Tiny Sepuku", "http://www.gocomics.com/tinysepuku"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "TOBY", "http://www.gocomics.com/toby"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Tom the Dancing Bug", "http://www.gocomics.com/tomthedancingbug"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Too Much Coffee Man", "http://www.gocomics.com/toomuchcoffeeman"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "W.T. Duck", "http://www.gocomics.com/wtduck"));
		comics.add(new ComicInfo(context, R.raw.gocomics, "Yenny", "http://www.gocomics.com/yenny"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "9 Chickweed Lane", "http://www.comics.com/9_chickweed_lane"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "9 to 5", "http://www.comics.com/9_to_5"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Agnes", "http://www.comics.com/agnes"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Alley Oop", "http://www.comics.com/alley_oop"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Andy Capp", "http://www.comics.com/andy_capp"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Animal Crackers", "http://www.comics.com/animal_crackers"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Annie", "http://www.comics.com/annie"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Arlo & Janis", "http://www.comics.com/arlo&janis"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "B.C.", "http://www.comics.com/bc"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Ballard Street", "http://www.comics.com/ballard_street"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Ben", "http://www.comics.com/ben"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Betty", "http://www.comics.com/betty"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Big Nate", "http://www.comics.com/big_nate"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Bliss", "http://www.comics.com/bliss"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Bottom Liners", "http://www.comics.com/bottom_liners"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Bound and Gagged", "http://www.comics.com/bound_and_gagged"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Brenda Starr", "http://www.comics.com/brenda_starr"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Brevity", "http://www.comics.com/brevity"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Brewster Rockit", "http://www.comics.com/brewster_rockit"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Broom-Hilda", "http://www.comics.com/broom-hilda"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Cafe Con Leche", "http://www.comics.com/cafe_con_leche"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Candorville", "http://www.comics.com/candorville"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Cheap Thrills", "http://www.comics.com/cheap_thrills"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Chuckle Bros", "http://www.comics.com/chuckle_bros"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Committed", "http://www.comics.com/committed"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Cow & Boy", "http://www.comics.com/cow&boy"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Daddy's Home", "http://www.comics.com/daddys_home"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Dick Tracy", "http://www.comics.com/dick_tracy"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Dog eat Doug", "http://www.comics.com/dog_eat_doug"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Drabble", "http://www.comics.com/drabble"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "F Minus", "http://www.comics.com/f_minus"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Family Tree", "http://www.comics.com/family_tree"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Farcus", "http://www.comics.com/farcus"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Fat Cats Classics", "http://www.comics.com/fat_cats_classics"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Ferd'nand", "http://www.comics.com/ferdnand"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Flight Deck", "http://www.comics.com/flight_deck"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Flo & Friends", "http://www.comics.com/flo&friends"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Fort Knox", "http://www.comics.com/fort_knox"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Frank & Ernest", "http://www.comics.com/frank&ernest"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Frazz", "http://www.comics.com/frazz"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Free Range", "http://www.comics.com/free_range"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Gasoline Alley", "http://www.comics.com/gasoline_alley"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Geech Classics", "http://www.comics.com/geech_classics"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Get Fuzzy", "http://www.comics.com/get_fuzzy"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Gil Thorp", "http://www.comics.com/gil_thorp"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Girls & Sports", "http://www.comics.com/girls&sports"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Graffiti", "http://www.comics.com/graffiti"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Grand Avenue", "http://www.comics.com/grand_avenue"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Heathcliff", "http://www.comics.com/heathcliff"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Herb and Jamaal", "http://www.comics.com/herb_and_jamaal"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Herman", "http://www.comics.com/herman"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Home and Away", "http://www.comics.com/home_and_away"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "It's All About You", "http://www.comics.com/its_all_about_you"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Jane's World", "http://www.comics.com/janes_world"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Jump Start", "http://www.comics.com/jump_start"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Kit 'N' Carlyle", "http://www.comics.com/kit_n_carlyle"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Li'l Abner Classics", "http://www.comics.com/lil_abner_classics"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Liberty Meadows", "http://www.comics.com/liberty_meadows"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Little Dog Lost", "http://www.comics.com/little_dog_lost"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Lola", "http://www.comics.com/lola"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Loose Parts", "http://www.comics.com/loose_parts"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Love Is...", "http://www.comics.com/love_is"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Luann", "http://www.comics.com/luann"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Marmaduke", "http://www.comics.com/marmaduke"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Meg! Classics", "http://www.comics.com/meg_classics"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Minimum Security", "http://www.comics.com/minimum_security"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Moderately Confused", "http://www.comics.com/moderately_confused"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Momma", "http://www.comics.com/momma"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Monty", "http://www.comics.com/monty"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Motley Classics", "http://www.comics.com/motley_classics"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Nancy", "http://www.comics.com/nancy"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Natural Selection", "http://www.comics.com/natural_selection"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Nest Heads", "http://www.comics.com/nest_heads"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Off The Mark", "http://www.comics.com/off_the_mark"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "On a Claire Day", "http://www.comics.com/on_a_claire_day"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "One Big Happy Classics", "http://www.comics.com/one_big_happy_classics"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Over the Hedge", "http://www.comics.com/over_the_hedge"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "PC and Pixel", "http://www.comics.com/pc_and_pixel"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Peanuts", "http://www.comics.com/peanuts"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Pearls Before Swine", "http://www.comics.com/pearls_before_swine"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Pickles", "http://www.comics.com/pickles"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Pluggers", "http://www.comics.com/pluggers"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Prickly City", "http://www.comics.com/prickly_city"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Raising Duncan Classics", "http://www.comics.com/raising_duncan_classics"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Reality Check", "http://www.comics.com/reality_check"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Red & Rover", "http://www.comics.com/red&rover"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Rip Haywire", "http://www.comics.com/rip_haywire"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Ripley's Believe It or Not!", "http://www.comics.com/ripleys_believe_it_or_not"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Rose Is Rose", "http://www.comics.com/rose_is_rose"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Rubes", "http://www.comics.com/rubes"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Rudy Park", "http://www.comics.com/rudy_park"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Scary Gary", "http://www.comics.com/scary_gary"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Shirley and Son Classics", "http://www.comics.com/shirley_and_son_classics"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Soup To Nutz", "http://www.comics.com/soup_to_nutz"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Speed Bump", "http://www.comics.com/speed_bump"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Spot The Frog", "http://www.comics.com/spot_the_frog"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Strange Brew", "http://www.comics.com/strange_brew"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Sylvia", "http://www.comics.com/sylvia"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Tarzan Classics", "http://www.comics.com/tarzan_classics"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "That's Life", "http://www.comics.com/thats_life"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Barn", "http://www.comics.com/the_barn"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Born Loser", "http://www.comics.com/the_born_loser"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Buckets", "http://www.comics.com/the_buckets"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Dinette Set", "http://www.comics.com/the_dinette_set"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Grizzwells", "http://www.comics.com/the_grizzwells"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Humble Stumble", "http://www.comics.com/the_humble_stumble"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Knight Life", "http://www.comics.com/the_knight_life"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Meaning of Lila", "http://www.comics.com/the_meaning_of_lila"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Middletons", "http://www.comics.com/the_middeltons"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Other Coast", "http://www.comics.com/the_other_coast"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "The Sunshine Club", "http://www.comics.com/the_sunshine_club"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Unstrange Phenomena", "http://www.comics.com/unstrange_phenomena"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Watch Your Head", "http://www.comics.com/watch_your_head"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Wizard of Id", "http://www.comics.com/wizard_of_id"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Working Daze", "http://www.comics.com/working_daze"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Working It Out", "http://www.comics.com/working_it_out"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "Zack Hill", "http://www.comics.com/zack_hill"));
		comics.add(new ComicInfo(context, R.raw.comicsdotcom, "(Th)ink", "http://www.comics.com/think"));
		
		comics.add(new ComicInfo(context, R.raw.houstonchronicle, "Pardon My Planet", "http://www.chron.com/apps/comics/buildcp.mpl?c=152&page=1&cpp=1&v=3.0&quality=high"));
		
		Collections.sort(comics);
	}
}
