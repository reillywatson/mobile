//
//  Schedule.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Schedule.h"


@implementation Schedule

-(id)initWithScheduleID:(NSString *)ascheduleID carrier:(NSString *)acarrier departureTime:(NSString *)adepartureTime arrivalTime:(NSString *)aarrivalTime duration:(NSString *)aduration numStops:(NSString *)anumStops detailsArgs:(NSString *)adetailsargs isCanadian:(BOOL)aisCanadian {
	self = [super init];
	scheduleID = [ascheduleID retain];
	carrier = [acarrier retain];
	departureTime =[adepartureTime retain];
	arrivalTime = [aarrivalTime retain];
	tripDuration = [aduration retain];
	numStops = [anumStops retain];
	detailsArgs = [adetailsargs retain];
	isCanadian = aisCanadian;
	return self;
}

-(void)dealloc {
	[super dealloc];
	[scheduleID release];
	[carrier release];
	[departureTime release];
	[arrivalTime release];
	[tripDuration release];
	[numStops release];
	[detailsArgs release];
}

@end
