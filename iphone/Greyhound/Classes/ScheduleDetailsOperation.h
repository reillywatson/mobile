//
//  ScheduleDetailsOperation.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Schedule.h"
#import "ScheduleDetails.h"

@protocol ScheduleDetailsDelegate

-(void)detailsReady:(NSArray *)details;
-(void)detailsError:(NSError *)error;

@end


@interface ScheduleDetailsOperation : NSOperation {
	Schedule *_schedule;
	NSObject<ScheduleDetailsDelegate> *delegate;
}


-(id)initWithSchedule:(Schedule *)schedule delegate:(NSObject<ScheduleDetailsDelegate> *)adelegate;

@end
