//
//  ScheduleDetails.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-23.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface ScheduleDetails : NSObject {
@public
	NSString *cityName;
	NSString *arrives;
	NSString *departs;
	NSString *layover;
	NSString *company;
	NSString *schedule;
	NSString *remarks;
}

-(id)initWithCity:(NSString *)acityName arrives:(NSString *)aarrives departs:(NSString *)adeparts layover:(NSString *)alayover company:(NSString *)acompany schedule:(NSString *)aschedule remarks:(NSString *)aremarks;


@end
