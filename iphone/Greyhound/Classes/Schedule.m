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
	scheduleID = ascheduleID;
	carrier = acarrier;
	departureTime =adepartureTime;
	arrivalTime = aarrivalTime;
	tripDuration = aduration;
	numStops = anumStops;
	detailsArgs = adetailsargs;
	isCanadian = aisCanadian;
	return self;
}


@end
