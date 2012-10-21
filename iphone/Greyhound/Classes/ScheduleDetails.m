//
//  ScheduleDetails.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-23.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleDetails.h"


@implementation ScheduleDetails

-(id)initWithCity:(NSString *)acityName arrives:(NSString *)aarrives departs:(NSString *)adeparts layover:(NSString *)alayover company:(NSString *)acompany schedule:(NSString *)aschedule {
	self = [super init];
	cityName = acityName;
	arrives = aarrives;
	departs = adeparts;
	layover = alayover;
	company = acompany;
	schedule = aschedule;
	return self;
}


@end
