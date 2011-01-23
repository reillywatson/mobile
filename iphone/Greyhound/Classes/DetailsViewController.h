//
//  DetailsViewController.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-23.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Schedule.h"
#import "ScheduleDetailsOperation.h"

@interface DetailsViewController : UITableViewController<ScheduleDetailsDelegate> {
	NSOperationQueue *_opQueue;
	NSMutableArray *_details;
}

-(void)setSchedule:(Schedule *)schedule;

@end
