//
//  ScheduleViewController.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleViewController.h"
#import "Schedule.h"

@implementation ScheduleViewController

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
	_opQueue = [NSOperationQueue new];
	schedules = [NSMutableArray new];
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
//	[_opQueue addOperation:[[ScheduleListOperation alloc] initWith
}

-(void)gotConfirmationCode:(NSString *)code {
	[_opQueue addOperation:[[ScheduleListOperation alloc] initWithStartLocation:_start endLocation:_end requestID:code date:_date delegate:self]];
}

-(void)confirmationError:(NSError *)error {
	NSLog(@"Confirmation error: %@", error);
}

-(void)gotSchedules:(NSArray *)scheduleData {
	[schedules removeAllObjects];
	[schedules addObjectsFromArray:scheduleData];
	NSLog(@"HEY WE GOT EM %@", schedules);
	[self.tableView reloadData];
}

-(void)scheduleError:(NSError *)error {
}


#pragma mark -
#pragma mark Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return [schedules count];
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
    
	Schedule *schedule = [schedules objectAtIndex:indexPath.row];
	NSLog(@"DEPART: %@ ARRIVE: %@ DURATION: %@", schedule->departureTime, schedule->arrivalTime, schedule->tripDuration);
	[cell.textLabel setText:[NSString stringWithFormat:@"%@-%@ (%@)", schedule->departureTime, schedule->arrivalTime, schedule->tripDuration]];
    // Configure the cell...
    
    return cell;
}

#pragma mark -
#pragma mark Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // Navigation logic may go here. Create and push another view controller.
    /*
    <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
    // ...
    // Pass the selected object to the new view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
    [detailViewController release];
    */
}



@end

