//
//  URLResolver.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-25.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Location.h"
#import "Schedule.h"

@interface URLResolver : NSObject {

}
+(BOOL)isCanadianStart:(Location *)start end:(Location *)end;
+(NSString *)scheduleConfirmURLForStart:(Location *)start end:(Location *)end;
+(NSString *)scheduleDetailsURLForSchedule:(Schedule *)schedule;
+(NSString *)step2URLForStart:(Location *)start end:(Location *)end;
+(NSString *)locationsURLForText:(NSString *)text;
@end
