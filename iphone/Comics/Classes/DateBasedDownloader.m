//
//  SharingMachineDownloader.m
//  Comics
//
//  Created by Reilly Watson on 10-07-27.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "DateBasedDownloader.h"

@implementation DateBasedDownloader

-(NSString *)stringFromDate:(NSDate *)date {
	NSCalendar *gregorian = [[[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar] autorelease];
	NSDateComponents *weekdayComponents = [gregorian components:(NSDayCalendarUnit | NSYearCalendarUnit | NSMonthCalendarUnit) fromDate:date];
	NSInteger day = [weekdayComponents day];
	NSInteger year = [weekdayComponents year];
	NSInteger month = [weekdayComponents month];
	NSString *newDateStr = [NSString stringWithFormat:@"%02d%02d%02d", month, day, year - 2000];
	return newDateStr;	
}

-(NSString *)dateURL:(int)dayOffset {
	NSArray *urlParts = [comic->image componentsSeparatedByString:@"/"];
	NSString *dateStr = [urlParts objectAtIndex:[urlParts count] - 2];
	NSInteger month = [[dateStr substringToIndex:2] intValue];
	NSInteger day = [[dateStr substringWithRange:NSMakeRange(2, 2)] intValue];
	NSInteger year = [[dateStr substringFromIndex:4] intValue] + 2000;
	NSDateComponents *components = [[[NSDateComponents alloc] init] autorelease];
	[components setMonth:month];
	[components setYear:year];
	[components setDay:day];
	NSCalendar *gregorian = [[[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar] autorelease];
	NSDate *date = [gregorian dateFromComponents:components];

	NSDateComponents *offsetComponents = [[[NSDateComponents alloc] init] autorelease];
	[offsetComponents setDay:dayOffset];
	NSDate *newDate = [gregorian dateByAddingComponents:offsetComponents toDate:date options:0];
	NSDate *today = [[NSDate new] autorelease];
	
	if ([newDate earlierDate:today] == today)
		return nil;
	return [self stringFromDate:newDate];
}

-(void)parsePrevLink:(NSString *)str {
	NSString *prevLink = [self dateURL:-1];
	if (prevLink != nil)
		comic->prevUrl = [[basePrevNextURL stringByAppendingString:prevLink] retain];
}

-(void)parseNextLink:(NSString *)str {
	NSString *nextLink = [self dateURL:1];
	if (nextLink != nil)
		comic->nextUrl = [[basePrevNextURL stringByAppendingString:nextLink] retain];
}

@end
