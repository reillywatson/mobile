//
//  DetailsViewController.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-23.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "DetailsViewController.h"

@implementation DetailsViewController

@synthesize webView=_webView;

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
	_opQueue = [NSOperationQueue new];
	self.title = @"Schedule Details";
	return self;
}

-(void)dealloc {
	[super dealloc];
	[_opQueue release];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations.
    return YES;
}


-(void)setSchedule:(Schedule *)schedule {
	ScheduleDetailsOperation *op = [[ScheduleDetailsOperation alloc] initWithSchedule:schedule delegate:self];
	[_opQueue addOperation:op];
	[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
}

-(void)detailsError:(NSError *)error {
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"There was an error retrieving data, please check your Internet connection." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
	[alert show];
}

-(void)detailsReady:(NSArray *)details {
	NSString *table = @"";
	for (ScheduleDetails *detail in details) {
		table = [table stringByAppendingFormat:@"<tr><td>%@</td><td>%@</td><td>%@</td></tr>", detail->cityName, detail->arrives, detail->departs];
	}
	NSString *html = [NSString stringWithFormat:@"<html><head></head><body><table><tr><td>LOCATION</td><td>ARRIVES</td><td>DEPARTS</td></tr>%@</table></html>", table];
	[self.webView loadHTMLString:html baseURL:nil];
	[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
}

@end
