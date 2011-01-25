//
//  ScheduleDetails.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-23.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleDetails.h"


@implementation ScheduleDetails

-(id)initWithCity:(NSString *)acityName arrives:(NSString *)aarrives departs:(NSString *)adeparts layover:(NSString *)alayover company:(NSString *)acompany schedule:(NSString *)aschedule remarks:(NSString *)aremarks {
	self = [super init];
	cityName = [acityName retain];
	arrives = [aarrives retain];
	departs = [adeparts retain];
	layover = [alayover retain];
	company = [acompany retain];
	schedule = [aschedule retain];
	remarks = [aremarks retain];
	return self;
}

-(void)dealloc {
	[super dealloc];
	[cityName release];
	[arrives release];
	[departs release];
	[layover release];
	[company release];
	[schedule release];
	[remarks release];
}

@end
