//
//  ScheduleDetailsOperation.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleDetailsOperation.h"
#import "RegexKitLite.h"
#import "ScheduleDetails.h"

@implementation ScheduleDetailsOperation

-(id)initWithSchedule:(Schedule *)schedule delegate:(NSObject<ScheduleDetailsDelegate> *)adelegate {
	self = [super init];
	_schedule = [schedule retain];
	delegate = adelegate;
	return self;
}

-(void)dealloc {
	[super dealloc];
	[_schedule release];
}

-(void)main {
	
	NSString *url = [NSString stringWithFormat:@"http://www.greyhound.ca/home/ticketcenter/en/%@", _schedule->detailsArgs];
//	url = @"http://www.greyhound.ca/home/ticketcenter/en/ScheduleDetails.asp?ScheduleIndex=7&CS1=3473264952460496:Sock&ID=100706615"
	NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
	NSLog(@"HEY, MY REQUEST: %@", request);
	NSError *error =nil;
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
//	NSLog(@"RESPONSE FROM SERVER: %@", response);
	if (error != nil) {
		NSLog(@"DETAILS ERROR: %@", error);
		return;
	}
	NSString *stopRegex = @"<tr>[^<]*?<!--Location Cell-->(.*?)</tr>";
	NSString *cellRegex = @"<td[^>]*?>(.*?)</td>";
	NSArray *stops = [response arrayOfCaptureComponentsMatchedByRegex:stopRegex options:RKLDotAll range:NSMakeRange(0, [response length]) error:nil];
	NSMutableArray *detailsList = [[NSMutableArray new] autorelease];
	for (int i = 0; i < [stops count]; i++) {
		NSString *stopDetails = [[stops objectAtIndex:i] objectAtIndex:1];
//		NSLog(@"STOP: %@", stopDetails);
		NSArray *cells = [stopDetails arrayOfCaptureComponentsMatchedByRegex:cellRegex options:RKLDotAll range:NSMakeRange(0, [stopDetails length]) error:nil];
		NSString *cityName = [[cells objectAtIndex:0] objectAtIndex:1];
		cityName = [cityName stringByReplacingOccurrencesOfString:@"<b>" withString:@""];
		cityName = [cityName stringByReplacingOccurrencesOfString:@"</b>" withString:@""];
		cityName = [cityName stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
		NSString *arrives = [[cells objectAtIndex:1] objectAtIndex:1];
		NSString *departs = [[cells objectAtIndex:2] objectAtIndex:1];
		NSString *layover = [[cells objectAtIndex:3] objectAtIndex:1];
		NSString *company = [[cells objectAtIndex:4] objectAtIndex:1];
		NSString *schedule = [[cells objectAtIndex:5] objectAtIndex:1];
		NSString *remarks = [[cells objectAtIndex:6] objectAtIndex:1];
		NSLog(@"City: %@ Arrives: %@ Departs: %@ Layover: %@ Company: %@ Schedule:%@ Remarks: %@", cityName, arrives, departs, layover, company, schedule, remarks);
		ScheduleDetails *details = [[ScheduleDetails alloc] initWithCity:cityName arrives:arrives departs:departs layover:layover company:company schedule:schedule remarks:remarks];
		[detailsList addObject:details];
	}
	[delegate performSelectorOnMainThread:@selector(detailsReady:) withObject:detailsList waitUntilDone:NO];
}

@end
