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

// nag once a week!
static NSTimeInterval MIN_NAG_DELAY = 60 * 60 * 24 * 7;

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	if (buttonIndex == 1) {
		[[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://itunes.apple.com/us/app/comic-strips-read-300-daily/"]];
	}
}

-(void)showNagScreen {
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Upgrade" message:@"Do you want to get the full version, with 300+ comics?"
												   delegate:self cancelButtonTitle:@"No thanks" otherButtonTitles:@"Sure!", nil];
	[alert show];
	[alert release];
}

-(void)handleNagging {
	NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
	NSDate *date = [defaults objectForKey:@"lastNagScreen"];
	NSDate *now = [NSDate date];
	NSLog(@"%@ %@", date, now);
	if (date == nil) {
		[defaults setObject:now forKey:@"lastNagScreen"];
	}
	else if ([now timeIntervalSinceDate:date] > MIN_NAG_DELAY) {
		[defaults setObject:now forKey:@"lastNagScreen"];
		[self showNagScreen];
	}
}

-(void)viewWillAppear:(BOOL)animated {
#ifdef FREE_VERSION
	NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
	if ([defaults boolForKey:@"registered"] == NO) {
		[self handleNagging];
	}
#endif
	[favourites removeAllObjects];
	NSDictionary *favouriteTitles = [((ComicsAppDelegate *)[[UIApplication sharedApplication] delegate]) favourites];
	for (ComicInfo *comic in comics) {
		if ([favouriteTitles objectForKey:comic.title] != nil) {
			[favourites addObject:comic];
		}
	}
	[favourites sortUsingFunction:titleSort context:nil];
	[(UITableView *)self.view reloadData];
}

-(void)viewWillDisappear:(BOOL)animated {
	
}

#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
	return 40;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	if ([favourites count] == 0)
		return nil;
	return section == 0 ? @"Favorites" : @"Other Comics";
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

- (void)dealloc {
    [super dealloc];
	[comics release];
	[favourites release];
	[lastJSONPath release];
	[lastJSONObject release];
}

-(void)addComicsKingdomComicWithTitle:(NSString *)title basePrevNextURL:(NSString *)basePrevNextURL sundaysOnly:(BOOL)sundaysOnly {
	ComicInfo *info = [[ComicInfo new] autorelease];
	info.downloaderType = @"ComicsKingdom";
	info.basePrevNextURL = [basePrevNextURL retain];
	info.startUrl = @"calculateondemand";
	info.title = [title retain];
	info.sundaysOnly = sundaysOnly;
	info.useReferer = NO;
	[comics addObject:info];
}

-(void)addComicsKingdomComicWithTitle:(NSString *)title basePrevNextURL:(NSString *)basePrevNextURL {
	[self addComicsKingdomComicWithTitle:title basePrevNextURL:basePrevNextURL sundaysOnly:NO];
}

-(void)initComicsArray {
#ifdef FREE_VERSION
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Calvin and Hobbes" startURL:@"http://www.gocomics.com/calvinandhobbes"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"dilbert" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"FoxTrot" startURL:@"http://www.gocomics.com/foxtrot"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"pennyarcade" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"xkcd" ofType:@"json"]];
#else
	
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"achewood" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"alessonislearned" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"amazingsuperpowers" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"amya" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"archipelago" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"asofterworld" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"awkwardzombie" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"ballerinamafia" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"bearnuts" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"bluemilkspecial" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"bookofbiff" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"buttersafe" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"candi" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"capesnbabes" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"ctrlaltdel" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"ctrlaltdelsillies" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"cyanideandhappiness" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"danandmabsfurryadventures" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"devilbear" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"dieselsweeties" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"dilbert" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"dinosaurcomics" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"dominicdeegan" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"dreamscar" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"eeriecuties" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"elgoonishshive" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"evildiva" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"evilplan" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"exiern" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gaia" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"girlgenius" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"girlswithslingshots" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"goblins" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"grrlpower" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gunnerkriggcourt" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"harkavagrant" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"kinokofry" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"landscaper" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"leasticoulddo" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"lookingforgroup" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"magellan" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"megatokyo" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"menagea3" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"misfile" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"moe" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"mylifewithfel" ofType:@"json"]];

	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"nedroid" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"notavillain" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"pennyarcade" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"perrybiblefellowship" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"phdcomics" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"precocious" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"puck" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"pvp" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"questionablecontent" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"rivalangels" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"romanticallyapocalyptic" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"sequentialart" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"shadowbinders" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"sharkattack" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"shockwatson" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"sinfest" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"slightlydamned" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"smbc" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"spinnerette" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"thechallengesofzona" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"thedreamlandchronicles" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"theothergreymeat" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"twentyfirstcenturycoeds" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"twilightlady" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"twokinds" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"uptomynipples" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"userfriendly" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"vgcats" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"weshadows" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"wondermark" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"xkcd" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"zodiac" ofType:@"json"]];

	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"toothpastefordinner" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"marriedtothesea" ofType:@"json"]];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"nataliedee" ofType:@"json"]];
#if 0
//	[self addComicsKingdomComicWithTitle:@"Sherman's Lagoon" basePrevNextURL:@"http://content.comicskingdom.net/Shermans_Lagoon/Shermans_Lagoon."];
	[self addComicsKingdomComicWithTitle:@"Mandrake the Magician" basePrevNextURL:@"http://content.comicskingdom.net/Mandrake/Mandrake."];
	[self addComicsKingdomComicWithTitle:@"Arctic Circle" basePrevNextURL:@"http://content.comicskingdom.net/Arctic/Arctic."];
	[self addComicsKingdomComicWithTitle:@"DeFlocked" basePrevNextURL:@"http://content.comicskingdom.net/Deflocked/Deflocked."];
	[self addComicsKingdomComicWithTitle:@"Flash Gordon" basePrevNextURL:@"http://content.comicskingdom.net/Flash/Flash." sundaysOnly:YES];
	[self addComicsKingdomComicWithTitle:@"Hazel" basePrevNextURL:@"http://content.comicskingdom.net/Hazel/Hazel."];
	[self addComicsKingdomComicWithTitle:@"Heaven's Love Thrift Shop" basePrevNextURL:@"http://content.comicskingdom.net/Heavens/Heavens." sundaysOnly:YES];
	[self addComicsKingdomComicWithTitle:@"Henry" basePrevNextURL:@"http://content.comicskingdom.net/Henry/Henry."];
	[self addComicsKingdomComicWithTitle:@"The Katzenjammer Kids" basePrevNextURL:@"http://content.comicskingdom.net/Katzenjammer_Kids/Katzenjammer_Kids." sundaysOnly:YES];
	[self addComicsKingdomComicWithTitle:@"On the Fastrack" basePrevNextURL:@"http://content.comicskingdom.net/Fast_Track/Fast_Track."];
	[self addComicsKingdomComicWithTitle:@"The Pajama Diaries" basePrevNextURL:@"http://content.comicskingdom.net/Pajama/Pajama."];
	[self addComicsKingdomComicWithTitle:@"Popeye" basePrevNextURL:@"http://content.comicskingdom.net/Popeye/Popeye."];
	[self addComicsKingdomComicWithTitle:@"Prince Valiant" basePrevNextURL:@"http://content.comicskingdom.net/Prince_Valiant/Prince_Valiant." sundaysOnly:YES];
	[self addComicsKingdomComicWithTitle:@"Pros & Cons" basePrevNextURL:@"http://content.comicskingdom.net/Lawyer/Lawyer."];
	[self addComicsKingdomComicWithTitle:@"Retail" basePrevNextURL:@"http://content.comicskingdom.net/Retail/Retail."];
	[self addComicsKingdomComicWithTitle:@"Sam and Silo" basePrevNextURL:@"http://content.comicskingdom.net/Sam/Sam."];
	[self addComicsKingdomComicWithTitle:@"Tina's Groove" basePrevNextURL:@"http://content.comicskingdom.net/Tinas_Groove/Tinas_Groove."];
	[self addComicsKingdomComicWithTitle:@"Todd the Dinosaur" basePrevNextURL:@"http://content.comicskingdom.net/Todd/Todd."];	

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
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"The Boondocks" startURL:@"http://www.arcamax.com/boondocks/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Cathy" startURL:@"http://www.arcamax.com/cathy/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"arcamax" ofType:@"json"] title:@"Jerry King Cartoons" startURL:@"http://www.arcamax.com/humorcartoon/"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"2 Cows and a Chicken" startURL:@"http://www.gocomics.com/2cowsandachicken"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"9 Chickweed Lane" startURL:@"http://www.gocomics.com/9chickweedlane"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"9 to 5" startURL:@"http://www.gocomics.com/9to5"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Academia Waltz" startURL:@"http://www.gocomics.com/academiawaltz"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Adam@Home" startURL:@"http://www.gocomics.com/adamathome"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Agnes" startURL:@"http://www.gocomics.com/agnes"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Alley Oop" startURL:@"http://www.gocomics.com/alley-oop"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Andy Capp" startURL:@"http://www.gocomics.com/andycapp"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Animal Crackers" startURL:@"http://www.gocomics.com/animalcrackers"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Argyle Sweater" startURL:@"http://www.gocomics.com/theargylesweater"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Arlo and Janis" startURL:@"http://www.gocomics.com/arloandjanis"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"B.C." startURL:@"http://www.gocomics.com/bc"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Back in the Day" startURL:@"http://www.gocomics.com/backintheday"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Bad Reporter" startURL:@"http://www.gocomics.com/badreporter"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Baldo" startURL:@"http://www.gocomics.com/baldo"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Ballard Street" startURL:@"http://www.gocomics.com/ballardstreet"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Barkeater Lake" startURL:@"http://www.gocomics.com/barkeaterlake"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Barn" startURL:@"http://www.gocomics.com/thebarn"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Barney & Clyde" startURL:@"http://www.gocomics.com/barneyandclyde"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Basic Instructions" startURL:@"http://www.gocomics.com/basicinstructions"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Ben" startURL:@"http://www.gocomics.com/ben"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Betty" startURL:@"http://www.gocomics.com/betty"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Bewley" startURL:@"http://www.gocomics.com/bewley"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Big Nate" startURL:@"http://www.gocomics.com/bignate"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Big Picture" startURL:@"http://www.gocomics.com/thebigpicture"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Big Top" startURL:@"http://www.gocomics.com/bigtop"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Biographic" startURL:@"http://www.gocomics.com/biographic"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Birdbrains" startURL:@"http://www.gocomics.com/birdbrains"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Bliss" startURL:@"http://www.gocomics.com/bliss"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Bloom County" startURL:@"http://www.gocomics.com/bloomcounty"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Bo Nanas" startURL:@"http://www.gocomics.com/bonanas"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Bob the Squirrel" startURL:@"http://www.gocomics.com/bobthesquirrel"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Boomerangs" startURL:@"http://www.gocomics.com/boomerangs"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Born Loser" startURL:@"http://www.gocomics.com/the-born-loser"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Bottomliners" startURL:@"http://www.gocomics.com/bottomliners"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Bound and Gagged" startURL:@"http://www.gocomics.com/boundandgagged"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Brainwaves" startURL:@"http://www.gocomics.com/brainwaves"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Brevity" startURL:@"http://www.gocomics.com/brevity"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Brewster Rockit" startURL:@"http://www.gocomics.com/brewsterrockit"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Broom Hilda" startURL:@"http://www.gocomics.com/broomhilda"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Buckets" startURL:@"http://www.gocomics.com/thebuckets"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Cafe con Leche" startURL:@"http://www.gocomics.com/cafeconleche"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Calvin and Hobbes" startURL:@"http://www.gocomics.com/calvinandhobbes"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Candorville" startURL:@"http://www.gocomics.com/candorville"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"C'est la Vie" startURL:@"http://www.gocomics.com/cestlavie"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Chuckle Bros" startURL:@"http://www.gocomics.com/chucklebros"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Citizen Dog" startURL:@"http://www.gocomics.com/citizendog"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The City" startURL:@"http://www.gocomics.com/thecity"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Cleats" startURL:@"http://www.gocomics.com/cleats"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Close to Home" startURL:@"http://www.gocomics.com/closetohome"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Compu-toon" startURL:@"http://www.gocomics.com/compu-toon"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Cornered" startURL:@"http://www.gocomics.com/cornered"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Cow and Boy" startURL:@"http://www.gocomics.com/cowandboy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"CowTown" startURL:@"http://www.gocomics.com/cowtown"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Cul de Sac" startURL:@"http://www.gocomics.com/culdesac"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Daddy's Home" startURL:@"http://www.gocomics.com/daddyshome"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Dark Side of the Horse" startURL:@"http://www.gocomics.com/darksideofthehorse"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Deep Cover" startURL:@"http://www.gocomics.com/deepcover"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Diamond Lil" startURL:@"http://www.gocomics.com/diamondlil"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Dick Tracy" startURL:@"http://www.gocomics.com/dicktracy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Dinette Set" startURL:@"http://www.gocomics.com/dinetteset"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Dog Eat Doug" startURL:@"http://www.gocomics.com/dogeatdoug"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Dogs of C-Kennel" startURL:@"http://www.gocomics.com/dogsofckennel"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Domestic Abuse" startURL:@"http://www.gocomics.com/domesticabuse"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Doodles" startURL:@"http://www.gocomics.com/doodles"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Doonesbury" startURL:@"http://www.gocomics.com/doonesbury"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Doozies" startURL:@"http://www.gocomics.com/thedoozies"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Drabble" startURL:@"http://www.gocomics.com/drabble"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Dude and Dude" startURL:@"http://www.gocomics.com/dudedude"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Duplex" startURL:@"http://www.gocomics.com/duplex"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Eek!" startURL:@"http://www.gocomics.com/eek"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Elderberries" startURL:@"http://www.gocomics.com/theelderberries"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Endtown" startURL:@"http://www.gocomics.com/endtown"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"F Minus" startURL:@"http://www.gocomics.com/fminus"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Family Tree" startURL:@"http://www.gocomics.com/familytree"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Ferd'nand" startURL:@"http://www.gocomics.com/ferdnand"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Flo and Friends" startURL:@"http://www.gocomics.com/floandfriends"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Flying McCoys" startURL:@"http://www.gocomics.com/theflyingmccoys"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"For Better or For Worse" startURL:@"http://www.gocomics.com/forbetterorforworse"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Fort Knox" startURL:@"http://www.gocomics.com/fortknox"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"FoxTrot" startURL:@"http://www.gocomics.com/foxtrot"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"FoxTrot Classics" startURL:@"http://www.gocomics.com/foxtrotclassics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Frank & Ernest" startURL:@"http://www.gocomics.com/frankandernest"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Frazz" startURL:@"http://www.gocomics.com/frazz"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Fred Basset" startURL:@"http://www.gocomics.com/fredbasset"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Free Range" startURL:@"http://www.gocomics.com/freerange"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Freshly Squeezed" startURL:@"http://www.gocomics.com/freshlysqueezed"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Frog Applause" startURL:@"http://www.gocomics.com/frogapplause"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Fusco Brothers" startURL:@"http://www.gocomics.com/thefuscobrothers"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Garfield" startURL:@"http://www.gocomics.com/garfield"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Garfield Minus Garfield" startURL:@"http://www.gocomics.com/garfieldminusgarfield"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Gasoline Alley" startURL:@"http://www.gocomics.com/gasolinealley"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Get a Life" startURL:@"http://www.gocomics.com/getalife"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Get Fuzzy" startURL:@"http://www.gocomics.com/getfuzzy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Gil Thorp" startURL:@"http://www.gocomics.com/gilthorp"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Ginger Meggs" startURL:@"http://www.gocomics.com/gingermeggs"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Girls & Sports" startURL:@"http://www.gocomics.com/girlsandsports"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Graffiti" startURL:@"http://www.gocomics.com/graffiti"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Grand Avenue" startURL:@"http://www.gocomics.com/grand-avenue"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Grizzwells" startURL:@"http://www.gocomics.com/thegrizzwells"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Haiku Ewe" startURL:@"http://www.gocomics.com/haikuewe"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Heart of the City" startURL:@"http://www.gocomics.com/heartofthecity"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Heathcliff" startURL:@"http://www.gocomics.com/heathcliff"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Herb and Jamaal" startURL:@"http://www.gocomics.com/herbandjamaal"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Herman" startURL:@"http://www.gocomics.com/herman"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Home and Away" startURL:@"http://www.gocomics.com/homeandaway"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Hubert and Abby" startURL:@"http://www.gocomics.com/hubertandabby"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Imagine This" startURL:@"http://www.gocomics.com/imaginethis"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"In the Bleachers" startURL:@"http://www.gocomics.com/inthebleachers"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"In the Sticks" startURL:@"http://www.gocomics.com/inthesticks"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Incidental Comics" startURL:@"http://www.gocomics.com/incidentalcomics"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Ink Pen" startURL:@"http://www.gocomics.com/inkpen"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"It's All About You" startURL:@"http://www.gocomics.com/itsallaboutyou"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Jane's World" startURL:@"http://www.gocomics.com/janesworld"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Jim's Journal" startURL:@"http://www.gocomics.com/jimsjournal"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Joe Vanilla" startURL:@"http://www.gocomics.com/joevanilla"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Jump Start" startURL:@"http://www.gocomics.com/jumpstart"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"KidSpot" startURL:@"http://www.gocomics.com/kidspot"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Kit 'N' Carlyle" startURL:@"http://www.gocomics.com/kitandcarlyle"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Knight Life" startURL:@"http://www.gocomics.com/theknightlife"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"La Cucaracha" startURL:@"http://www.gocomics.com/lacucaracha"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Last Kiss" startURL:@"http://www.gocomics.com/lastkiss"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The LeftyBosco Picture Show" startURL:@"http://www.gocomics.com/leftyboscopictureshow"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Legend of Bill" startURL:@"http://www.gocomics.com/legendofbill"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Liberty Meadows" startURL:@"http://www.gocomics.com/libertymeadows"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Lio" startURL:@"http://www.gocomics.com/lio"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Little Dog Lost" startURL:@"http://www.gocomics.com/littledoglost"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Lola" startURL:@"http://www.gocomics.com/lola"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Loose Parts" startURL:@"http://www.gocomics.com/looseparts"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Lost Side of Suburbia" startURL:@"http://www.gocomics.com/lostsideofsuburbia"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Love Is..." startURL:@"http://www.gocomics.com/loveis"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Luann" startURL:@"http://www.gocomics.com/luann"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Lucky Cow" startURL:@"http://www.gocomics.com/luckycow"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Maintaining" startURL:@"http://www.gocomics.com/maintaining"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Marmaduke" startURL:@"http://www.gocomics.com/marmaduke"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Meaning of Lila" startURL:@"http://www.gocomics.com/meaningoflila"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Middletons" startURL:@"http://www.gocomics.com/themiddletons"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Minimum Security" startURL:@"http://www.gocomics.com/minimumsecurity"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Moderately Confused" startURL:@"http://www.gocomics.com/moderately-confused"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Molly and the Bear" startURL:@"http://www.gocomics.com/mollyandthebear"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Momma" startURL:@"http://www.gocomics.com/momma"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Monty" startURL:@"http://www.gocomics.com/monty"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Mutt & Jeff" startURL:@"http://www.gocomics.com/muttandjeff"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"My Cage" startURL:@"http://www.gocomics.com/mycage"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"MythTickle" startURL:@"http://www.gocomics.com/mythtickle"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Nancy" startURL:@"http://www.gocomics.com/nancy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Nest Heads" startURL:@"http://www.gocomics.com/nestheads"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"NEUROTICA" startURL:@"http://www.gocomics.com/neurotica"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"New Adventures of Queen Victoria" startURL:@"http://www.gocomics.com/thenewadventuresofqueenvictoria"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Non Sequitur" startURL:@"http://www.gocomics.com/nonsequitur"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Norm" startURL:@"http://www.gocomics.com/thenorm"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Off the Mark" startURL:@"http://www.gocomics.com/offthemark"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"On A Claire Day" startURL:@"http://www.gocomics.com/onaclaireday"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"One Big Happy" startURL:@"http://www.gocomics.com/onebighappy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"The Other Coast" startURL:@"http://www.gocomics.com/theothercoast"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Out of the Gene Pool Re-Runs" startURL:@"http://www.gocomics.com/outofthegenepool"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Over the Hedge" startURL:@"http://www.gocomics.com/overthehedge"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Overboard" startURL:@"http://www.gocomics.com/overboard"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"PC and Pixel" startURL:@"http://www.gocomics.com/pcandpixel"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Peanuts" startURL:@"http://www.gocomics.com/peanuts"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Pearls Before Swine" startURL:@"http://www.gocomics.com/pearlsbeforeswine"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Pibgorn" startURL:@"http://www.gocomics.com/pibgorn"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Pibgorn Sketches" startURL:@"http://www.gocomics.com/pibgornsketches"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Pickles" startURL:@"http://www.gocomics.com/pickles"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Pinkerton" startURL:@"http://www.gocomics.com/pinkerton"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Pluggers" startURL:@"http://www.gocomics.com/pluggers"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Pooch Caf" startURL:@"http://www.gocomics.com/poochcafe"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"PreTeena" startURL:@"http://www.gocomics.com/preteena"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Prickly City" startURL:@"http://www.gocomics.com/pricklycity"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Rabbits Against Magic" startURL:@"http://www.gocomics.com/rabbitsagainstmagic"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Real Life Adventures" startURL:@"http://www.gocomics.com/reallifeadventures"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Reality Check" startURL:@"http://www.gocomics.com/realitycheck"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Red and Rover" startURL:@"http://www.gocomics.com/redandrover"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Red Meat" startURL:@"http://www.gocomics.com/redmeat"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Reply All" startURL:@"http://www.gocomics.com/replyall"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Reynolds Unwrapped" startURL:@"http://www.gocomics.com/reynoldsunwrapped"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Rip Haywire" startURL:@"http://www.gocomics.com/riphaywire"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Ripley's Believe It or Not" startURL:@"http://www.gocomics.com/ripleysbelieveitornot"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Ronaldinho Gaucho" startURL:@"http://www.gocomics.com/ronaldinhogaucho"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Rose is Rose" startURL:@"http://www.gocomics.com/roseisrose"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Rubes" startURL:@"http://www.gocomics.com/rubes"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Rudy Park" startURL:@"http://www.gocomics.com/rudypark"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Scary Gary" startURL:@"http://www.gocomics.com/scarygary"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Shoe" startURL:@"http://www.gocomics.com/shoe"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Shoecabbage" startURL:@"http://www.gocomics.com/shoecabbage"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Skin Horse" startURL:@"http://www.gocomics.com/skinhorse"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Slowpoke" startURL:@"http://www.gocomics.com/slowpoke"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Soup to Nutz" startURL:@"http://www.gocomics.com/soup-to-nutz"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Speed Bump" startURL:@"http://www.gocomics.com/speedbump"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Stone Soup" startURL:@"http://www.gocomics.com/stonesoup"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Strange Brew" startURL:@"http://www.gocomics.com/strangebrew"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Sylvia" startURL:@"http://www.gocomics.com/sylvia"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Tank McNamara" startURL:@"http://www.gocomics.com/tankmcnamara"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Tarzan" startURL:@"http://www.gocomics.com/tarzan"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Thatababy" startURL:@"http://www.gocomics.com/thatababy"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"(th)ink" startURL:@"http://www.gocomics.com/think"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Tiny Sepuku" startURL:@"http://www.gocomics.com/tinysepuku"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"TOBY" startURL:@"http://www.gocomics.com/toby"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Today's Dogg" startURL:@"http://www.gocomics.com/todays-dogg"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Tom the Dancing Bug" startURL:@"http://www.gocomics.com/tomthedancingbug"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Too Much Coffee Man" startURL:@"http://www.gocomics.com/toomuchcoffeeman"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Unstrange Phenomena" startURL:@"http://www.gocomics.com/unstrange-phenomena"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Watch Your Head" startURL:@"http://www.gocomics.com/watchyourhead"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Wizard of Id" startURL:@"http://www.gocomics.com/wizardofid"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Working Daze" startURL:@"http://www.gocomics.com/working-daze"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Working It Out" startURL:@"http://www.gocomics.com/workingitout"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"W.T. Duck" startURL:@"http://www.gocomics.com/wtduck"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Yenny" startURL:@"http://www.gocomics.com/yenny"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Zack Hill" startURL:@"http://www.gocomics.com/zackhill"];
	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"gocomics" ofType:@"json"] title:@"Ziggy" startURL:@"http://www.gocomics.com/ziggy"];
#endif
	// looks like the Houston Chronicle has switched to a JavaScript-based solution, making it much harder
//	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Apartment 3-G" startURL:@"http://www.chron.com/entertainment/comics-games/comic/Apartment-3-G/"];
//	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Between Friends" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=143&page=1&cpp=1&v=3.0&quality=high"];
//	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Crock" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=106&page=1&cpp=1&v=3.0&quality=high"];
//	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Mark Trail" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=144&page=1&cpp=1&v=3.0&quality=high"];
//	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Redeye" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=126&page=1&cpp=1&v=3.0&quality=high"];
//	[self addComicWithJSON:[[NSBundle mainBundle] pathForResource:@"houstonchronicle" ofType:@"json"] title:@"Tiger" startURL:@"http://www.chron.com/apps/comics/buildcp.mpl?c=132&page=1&cpp=1&v=3.0&quality=high"];
#endif
	[comics sortUsingFunction:titleSort context:nil];
	for (ComicInfo *comic in comics) {
		NSLog(@"%@", comic.title);
	}
	NSLog(@"%d comics in list", [comics count]);
}

-(ComicInfo *)comicFromJSON:(NSString *)jsonFile {
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
	comic.downloaderType = [parsedJSON objectForKey:@"DownloaderType"];
	comic.startUrl = [parsedJSON objectForKey:@"StartURL"];
	comic.comicPattern = [parsedJSON objectForKey:@"ImagePattern"];
	comic.prevComicPattern = [parsedJSON objectForKey:@"PrevComicPattern"];
	comic.nextComicPattern = [parsedJSON objectForKey:@"NextComicPattern"];
	comic.altTextPattern = [parsedJSON objectForKey:@"AltTextPattern"];
	comic.titlePattern = [parsedJSON objectForKey:@"TitlePattern"];
	comic.randomURL = [parsedJSON objectForKey:@"RandomLink"];
	comic.randomComicPattern = [parsedJSON objectForKey:@"RandomComicPattern"];
	comic.baseRandomComicURL = [parsedJSON objectForKey:@"BaseRandomComicURL"];
	if (comic.baseRandomComicURL == nil)
		comic.baseRandomComicURL = @"";
	comic.basePrevNextURL = [parsedJSON objectForKey:@"BasePrevNextURL"];
	if (comic.basePrevNextURL == nil)
		comic.basePrevNextURL = @"";
	comic.baseComicURL = [parsedJSON objectForKey:@"BaseComicURL"];
	if (comic.baseComicURL == nil)
		comic.baseComicURL = @"";
	if (comic.title != nil)
		NSLog(@"ADDING COMIC: %@", comic.title);
	return comic;
}

-(void)addComicWithJSON:(NSString *)jsonFile {
	[comics addObject:[self comicFromJSON:jsonFile]];
}


-(void)addComicWithJSON:(NSString *)jsonFile title:(NSString *)title startURL: (NSString *)startUrl {
	ComicInfo *comic = [self comicFromJSON:jsonFile];
	if (startUrl != nil)
		comic.startUrl = [startUrl retain];
	if (title != nil) {
		comic.title = [title retain];
		NSLog(@"ADDING COMIC: %@", comic.title);
	}
	
	[comics addObject:comic];
}


@end

