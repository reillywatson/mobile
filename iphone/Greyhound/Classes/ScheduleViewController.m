//
//  ScheduleViewController.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleViewController.h"
#import "Schedule.h"
#import "DetailsViewController.h"

@implementation ScheduleViewController

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
	_opQueue = [NSOperationQueue new];
	schedules = [NSMutableArray new];
	self.title = @"Schedules";
	return self;
}

-(void)dealloc {
	[super dealloc];
	[_opQueue release];
	[schedules release];
}

-(void)setLocationStart:(Location *)start end:(Location *)end date:(NSDate *)date {
	_start = [start retain];
	_end = [end retain];
	_date = [date retain];
	[_opQueue addOperation:[[ScheduleConfirmOperation alloc] initWithStartLocation:start endLocation:end date:_date delegate:self]];
	[UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
}

-(void)confirmationError:(NSError *)error {
	NSLog(@"Confirmation error: %@", error);
	[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"There was an error retrieving data, please check your Internet connection." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
	[alert show];
}

-(void)gotSchedules:(NSArray *)scheduleData {
	[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
	[schedules removeAllObjects];
	[schedules addObjectsFromArray:scheduleData];
	if ([scheduleData count] == 0) {
		NSDateFormatter *dateFormatter = [[NSDateFormatter new] autorelease];
		[dateFormatter setTimeStyle:NSDateFormatterNoStyle];
		[dateFormatter setDateStyle:NSDateFormatterMediumStyle];		
		NSString *message = [NSString stringWithFormat:@"No schedules from %@ to %@ for %@", _start->name, _end->name, [dateFormatter stringFromDate:_date]];
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No results found" message:message delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		[self.tableView setHidden:YES];
		return;
	}
	[self.tableView setHidden:NO];
	[self.tableView reloadData];
}

-(void)scheduleError:(NSError *)error {
	[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
}


#pragma mark -
#pragma mark Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [schedules count];
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier] autorelease];
    }
	
	Schedule *schedule = [schedules objectAtIndex:indexPath.row];
	NSString *depart = [schedule->departureTime substringToIndex:[schedule->departureTime rangeOfString:@"<"].location];// stringByReplacingOccurrencesOfString:@"a" withString:@"AM"] stringByReplacingOccurrencesOfString:@"p" withString:@"PM"];
	NSString *arrive = [schedule->arrivalTime substringToIndex:[schedule->arrivalTime rangeOfString:@"<"].location];// stringByReplacingOccurrencesOfString:@"a" withString:@"AM"] stringByReplacingOccurrencesOfString:@"p" withString:@"PM"];
	NSString *duration = schedule->tripDuration;
	//dd,hh:mm
/*	int days = [[schedule->tripDuration substringToIndex:2] intValue];
	int hours = [[schedule->tripDuration substringWithRange:NSMakeRange(3, 2)] intValue];
	int minutes = [[schedule->tripDuration substringWithRange:NSMakeRange(6, 2)] intValue];
	if (days > 0) {
		duration = [NSString stringWithFormat:@"%dd %dh %dm",days,hours,minutes];
	}
	else {
		duration = [NSString stringWithFormat:@"%dh %dm",hours,minutes];
	}*/
	[cell.textLabel setText:[NSString stringWithFormat:@"%@ - %@", depart, arrive]];
	[cell.detailTextLabel setText:duration];
		
    return cell;
}

#pragma mark -
#pragma mark Table view delegate


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	DetailsViewController *details = [[DetailsViewController alloc] initWithNibName:@"DetailsViewController" bundle:nil];
	[details setSchedule:[schedules objectAtIndex:indexPath.row]];
	[[self navigationController] pushViewController:details animated:YES];
}

-(BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation {
	return YES;
}


@end

