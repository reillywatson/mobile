//
//  URLResolver.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-25.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "URLResolver.h"
#import "Location.h"

@implementation URLResolver

+(BOOL)stateIsCanadian:(NSString *)state {
	if (state == nil)
		return NO;
	NSArray *canStates = [NSArray arrayWithObjects:@"BC",@"AB",@"SK",@"MB",@"ON",@"PQ",@"NB",@"NS",@"PE",@"NL",@"YT",@"NT",@"NU",nil];
	return [canStates containsObject:state];
}

+(NSString *)stateForLocation:(Location *)location {
	NSArray *startNameComponents = [location->name componentsSeparatedByString:@","];
	if ([startNameComponents count] != 2)
		return nil;
	return [[startNameComponents objectAtIndex:1] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
}

+(BOOL)isCanadianStart:(Location *)start end:(Location *)end {
	if ([URLResolver stateIsCanadian:[URLResolver stateForLocation:start]] && [URLResolver stateIsCanadian:[URLResolver stateForLocation:start]]) {
		return YES;
	}
	return NO;
}

+(NSString *)scheduleListURLForStart:(Location *)start end:(Location *)end {
	if ([URLResolver isCanadianStart:start end:end]) {
		return @"http://www.greyhound.ca/home/ticketcenter/en/step3.asp";
	}
	return @"http://www.greyhound.com/ticketcenter/en/Step3.asp";
}

+(NSString *)scheduleConfirmURLForStart:(Location *)start end:(Location *)end {
	if ([URLResolver isCanadianStart:start end:end]) {
		return @"http://www.greyhound.ca/home/ticketcenter/en/step2.asp";
	}
	return @"http://www.greyhound.com/ticketcenter/en/step2.asp";
}

+(NSString *)scheduleDetailsURLForSchedule:(Schedule *)schedule {
	if (schedule->isCanadian)
		return [NSString stringWithFormat:@"http://www.greyhound.ca/home/ticketcenter/en/%@", schedule->detailsArgs];
	return [NSString stringWithFormat:@"http://www.greyhound.com/ticketcenter/en/%@", schedule->detailsArgs];
}

@end
