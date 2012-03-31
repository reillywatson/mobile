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

+(NSString *)stateForText:(NSString *)text {
	NSArray *startNameComponents = [text componentsSeparatedByString:@","];
	if ([startNameComponents count] != 2)
		return nil;
	return [[startNameComponents objectAtIndex:1] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];	
}

+(NSString *)stateForLocation:(Location *)location {
	return [URLResolver stateForText:location->name];
}

+(BOOL)isCanadianStart:(Location *)start end:(Location *)end {
	if ([URLResolver stateIsCanadian:[URLResolver stateForLocation:start]] && [URLResolver stateIsCanadian:[URLResolver stateForLocation:end]]) {
		return YES;
	}
	return NO;
}

+(NSString *)locationsURLForText:(NSString *)text {
	if ([URLResolver stateIsCanadian:[URLResolver stateForText:text]]) {
		return @"http://www.greyhound.ca/services/locations.asmx/GetDestinationLocationsByName";		
	}
	return @"http://www.greyhound.com/services/locations.asmx/GetDestinationLocationsByName";
}

+(NSString *)scheduleConfirmURLForStart:(Location *)start end:(Location *)end {
	if ([URLResolver isCanadianStart:start end:end]) {
		return @"https://www.greyhound.ca/services/farefinder.asmx/Search";
	}
	return @"https://www.greyhound.com/services/farefinder.asmx/Search";
}

+(NSString *)step2URLForStart:(Location *)start end:(Location *)end {
	if ([URLResolver isCanadianStart:start end:end]) {
		return @"https://www.greyhound.ca/farefinder/step2.aspx";
	}
	return @"https://www.greyhound.com/farefinder/step2.aspx";
};

+(NSString *)scheduleDetailsURLForSchedule:(Schedule *)schedule {
	if (schedule->isCanadian)
		return @"https://www.greyhound.ca/services/farefinder.asmx/GetScheduleDetails";
	return @"https://www.greyhound.com/services/farefinder.asmx/GetScheduleDetails";
}

@end
