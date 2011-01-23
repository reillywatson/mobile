//
//  ScheduleViewController.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Location.h"
#import "ScheduleConfirmOperation.h"
#import "ScheduleListOperation.h"
#import "Schedule.h"

@interface ScheduleViewController : UITableViewController<ScheduleConfirmDelegate, ScheduleListDelegate> {
	NSMutableArray *schedules;
	NSOperationQueue *_opQueue;
	Location *_start;
	Location *_end;
	NSDate *_date;
}

-(void)setLocationStart:(Location *)start end:(Location *)end date:(NSDate *)date;

@end
