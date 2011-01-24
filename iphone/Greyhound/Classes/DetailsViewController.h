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

@interface DetailsViewController : UIViewController<ScheduleDetailsDelegate> {
	NSOperationQueue *_opQueue;
	UIWebView *_webView;
}

@property (nonatomic, retain) IBOutlet UIWebView *webView;

-(void)setSchedule:(Schedule *)schedule;

@end
