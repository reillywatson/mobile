//
//  DatePickController.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "DatePickController.h"
#import "ScheduleViewController.h"

@implementation DatePickController

@synthesize datePicker = _datePicker;

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
	_start = nil;
	_end = nil;
	return self;
}

-(void)viewDidAppear:(BOOL)animated {
	UIBarButtonItem *save = [[UIBarButtonItem alloc] initWithTitle:@"Save" style:UIBarButtonItemStyleDone target:self action:@selector(save)];
	
	[[[[self navigationController] navigationBar] topItem] setRightBarButtonItem:save];
	
}

-(void)setLocationStart:(Location *)start end:(Location *)end {
	_start = [start retain];
	_end = [end retain];
}

-(void)dealloc {
	[super dealloc];
	[_start release];
	[_end release];
}

-(IBAction)save {
	ScheduleViewController *scheduleView = [[ScheduleViewController alloc] initWithNibName:@"ScheduleViewController" bundle:nil];
	[scheduleView setLocationStart:_start end:_end date:self.datePicker.date];
	[[self navigationController] pushViewController:scheduleView animated:YES];
}

-(IBAction)cancel {
	[[self navigationController] popViewControllerAnimated:YES];
}

@end
