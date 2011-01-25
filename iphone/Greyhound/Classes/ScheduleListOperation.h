//
//  ScheduleListOperation.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Location.h"

@protocol ScheduleListDelegate
-(void)gotSchedules:(NSArray *)schedule;
-(void)scheduleError:(NSError *)error;
@end


@interface ScheduleListOperation : NSOperation {
	NSObject<ScheduleListDelegate> *delegate;
	Location *_start;
	Location *_end;
	NSDate *_date;
	NSString *_requestID;
}

-(id)initWithStartLocation:(Location *)start endLocation:(Location *)end requestID:(NSString *)requestID date:(NSDate *)date delegate:(NSObject<ScheduleListDelegate>*)aDelegate;

@end
