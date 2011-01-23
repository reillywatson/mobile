//
//  DatePickController.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Location.h"

@interface DatePickController : UIViewController {
	UIDatePicker *_datePicker;
	Location *_start;
	Location *_end;
}

@property (nonatomic, retain) IBOutlet UIDatePicker* datePicker;

-(IBAction)save;

-(void)setLocationStart:(Location *)start end:(Location *)end;


@end
