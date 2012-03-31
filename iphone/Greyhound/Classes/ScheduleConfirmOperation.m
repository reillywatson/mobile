//
//  ScheduleConfirmOperation.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleConfirmOperation.h"
#import "RegexKitLite.h"
#import "URLResolver.h"
#import "SBJSON.h"
#import "LocationsDataRetrievalOperation.h"

@implementation ScheduleConfirmOperation

-(id)initWithStartLocation:(Location *)start endLocation:(Location *)end date:(NSDate *)date delegate:(id <ScheduleConfirmDelegate>)aDelegate {
	self = [super init];
	_start = [start retain];
	_end = [end retain];
	_date = [date retain];
	delegate = aDelegate;
	return self;
}

-(void)dealloc {
	[super dealloc];
	[_start release];
	[_end release];
	[_date release];
}

-(void)main {
	NSString *url = [URLResolver scheduleConfirmURLForStart:_start end:_end];
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
	[request setHTTPMethod:@"POST"];
	
	NSDateFormatter *formatter = [[NSDateFormatter new] autorelease];
	[formatter setDateFormat:@"dd MMMM yyyy"];
	NSString *date = [formatter stringFromDate:_date];
	
	if ([URLResolver isCanadianStart:_start end:_end]) {
		// apparently there are incompatible location IDs between the Canadian and US sites now.  Gross!
		NSArray *startLocs = [[[LocationsDataRetrievalOperation new] autorelease] locationsForString:_start->name];
		NSArray *endLocs = [[[LocationsDataRetrievalOperation new] autorelease] locationsForString:_end->name];
		if ([startLocs count] > 0) {
			[_start release];
			_start = [[startLocs objectAtIndex:0] retain];
		}
		if ([endLocs count] > 0) {
			[_end release];
			_end = [[endLocs objectAtIndex:0] retain];
		}
	}
	
	NSString *origin = _start->locationId; //@"126300|Toronto/ON";
	NSString *dest = _end->locationId; //@"123661|London/ON";

	NSLog(@"ORIGIN: %@", origin);
	NSLog(@"DEST: %@", dest);
	
	NSLog(@"DATE: %@", date);
	
	NSString *params = [NSString stringWithFormat:@"{\"request\":{\"__type\":\"Greyhound.Website.DataObjects.ClientSearchRequest\",\"Mode\":0,\"Origin\":\"%@\",\"Destination\":\"%@\",\"Departs\":\"%@\"}}", origin, dest, date];
	
	[request setValue:@"application/json; charset=utf-8" forHTTPHeaderField:@"Content-Type"];

	[request setHTTPBody:[params dataUsingEncoding:NSUTF8StringEncoding]];
	NSError *error = nil;
	NSLog(@"HEY, MY REQUEST: %@", request);
	NSLog(@"PARAMS: %@", params);
	NSHTTPURLResponse *response;
	[NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
	NSString *cookie = [[response allHeaderFields] valueForKey:@"Set-Cookie"];
	
	request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:[URLResolver step2URLForStart:_start end:_end]]];
	[request setValue:cookie forHTTPHeaderField:@"Cookie"];
	NSLog(@"HEY, MY REQUEST: %@", request);
	
	NSString *responseStr = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error] encoding:NSUTF8StringEncoding];
	
	
	
	// this is the key to the whole response!
//	<input type="hidden" name="RequestID" value="100657914" />
	if (error != nil) {
		NSLog(@"OH SHIT DAWG, GETTING LOCATION DATA FAILED: %@", error);
		[delegate confirmationError:error];
	}
	else {
		NSArray *components = [responseStr captureComponentsMatchedByRegex:@"FareFinder.Step2.Initialize\\s*\\((.*?), false\\);" options:RKLDotAll range:NSMakeRange(0, [responseStr length]) error:&error];
		if (error != nil) {
			NSLog(@"REGEX ERROR: %@", error);
		}
		NSMutableArray *schedules = [NSMutableArray new];
		if ([components count] > 1) {
			NSLog(@"GOT IT: %@", [components objectAtIndex:1]);
			SBJSON *jsonEngine = [[SBJSON new] autorelease];
			NSDictionary *json = [jsonEngine objectWithString:[components objectAtIndex:1]];
			NSArray *scheduleDatas = [json objectForKey:@"SchedulesDepart"];
			for (NSDictionary *scheduleData in scheduleDatas) {
				NSString *departureTime = [scheduleData objectForKey:@"DisplayDeparts"];
				NSString *arrivalTime = [scheduleData objectForKey:@"DisplayArrives"];
				NSString *duration = [scheduleData objectForKey:@"Time"];
				NSString *key = [scheduleData objectForKey:@"Key"]; // what's this?
				NSString *numStops = [scheduleData objectForKey:@"Transfers"];
				BOOL canadian = [URLResolver isCanadianStart:_start end:_end];
				Schedule *sched = [[Schedule alloc] initWithScheduleID:key carrier:nil departureTime:departureTime arrivalTime:arrivalTime duration:duration numStops:numStops detailsArgs:nil isCanadian:canadian];
				NSLog(@"Adding schedule: %@-%@", departureTime, arrivalTime);
				[schedules addObject:sched];
			}
		}
		else {
			NSLog(@"Oh shit, we got no results!");
		}
		[delegate performSelectorOnMainThread:@selector(gotSchedules:) withObject:schedules waitUntilDone:NO];
	}
//	NSLog(@"RESPONSE FROM SERVER: %@", responseStr);
}
@end
