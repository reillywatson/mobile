package com.vasken.comics.Downloaders;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

import com.vasken.comics.Comic;

/*
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
			
			
		//	 Create a date components to represent the number of days to subtract from the current date.
		//	 The weekday value for Sunday in the Gregorian calendar is 1, so subtract 1 from the number of days to subtract from the date in question.  (If today's Sunday, subtract 0 days.)
			NSDateComponents *componentsToSubtract = [[[NSDateComponents alloc] init] autorelease];
			[componentsToSubtract setDay: 0 - ([weekdayComponents weekday] - 1)];
			newest = [gregorian dateByAddingComponents:componentsToSubtract toDate:today options:0];
		}
		comic->image = [[NSString alloc] initWithFormat:@"%@%@_large.gif", basePrevNextURL, [self stringFromDate:newest]];
		comic->url = [comic->image retain];
	}
	else {
		comic->image = [url retain];
		comic->url = [url retain];
	}
	NSLog(@"URL: %@", comic->image);
	[self parsePrevLink:nil];
	[self parseNextLink:nil];
//	comic->url = nil;
	return comic;
}
*/

public class ComicsKingdomDownloader extends Downloader {
	private String baseUrl;
	private boolean sundaysOnly;
	
	public ComicsKingdomDownloader(String startUrl, boolean sundaysOnly) {
		this.baseUrl = startUrl;
		this.sundaysOnly = sundaysOnly;
	}
	
	private String getComicUrl(Calendar calendar) {
		return String.format("%s%04d%02d%02d.gif", baseUrl, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
	}
	
	private String getNewestUrl() {
		Calendar calendar = new GregorianCalendar();
		if (sundaysOnly) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		}
		
		return getComicUrl(calendar);
	}
	
	@Override
	protected Comic newComic() {		
		Comic comic = super.newComic();
		Calendar calendar = new GregorianCalendar();
		if (!url.endsWith(".gif")) {
			url = getNewestUrl();
		}
		Log.d(this.getClass().getName(), "URL: " + url);
		String[] urlParts = url.split("\\.");
		String dateStr = urlParts[urlParts.length - 2];
		calendar.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
		calendar.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
		calendar.set(Calendar.DATE, Integer.parseInt(dateStr.substring(6, 8)));
		comic.title = String.format("%02d/%02d", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
		int diff = 1;
		if (sundaysOnly) {
			diff = 7;
		}
		calendar.add(Calendar.DATE, diff);
		Calendar now = new GregorianCalendar();
		Date nowDate = new Date();
		now.set(nowDate.getYear() + 1900, nowDate.getMonth(), nowDate.getDate());
		if (calendar.before(now)) {
			comic.nextUrl = getComicUrl(calendar);
			Log.d("NEXT", comic.nextUrl);
		}
		calendar.add(Calendar.DATE, -diff * 2);
		comic.prevUrl = getComicUrl(calendar);
		Log.d("PREV", comic.prevUrl);
		comic.permalink = url;
		comic.image = url;
		comic.url = url;
		return comic;
	}
	
	@Override
	public Comic getComic() {
		return newComic();
	}
}
