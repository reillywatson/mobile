//
//  HitItViewController.m
//  HitIt
//
//  Created by Reilly Watson on 10-05-23.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "HitItViewController.h"
#import "AdMobView.h"

@interface HitItViewController (Private)
-(void)loadNewItem:(HotItem *)currentItem;
@end

@implementation HitItViewController

@synthesize spinner, noButton, drunkButton, yesButton, imageview, resultsView, resultImageView, resultImageIcon, resultNumRatings;

-(id)initWithCoder:(NSCoder *)aDecoder {
	self = [super initWithCoder:aDecoder];
	opqueue = [NSOperationQueue new];
	[opqueue setMaxConcurrentOperationCount:1];
	items = [NSMutableArray new];
	lastItem = nil;
	currentItem = nil;
	return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	[self.view sendSubviewToBack:[imageview superview]];
	AdMobView *ad = [AdMobView requestAdWithDelegate:self]; // start a new ad request
    ad.frame = CGRectMake(0, 412, 320, 48); // set the frame, in this case at the bottom of the screen
    [self.view addSubview:ad]; // attach the ad to the view hierarchy; self.view is responsible for retaining the ad
	[yesButton addTarget:self action:@selector(loadNewItem:) forControlEvents:UIControlEventTouchUpInside];
	[noButton addTarget:self action:@selector(loadNewItem:) forControlEvents:UIControlEventTouchUpInside];
	[drunkButton addTarget:self action:@selector(loadNewItem:) forControlEvents:UIControlEventTouchUpInside];
	[self loadNewItem:nil];
}

-(void)loadNewItem:(HotItem *)prevItem {
	if ([[opqueue operations] count] == 0) {
		[spinner startAnimating];
		if (currentItem == nil)
			[opqueue addOperation:[[HotOrNotDownloadOperation alloc] initWithDelegate:self rateID:nil imageURL:nil]];
		else
			[opqueue addOperation:[[HotOrNotDownloadOperation alloc] initWithDelegate:self rateID:currentItem->rateID imageURL:currentItem->imageURL]];
	}
}

- (void)dealloc {
    [super dealloc];
	[opqueue release];
	[items release];
	[lastItem release];
	[currentItem release];
}

- (NSString *)publisherId {
	return @"a14bf8b7dde92c0";
}

- (UIViewController *)currentViewController {
	return self;
}

-(void)showPrevItem {
	NSLog(@"SHOWING PREV ITEM WITH %@ RATINGS", currentItem->resultTotals);
	[resultsView setHidden:NO];
	[resultImageView setImage:lastItem->image];
	[resultImageIcon setImage:[UIImage imageNamed:@"white_thumbs_up.png"]];
	[resultNumRatings setText:[NSString stringWithFormat:@"%@ votes", currentItem->resultTotals]];
}

-(void)itemReady:(HotItem *)item {
	if (item->image != nil && item->image.size.height > 100) {
		[lastItem release];
		lastItem = [currentItem retain];
		[imageview setImage:item->image];
		[spinner stopAnimating];
		[currentItem release];
		currentItem = [item retain];
		if (lastItem != nil && item->resultTotals != nil) {
			[self showPrevItem];
		}
		else {
			[resultsView setHidden:YES];
		}
	}
	else {
		[self loadNewItem:nil];
	}
}

-(void)requestFailedWithError:(NSError *)error {
}


@end
