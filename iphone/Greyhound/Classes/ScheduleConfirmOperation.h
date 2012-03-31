//
//  ScheduleConfirmOperation.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Location.h"

@protocol ScheduleConfirmDelegate

-(void)gotSchedules:(NSArray *)schedules;
-(void)confirmationError:(NSError *)error;

@end


@interface ScheduleConfirmOperation : NSOperation {
	id<ScheduleConfirmDelegate> delegate;
	Location *_start;
	Location *_end;
	NSDate *_date;
}

-(id)initWithStartLocation:(Location *)start endLocation:(Location *)end date:(NSDate *)date delegate:(id<ScheduleConfirmDelegate>)aDelegate;

@end
