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

@synthesize spinner, noButton, drunkButton, yesButton, webview;

-(id)initWithCoder:(NSCoder *)aDecoder {
	self = [super initWithCoder:aDecoder];
	opqueue = [NSOperationQueue new];
	[opqueue setMaxConcurrentOperationCount:1];
	items = [NSMutableArray new];
	return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	AdMobView *ad = [AdMobView requestAdWithDelegate:self]; // start a new ad request
    ad.frame = CGRectMake(0, 412, 320, 48); // set the frame, in this case at the bottom of the screen
    [self.view addSubview:ad]; // attach the ad to the view hierarchy; self.view is responsible for retaining the ad
	[yesButton addTarget:self action:@selector(loadNewItem:) forControlEvents:UIControlEventTouchUpInside];
	[noButton addTarget:self action:@selector(loadNewItem:) forControlEvents:UIControlEventTouchUpInside];
	[drunkButton addTarget:self action:@selector(loadNewItem:) forControlEvents:UIControlEventTouchUpInside];
	[self loadNewItem:nil];
}

-(void)loadNewItem:(HotItem *)currentItem {
	if ([[opqueue operations] count] == 0) {
		[spinner startAnimating];
		if (currentItem == nil)
			[opqueue addOperation:[[HotOrNotDownloadOperation alloc] initWithDelegate:self rateID:nil imageURL:nil]];
		else
			[opqueue addOperation:[[HotOrNotDownloadOperation alloc] initWithDelegate:self rateID:currentItem->rateID imageURL:currentItem->imageURL]];
	}
}

- (void)webViewDidFinishLoad:(UIWebView *)webView {
	[spinner stopAnimating];
}

// TODO: remove this when fetching ads works properly (seems to fail at the moment)
-(NSArray *)testDevices {
	return [NSArray arrayWithObject:ADMOB_SIMULATOR_ID];
}

- (void)dealloc {
    [super dealloc];
	[opqueue release];
	[items release];
}

- (NSString *)publisherId {
	return @"a14bf8b7dde92c0";
}

- (UIViewController *)currentViewController {
	return self;
}

-(void)itemReady:(HotItem *)item {
	[webview loadImageUrlWithAutoZoom:item->imageURL];
}

-(void)requestFailedWithError:(NSError *)error {
}


@end
