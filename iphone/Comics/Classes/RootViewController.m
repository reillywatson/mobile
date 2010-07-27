//
//  RootViewController.m
//  Comics
//
//  Created by Reilly Watson on 10-05-15.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "RootViewController.h"
#include "ComicInfo.h"
#import "Viewer.h"
#import "ComicsAppDelegate.h"

@interface RootViewController (Private)
-(void)initComicsArray;
-(void)addComicWithJSON:(NSString *)jsonFile;
-(void)addComicWithJSON:(NSString *)jsonFile title:(NSString *)title startURL: (NSString *)startUrl;
@end

@implementation RootViewController

NSInteger titleSort(id comic1, id comic2, void *context)
{
	NSString *title1 = [((ComicInfo *)comic1).title stringByReplacingOccurrencesOfString:@"The " withString:@""];
	NSString *title2 = [((ComicInfo *)comic2).title stringByReplacingOccurrencesOfString:@"The " withString:@""];
	return [title1 compare:title2];
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	lastJSONPath = nil;
	lastJSONObject = nil;
	
	self.title = @"Comics";
	comics = [NSMutableArray new];
	favourites = [NSMutableArray new];
	[self initComicsArray];
}

-(void)viewWillAppear:(BOOL)animated {
	[favourites removeAllObjects];
	NSDictionary *favouriteTitles = [((ComicsAppDelegate *)[[UIApplication sharedApplication] delegate]) favourites];
	for (ComicInfo *comic in comics) {
		if ([favouriteTitles objectForKey:comic.title] != nil) {
			[favourites addObject:comic];
		}
	}
	[favourites sortUsingFunction:titleSort context:nil];
	[self.view reloadData];
}

-(void)viewWillDisappear:(BOOL)animated {
	
}

#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
	if ([favourites count] == 0)
		return nil;
	UILabel *label = [UILabel new];
	[label setText:(section == 0 ? @"Favorites" : @"Comics")];
	return label;
	
}

// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	if (section == 0)
		return [favourites count];
    return [comics count];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	ComicInfo *info;
	if ([indexPath section] == 0)
		info = [favourites objectAtIndex:[indexPath row]];
	else
		info = [comics objectAtIndex:[indexPath row]];
	
	Viewer *viewer = [[[Viewer alloc] initWithComic:info] autorelease];
	[[self navigationController] pushViewController:viewer animated:YES];
}

- (BOOL) shouldAutorotateToInterfaceOrientation: (UIInterfaceOrientation) interfaceOrientation {
	return YES;
}

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
	
	ComicInfo *info;
	
	if ([indexPath section] == 0) {
		info = [favourites objectAtIndex:[indexPath row]];
	}
	else {
		info = [comics objectAtIndex:[indexPath row]];
	}
	
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
    
	// Configure the cell.
	[cell.textLabel setText:info.title];

    return cell;
}
/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
		[comics removeObjectAtIndex:[indexPath row]];
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
}




// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
	id cell = [comics objectAtIndex:[fromIndexPath row]];
	[comics removeObjectAtIndex:[fromIndexPath row]];
	[comics insertObject:cell atIndex:[toIndexPath row]];
}
*/

- (void)dealloc {
    [super dealloc];
	[comics release];
}

-(void)initComicsArray {
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"achewood" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"amazingsuperpowers" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"asofterworld" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"awkwardzombie" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"bookofbiff" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"buttersafe" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"capesnbabes" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"ctrlaltdel" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"ctrlaltdelsillies" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"cyanideandhappiness" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"dilbert" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"dinosaurcomics" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"girlgenius" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"girlswithslingshots" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"leasticoulddo" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"lookingforgroup" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"megatokyo" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"nedroid" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"pennyarcade" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"perrybiblefellowship" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"pvp" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"questionablecontent" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"sharkattack" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"shermanslagoon" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"shockwatson" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"sinfest" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"smbc" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"userfriendly" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"vgcats" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"wondermark" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"xkcd" ofType:@"json"]];
	// These three can't be parsed with the standard Downloader, on account of their lack of prev/next links
	//comics.add(new ComicInfo("Married To The Sea" startURL:@"http://www.marriedtothesea.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.marriedtothesea.com"); }}];
	//comics.add(new ComicInfo("Natalie Dee" startURL:@"http://www.nataliedee.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.nataliedee.com"); }}];
	//comics.add(new ComicInfo("Toothpaste For Dinner" startURL:@"http://www.toothpastefordinner.com/", new Callable<Downloader>(){public Downloader call() throws Exception { return new SharingMachineDownloader("http://www.toothpastefordinner.com"); }}];

	
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Apartment 3-G" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=100&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Better Half" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=103&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Between Friends" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=143&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Bizarro" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=153&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Buckles" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=105&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Crankshaft" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=154&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Crock" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=106&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Curtis" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=107&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Dennis the Menace" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=108&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Edge City" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=151&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Fast Track" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=111&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Funky Winkerbean" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=112&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Grin and Bear It" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=113&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Hi & Lois" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=115&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Judge Parker" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=116&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Lockhorns" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=117&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Mallard Fillmore" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=118&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Mark Trail" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=144&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Marvin" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=119&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Mary Worth" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=120&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"My Cage" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=157&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Pardon My Planet" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=152&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Phantom" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=123&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Piranha Club" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=124&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Redeye" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=126&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Rex Morgan" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=127&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Rhymes with Orange" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=136&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Safe Havens" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=128&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Sally Forth" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=129&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Six Chix" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=147&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Slylock Fox" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=146&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Snuffy Smith" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=148&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Spider-Man" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=130&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"The Brilliant Mind of Edison Lee" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=156&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Tiger" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=132&page=1&cpp=1&v=3.0&quality=high"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Zippy the Pinhead" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=137&page=1&cpp=1&v=3.0&quality=high"];
	
	
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"Archie" startURL:@"http://www.creators.com/comics/archie.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"Ask Shagg" startURL:@"http://www.creators.com/comics/ask-shagg.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"Flare" startURL:@"http://www.creators.com/comics/flare.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"For Heaven's Sake" startURL:@"http://www.creators.com/comics/for-heavens-sake.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"The Quigmans" startURL:@"http://www.creators.com/comics/the-quigmans.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"Rugrats" startURL:@"http://www.creators.com/comics/rugrats.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"State of the Union" startURL:@"http://www.creators.com/comics/state-of-the-union.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"Thin Lines" startURL:@"http://www.creators.com/comics/thin-lines.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"Wee Pals" startURL:@"http://www.creators.com/comics/wee-pals.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"creatorsdotcom" ofType:@"json"] title:@"Winnie the Pooh" startURL:@"http://www.creators.com/comics/winnie-the-pooh.html"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Baby Blues" startURL:@"http://www.arcamax.com/babyblues/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Beetle Bailey" startURL:@"http://www.arcamax.com/beetlebailey/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Blondie" startURL:@"http://www.arcamax.com/blondie/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"The Boondocks" startURL:@"http://www.arcamax.com/boondocks/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Cathy" startURL:@"http://www.arcamax.com/cathy/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Dustin" startURL:@"http://www.arcamax.com/dustin/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Family Circus" startURL:@"http://www.arcamax.com/familycircus/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Hagar the Horrible" startURL:@"http://www.arcamax.com/hagarthehorrible/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Jerry King Cartoons" startURL:@"http://www.arcamax.com/humorcartoon/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Mother Goose & Grimm" startURL:@"http://www.arcamax.com/mothergooseandgrimm/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Mutts" startURL:@"http://www.arcamax.com/mutts/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Zits" startURL:@"http://www.arcamax.com/zits/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The Academia Waltz" startURL:@"http://www.gocomics.com/academiawaltz"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Adam@Home" startURL:@"http://www.gocomics.com/adamathome"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The Argyle Sweater" startURL:@"http://www.gocomics.com/theargylesweater"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Back in the Day" startURL:@"http://www.gocomics.com/backintheday"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Bad Reporter" startURL:@"http://www.gocomics.com/badreporter"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Baldo" startURL:@"http://www.gocomics.com/baldo"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Barkeater Lake" startURL:@"http://www.gocomics.com/barkeaterlake"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Basic Instructions" startURL:@"http://www.gocomics.com/basicinstructions"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Bewley" startURL:@"http://www.gocomics.com/bewley"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Big Top" startURL:@"http://www.gocomics.com/bigtop"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Biographic" startURL:@"http://www.gocomics.com/biographic"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Birdbrains" startURL:@"http://www.gocomics.com/birdbrains"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Bleeker: The Rechargeable Dog" startURL:@"http://www.gocomics.com/bleeker"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Bloom County" startURL:@"http://www.gocomics.com/bloomcounty"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Bo Nanas" startURL:@"http://www.gocomics.com/bonanas"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Bob the Squirrel" startURL:@"http://www.gocomics.com/bobthesquirrel"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The Boiling Point" startURL:@"http://www.gocomics.com/theboilingpoint"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Boomerangs" startURL:@"http://www.gocomics.com/boomerangs"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Brainwaves" startURL:@"http://www.gocomics.com/brainwaves"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Calvin and Hobbes" startURL:@"http://www.gocomics.com/calvinandhobbes"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"C'est la Vie" startURL:@"http://www.gocomics.com/cestlavie"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Citizen Dog" startURL:@"http://www.gocomics.com/citizendog"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The City" startURL:@"http://www.gocomics.com/thecity"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Cleats" startURL:@"http://www.gocomics.com/cleats"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Close to Home" startURL:@"http://www.gocomics.com/closetohome"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Compu-toon" startURL:@"http://www.gocomics.com/compu-toon"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Cornered" startURL:@"http://www.gocomics.com/cornered"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Cul de Sac" startURL:@"http://www.gocomics.com/culdesac"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Deep Cover" startURL:@"http://www.gocomics.com/deepcover"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Domestic Abuse" startURL:@"http://www.gocomics.com/domesticabuse"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Doodles" startURL:@"http://www.gocomics.com/doodles"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Doonesbury" startURL:@"http://www.gocomics.com/doonesbury"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The Doozies" startURL:@"http://www.gocomics.com/thedoozies"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The Duplex" startURL:@"http://www.gocomics.com/duplex"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Eek!" startURL:@"http://www.gocomics.com/eek"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The Elderberries" startURL:@"http://www.gocomics.com/theelderberries"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The Flying McCoys" startURL:@"http://www.gocomics.com/theflyingmccoys"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"For Better or For Worse" startURL:@"http://www.gocomics.com/forbetterorforworse"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"FoxTrot" startURL:@"http://www.gocomics.com/foxtrot"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"FoxTrot Classics" startURL:@"http://www.gocomics.com/foxtrotclassics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Fred Basset" startURL:@"http://www.gocomics.com/fredbasset"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Frog Applause" startURL:@"http://www.gocomics.com/frogapplause"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The Fusco Brothers" startURL:@"http://www.gocomics.com/thefuscobrothers"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Garfield" startURL:@"http://www.gocomics.com/garfield"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Garfield Minus Garfield" startURL:@"http://www.gocomics.com/garfieldminusgarfield"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"GET A LIFE!" startURL:@"http://www.gocomics.com/getalife"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Ginger Meggs" startURL:@"http://www.gocomics.com/gingermeggs"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Haiku Ewe" startURL:@"http://www.gocomics.com/haikuewe"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Heart of the City" startURL:@"http://www.gocomics.com/heartofthecity"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Housebroken" startURL:@"http://www.gocomics.com/housebroken"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Hubert and Abby" startURL:@"http://www.gocomics.com/hubertandabby"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Imagine This" startURL:@"http://www.gocomics.com/imaginethis"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"In the Bleachers" startURL:@"http://www.gocomics.com/inthebleachers"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"In the Sticks" startURL:@"http://www.gocomics.com/inthesticks"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Ink Pen" startURL:@"http://www.gocomics.com/inkpen"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Joe Vanilla" startURL:@"http://www.gocomics.com/joevanilla"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"La Cucaracha" startURL:@"http://www.gocomics.com/lacucaracha"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Last Kiss" startURL:@"http://www.gocomics.com/lastkiss"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Legend of Bill" startURL:@"http://www.gocomics.com/legendofbill"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Lio" startURL:@"http://www.gocomics.com/lio"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Little Otto" startURL:@"http://www.gocomics.com/littleotto"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Maintaining" startURL:@"http://www.gocomics.com/maintaining"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Mal and Chad" startURL:@"http://www.gocomics.com/malandchad"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Mutt & Jeff" startURL:@"http://www.gocomics.com/muttandjeff"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Mythtickle" startURL:@"http://www.gocomics.com/mythtickle"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"NEUROTICA" startURL:@"http://www.gocomics.com/neurotica"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"New Adventures of Queen Victoria" startURL:@"http://www.gocomics.com/thenewadventuresofqueenvictoria"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Non Sequitur" startURL:@"http://www.gocomics.com/nonsequitur"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"The Norm" startURL:@"http://www.gocomics.com/thenorm"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Out of the Gene Pool Re-Runs" startURL:@"http://www.gocomics.com/outofthegenepool"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Overboard" startURL:@"http://www.gocomics.com/overboard"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Pibgorn" startURL:@"http://www.gocomics.com/pibgorn"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Pibgorn Sketches" startURL:@"http://www.gocomics.com/pibgornsketches"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Pinkerton" startURL:@"http://www.gocomics.com/pinkerton"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Pooch Cafe" startURL:@"http://www.gocomics.com/poochcafe"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"PreTeena" startURL:@"http://www.gocomics.com/preteena"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Rabbits Against Magic" startURL:@"http://www.gocomics.com/rabbitsagainstmagic"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Real Life Adventures" startURL:@"http://www.gocomics.com/reallifeadventures"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Red Meat" startURL:@"http://www.gocomics.com/redmeat"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Reynolds Unwrapped" startURL:@"http://www.gocomics.com/reynoldsunwrapped"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Ronaldinho Gaucho" startURL:@"http://www.gocomics.com/ronaldinhogaucho"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Shoe" startURL:@"http://www.gocomics.com/shoe"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Shoecabbage" startURL:@"http://www.gocomics.com/shoecabbage"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Skin Horse" startURL:@"http://www.gocomics.com/skinhorse"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Slowpoke" startURL:@"http://www.gocomics.com/slowpoke"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Stone Soup" startURL:@"http://www.gocomics.com/stonesoup"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Tank McNamara" startURL:@"http://www.gocomics.com/tankmcnamara"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Tiny Sepuku" startURL:@"http://www.gocomics.com/tinysepuku"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"TOBY" startURL:@"http://www.gocomics.com/toby"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Tom the Dancing Bug" startURL:@"http://www.gocomics.com/tomthedancingbug"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Too Much Coffee Man" startURL:@"http://www.gocomics.com/toomuchcoffeeman"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"W.T. Duck" startURL:@"http://www.gocomics.com/wtduck"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomicsiphone" ofType:@"json"] title:@"Yenny" startURL:@"http://www.gocomics.com/yenny"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"9 Chickweed Lane" startURL:@"http://www.comics.com/9_chickweed_lane"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"9 to 5" startURL:@"http://www.comics.com/9_to_5"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Agnes" startURL:@"http://www.comics.com/agnes"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Alley Oop" startURL:@"http://www.comics.com/alley_oop"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Andy Capp" startURL:@"http://www.comics.com/andy_capp"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Animal Crackers" startURL:@"http://www.comics.com/animal_crackers"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Annie" startURL:@"http://www.comics.com/annie"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Arlo & Janis" startURL:@"http://www.comics.com/arlo&janis"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"B.C." startURL:@"http://www.comics.com/bc"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Ballard Street" startURL:@"http://www.comics.com/ballard_street"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Ben" startURL:@"http://www.comics.com/ben"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Betty" startURL:@"http://www.comics.com/betty"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Big Nate" startURL:@"http://www.comics.com/big_nate"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Bliss" startURL:@"http://www.comics.com/bliss"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Bottom Liners" startURL:@"http://www.comics.com/bottom_liners"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Bound and Gagged" startURL:@"http://www.comics.com/bound_and_gagged"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Brenda Starr" startURL:@"http://www.comics.com/brenda_starr"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Brevity" startURL:@"http://www.comics.com/brevity"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Brewster Rockit" startURL:@"http://www.comics.com/brewster_rockit"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Broom-Hilda" startURL:@"http://www.comics.com/broom-hilda"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Cafe Con Leche" startURL:@"http://www.comics.com/cafe_con_leche"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Candorville" startURL:@"http://www.comics.com/candorville"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Cheap Thrills" startURL:@"http://www.comics.com/cheap_thrills"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Chuckle Bros" startURL:@"http://www.comics.com/chuckle_bros"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Committed" startURL:@"http://www.comics.com/committed"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Cow & Boy" startURL:@"http://www.comics.com/cow&boy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Daddy's Home" startURL:@"http://www.comics.com/daddys_home"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Dick Tracy" startURL:@"http://www.comics.com/dick_tracy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Dog eat Doug" startURL:@"http://www.comics.com/dog_eat_doug"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Drabble" startURL:@"http://www.comics.com/drabble"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"F Minus" startURL:@"http://www.comics.com/f_minus"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Family Tree" startURL:@"http://www.comics.com/family_tree"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Farcus" startURL:@"http://www.comics.com/farcus"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Fat Cats Classics" startURL:@"http://www.comics.com/fat_cats_classics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Ferd'nand" startURL:@"http://www.comics.com/ferdnand"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Flight Deck" startURL:@"http://www.comics.com/flight_deck"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Flo & Friends" startURL:@"http://www.comics.com/flo&friends"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Fort Knox" startURL:@"http://www.comics.com/fort_knox"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Frank & Ernest" startURL:@"http://www.comics.com/frank&ernest"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Frazz" startURL:@"http://www.comics.com/frazz"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Free Range" startURL:@"http://www.comics.com/free_range"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Gasoline Alley" startURL:@"http://www.comics.com/gasoline_alley"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Geech Classics" startURL:@"http://www.comics.com/geech_classics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Get Fuzzy" startURL:@"http://www.comics.com/get_fuzzy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Gil Thorp" startURL:@"http://www.comics.com/gil_thorp"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Girls & Sports" startURL:@"http://www.comics.com/girls&sports"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Graffiti" startURL:@"http://www.comics.com/graffiti"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Grand Avenue" startURL:@"http://www.comics.com/grand_avenue"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Heathcliff" startURL:@"http://www.comics.com/heathcliff"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Herb and Jamaal" startURL:@"http://www.comics.com/herb_and_jamaal"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Herman" startURL:@"http://www.comics.com/herman"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Home and Away" startURL:@"http://www.comics.com/home_and_away"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"It's All About You" startURL:@"http://www.comics.com/its_all_about_you"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Jane's World" startURL:@"http://www.comics.com/janes_world"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Jump Start" startURL:@"http://www.comics.com/jump_start"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Kit 'N' Carlyle" startURL:@"http://www.comics.com/kit_n_carlyle"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Li'l Abner Classics" startURL:@"http://www.comics.com/lil_abner_classics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Liberty Meadows" startURL:@"http://www.comics.com/liberty_meadows"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Little Dog Lost" startURL:@"http://www.comics.com/little_dog_lost"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Lola" startURL:@"http://www.comics.com/lola"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Loose Parts" startURL:@"http://www.comics.com/loose_parts"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Love Is..." startURL:@"http://www.comics.com/love_is"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Luann" startURL:@"http://www.comics.com/luann"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Marmaduke" startURL:@"http://www.comics.com/marmaduke"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Meg! Classics" startURL:@"http://www.comics.com/meg_classics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Minimum Security" startURL:@"http://www.comics.com/minimum_security"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Moderately Confused" startURL:@"http://www.comics.com/moderately_confused"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Momma" startURL:@"http://www.comics.com/momma"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Monty" startURL:@"http://www.comics.com/monty"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Motley Classics" startURL:@"http://www.comics.com/motley_classics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Nancy" startURL:@"http://www.comics.com/nancy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Natural Selection" startURL:@"http://www.comics.com/natural_selection"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Nest Heads" startURL:@"http://www.comics.com/nest_heads"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Off The Mark" startURL:@"http://www.comics.com/off_the_mark"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"On a Claire Day" startURL:@"http://www.comics.com/on_a_claire_day"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"One Big Happy Classics" startURL:@"http://www.comics.com/one_big_happy_classics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Over the Hedge" startURL:@"http://www.comics.com/over_the_hedge"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"PC and Pixel" startURL:@"http://www.comics.com/pc_and_pixel"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Peanuts" startURL:@"http://www.comics.com/peanuts"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Pearls Before Swine" startURL:@"http://www.comics.com/pearls_before_swine"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Pickles" startURL:@"http://www.comics.com/pickles"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Pluggers" startURL:@"http://www.comics.com/pluggers"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Prickly City" startURL:@"http://www.comics.com/prickly_city"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Raising Duncan Classics" startURL:@"http://www.comics.com/raising_duncan_classics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Reality Check" startURL:@"http://www.comics.com/reality_check"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Red & Rover" startURL:@"http://www.comics.com/red&rover"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Rip Haywire" startURL:@"http://www.comics.com/rip_haywire"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Ripley's Believe It or Not!" startURL:@"http://www.comics.com/ripleys_believe_it_or_not"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Rose Is Rose" startURL:@"http://www.comics.com/rose_is_rose"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Rubes" startURL:@"http://www.comics.com/rubes"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Rudy Park" startURL:@"http://www.comics.com/rudy_park"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Scary Gary" startURL:@"http://www.comics.com/scary_gary"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Shirley and Son Classics" startURL:@"http://www.comics.com/shirley_and_son_classics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Soup To Nutz" startURL:@"http://www.comics.com/soup_to_nutz"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Speed Bump" startURL:@"http://www.comics.com/speed_bump"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Spot The Frog" startURL:@"http://www.comics.com/spot_the_frog"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Strange Brew" startURL:@"http://www.comics.com/strange_brew"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Sylvia" startURL:@"http://www.comics.com/sylvia"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Tarzan Classics" startURL:@"http://www.comics.com/tarzan_classics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"That's Life" startURL:@"http://www.comics.com/thats_life"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Barn" startURL:@"http://www.comics.com/the_barn"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Born Loser" startURL:@"http://www.comics.com/the_born_loser"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Buckets" startURL:@"http://www.comics.com/the_buckets"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Dinette Set" startURL:@"http://www.comics.com/the_dinette_set"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Grizzwells" startURL:@"http://www.comics.com/the_grizzwells"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Humble Stumble" startURL:@"http://www.comics.com/the_humble_stumble"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Knight Life" startURL:@"http://www.comics.com/the_knight_life"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Meaning of Lila" startURL:@"http://www.comics.com/the_meaning_of_lila"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Middletons" startURL:@"http://www.comics.com/the_middeltons"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Other Coast" startURL:@"http://www.comics.com/the_other_coast"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"The Sunshine Club" startURL:@"http://www.comics.com/the_sunshine_club"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Unstrange Phenomena" startURL:@"http://www.comics.com/unstrange_phenomena"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Watch Your Head" startURL:@"http://www.comics.com/watch_your_head"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Wizard of Id" startURL:@"http://www.comics.com/wizard_of_id"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Working Daze" startURL:@"http://www.comics.com/working_daze"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Working It Out" startURL:@"http://www.comics.com/working_it_out"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"Zack Hill" startURL:@"http://www.comics.com/zack_hill"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"comicsdotcom" ofType:@"json"] title:@"(Th)ink" startURL:@"http://www.comics.com/think"];

	[comics sortUsingFunction:titleSort context:nil];
}

-(void)addComicWithJSON:(NSString *)jsonFile {
	[self addComicWithJSON:jsonFile title:nil startURL:nil];
}


-(void)addComicWithJSON:(NSString *)jsonFile title:(NSString *)title startURL: (NSString *)startUrl {
	NSDictionary *parsedJSON;
	if (lastJSONPath != nil && [lastJSONPath isEqual:jsonFile]) {
		parsedJSON = lastJSONObject;
	}
	else {
		NSString *jsonStr = [NSString stringWithContentsOfFile:jsonFile encoding:NSUTF8StringEncoding error:nil];
		SBJSON *json = [[SBJSON new] autorelease];
		NSError *jsonError;
		parsedJSON = [json objectWithString:jsonStr error:&jsonError];
		[lastJSONPath release];
		[lastJSONObject release];
		lastJSONPath = [jsonFile retain];
		lastJSONObject = [parsedJSON retain];
	}
	ComicInfo *comic = [[ComicInfo new] autorelease];
	comic.title = [parsedJSON objectForKey:@"Name"];
	comic.startUrl = [parsedJSON objectForKey:@"StartURL"];
	comic.comicPattern = [parsedJSON objectForKey:@"ImagePattern"];
	comic.prevComicPattern = [parsedJSON objectForKey:@"PrevComicPattern"];
	comic.nextComicPattern = [parsedJSON objectForKey:@"NextComicPattern"];
	comic.altTextPattern = [parsedJSON objectForKey:@"AltTextPattern"];
	comic.titlePattern = [parsedJSON objectForKey:@"TitlePattern"];
	comic.randomURL = [parsedJSON objectForKey:@"RandomLink"];
	comic.randomComicPattern = [parsedJSON objectForKey:@"RandomComicPattern"];
	comic.basePrevNextURL = [parsedJSON objectForKey:@"BasePrevNextURL"];
	if (comic.basePrevNextURL == nil)
		comic.basePrevNextURL = @"";
	comic.baseComicURL = [parsedJSON objectForKey:@"BaseComicURL"];
	if (comic.baseComicURL == nil)
		comic.baseComicURL = @"";
	
	if (startUrl != nil)
		comic.startUrl = [startUrl retain];
	if (title != nil)
		comic.title = [title retain];
	
	NSLog(@"ADDING COMIC: %@", comic.title);
	
	[comics addObject:comic];
}

@end

