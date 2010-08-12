//
//  ComicsKingdomDownloader.m
//  Comics
//
//  Created by Reilly Watson on 10-07-27.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "ComicsKingdomDownloader.h"


@implementation ComicsKingdomDownloader

// http://content.comicskingdom.net/Prince_Valiant/Prince_Valiant.20100725_large.gif


-(NSString *)stringFromDate:(NSDate *)date {
	NSCalendar *gregorian = [[[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar] autorelease];
	NSDateComponents *components = [gregorian components:(NSDayCalendarUnit | NSYearCalendarUnit | NSMonthCalendarUnit) fromDate:date];
	NSInteger day = [components day];
	NSInteger year = [components year];
	NSInteger month = [components month];
	NSString *newDateStr = [NSString stringWithFormat:@"%04d%02d%02d", year, month, day];
	return newDateStr;	
}

-(NSString *)dateURL:(int)dayOffset {
	NSString *dateStr = [comic->image substringWithRange:NSMakeRange([comic->image length] - 18, 8)];
	NSInteger year = [[dateStr substringToIndex:4] intValue];
	NSInteger month = [[dateStr substringWithRange:NSMakeRange(4, 2)] intValue];
	NSInteger day = [[dateStr substringFromIndex:6] intValue];
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
	NSString *prevLink = [self dateURL: sundaysOnly ? -7 : -1];
	if (prevLink != nil)
		comic->prevUrl = [[NSString alloc] initWithFormat:@"%@%@_large.gif", basePrevNextURL, prevLink];
}

-(void)parseNextLink:(NSString *)str {
	NSString *nextLink = [self dateURL:sundaysOnly ? 7 : 1];
	if (nextLink != nil)
		comic->nextUrl = [[NSString alloc] initWithFormat:@"%@%@_large.gif", basePrevNextURL, nextLink];
}

-(Comic *)getComicSynchronously {
	if ([url isEqual:@"calculateondemand"]) {
		NSCalendar *gregorian = [[[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar] autorelease];
		// Get the weekday component of the current date
		NSDate *today = [[NSDate new] autorelease];
		NSDate *newest = today;
		if (sundaysOnly) {
			NSDateComponents *weekdayComponents = [gregorian components:NSWeekdayCalendarUnit fromDate:today];
			
			/*
			 Create a date components to represent the number of days to subtract from the current date.
			 The weekday value for Sunday in the Gregorian calendar is 1, so subtract 1 from the number of days to subtract from the date in question.  (If today's Sunday, subtract 0 days.)
			 */
			NSDateComponents *componentsToSubtract = [[[NSDateComponents alloc] init] autorelease];
			[componentsToSubtract setDay: 0 - ([weekdayComponents weekday] - 1)];
			newest = [gregorian dateByAddingComponents:componentsToSubtract toDate:today options:0];
		}
		comic->image = [[NSString alloc] initWithFormat:@"%@%@_large.gif", basePrevNextURL, [self stringFromDate:newest]];
	}
	else {
		comic->image = [url retain];
	}
	NSLog(@"URL: %@", comic->image);
	[self parsePrevLink:nil];
	[self parseNextLink:nil];
	comic->url = nil;
	return comic;
}

@end
