//
//  RootViewController.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "RootViewController.h"
#import "LocationsDataRetrievalOperation.h"
#import "Location.h"
#import "DatePickController.h"

@implementation RootViewController

@synthesize searchBar = _searchBar;
@synthesize startLocation = _startLocation;

#pragma mark -
#pragma mark View lifecycle


- (void)viewDidLoad {
    [super viewDidLoad];
	_opQueue = [NSOperationQueue new];
	itemList = [NSMutableArray new];
	_autoCompleteTimer = nil;
}

-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	if (_startLocation == nil) {
		[self setTitle:@"Select Start City"];
	}
	else {
		[self setTitle:@"Select Destination City"];
	}
	self.navigationItem.backBarButtonItem =
	[[UIBarButtonItem alloc] initWithTitle:@"Back"
									 style: UIBarButtonItemStyleBordered
									target:nil
									action:nil];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	return YES;
}


#pragma mark -
#pragma mark Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return [itemList count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	static NSString *kCellID = @"cellID";
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellID];
	if (cell == nil)
	{
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:kCellID] autorelease];
		cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	}
	
	NSString *city = ((Location *)[itemList objectAtIndex:indexPath.row])->name;
	cell.textLabel.text = city;
	return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	UIViewController *next = nil;
	Location *location = [itemList objectAtIndex:indexPath.row];
	if (self.startLocation != nil) {
		DatePickController *datePick = [[DatePickController alloc] initWithNibName:@"DatePickController" bundle:nil];
		[datePick setLocationStart:self.startLocation end:location];
//		ScheduleViewController *scheduleView = [[ScheduleViewController alloc] initWithNibName:@"ScheduleViewController" bundle:nil];
//		[scheduleView setLocationStart:self.startLocation end:location];
		next = datePick;
	}
	else {
		RootViewController *returnView = [[RootViewController alloc] initWithNibName:@"RootViewController" bundle:nil];
		returnView.startLocation = location;
		next = returnView;
	}
	
    [[self navigationController] pushViewController:next animated:YES];
    [next release];
}

#pragma mark -
#pragma mark UISearchDisplayController Delegate Methods

-(void)getLocations:(NSTimer *)timer {
	LocationsDataRetrievalOperation *op = [LocationsDataRetrievalOperation alloc];
	[op initWithDelegate:self searchText:[timer userInfo]];
	[_opQueue cancelAllOperations];
	[_opQueue addOperation:op];
	[UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
	_autoCompleteTimer = nil;
}

- (BOOL)searchDisplayController:(UISearchDisplayController *)controller shouldReloadTableForSearchString:(NSString *)searchString
{
	if (_autoCompleteTimer != nil) {
		[_autoCompleteTimer invalidate];
		_autoCompleteTimer = nil;
	}
	
	if ([searchString length] > 0) {
		_autoCompleteTimer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(getLocations:) userInfo:searchString repeats:NO];
	}
	return NO;
}

#pragma mark -
#pragma mark LocationsDataRetrieval Delegate Methods

-(void)locationsFound:(NSArray *)locations {
	[itemList removeAllObjects];
	[itemList addObjectsFromArray:locations];
	[self.searchDisplayController.searchResultsTableView reloadData];
	[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
}

-(void)locationsError:(NSError *)error {
	[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
	NSLog(@"error retrieving locations: %@", error);
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"There was an error retrieving data, please check your Internet connection." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
	[alert show];
}

- (void)viewDidUnload {
    // Relinquish ownership of anything that can be recreated in viewDidLoad or on demand.
    // For example: self.myOutlet = nil;
	[_opQueue release];
	_opQueue = nil;
	[itemList release];
	itemList = nil;
}


- (void)dealloc {
    [super dealloc];
}


@end

