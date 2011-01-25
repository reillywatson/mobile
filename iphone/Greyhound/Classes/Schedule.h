//
//  Schedule.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Schedule : NSObject {
@public
	NSString *scheduleID;
	NSString *carrier;
	NSString *departureTime;
	NSString *arrivalTime;
	NSString *tripDuration;
	NSString *numStops;
	NSString *detailsArgs;
}

-(id)initWithScheduleID:(NSString *)scheduleID carrier:(NSString *)carrier departureTime:(NSString *)departureTime arrivalTime:(NSString *)arrivalTime
			   duration:(NSString *)duration numStops:(NSString *)numStops detailsArgs:(NSString *)detailsArgs;

@end
