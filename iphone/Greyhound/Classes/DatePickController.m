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

-(void)viewDidLoad {
	self.datePicker.date = [NSDate date];	
}

-(void)viewDidAppear:(BOOL)animated {
	UIBarButtonItem *save = [[UIBarButtonItem alloc] initWithTitle:@"Go" style:UIBarButtonItemStyleDone target:self action:@selector(save)];
	
	[[[[self navigationController] navigationBar] topItem] setRightBarButtonItem:save];
	
}

-(void)setLocationStart:(Location *)start end:(Location *)end {
	_start = start;
	_end = end;
}


-(IBAction)save {
	ScheduleViewController *scheduleView = [[ScheduleViewController alloc] initWithNibName:@"ScheduleViewController" bundle:nil];
	[scheduleView setLocationStart:_start end:_end date:self.datePicker.date];
	[[self navigationController] pushViewController:scheduleView animated:YES];
}

-(BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation {
	return YES;
}

@end
