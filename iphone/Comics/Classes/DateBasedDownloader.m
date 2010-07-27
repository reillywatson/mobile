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

/*
public class SharingMachineDownloader extends Downloader {

	private String rootDomain;
	
	public SharingMachineDownloader(String rootDomain) {
		this.rootDomain = rootDomain;
	}
	
	private Pattern imgData = Pattern.compile( "<div class=\"headertext\">.*?<a href=\"(.*?)\">(.*?)<", Pattern.DOTALL);
	
	private String getComicUrl(Calendar calendar) {
		return String.format("%s/index.php?date=%02d%02d%02d", rootDomain, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR) - 2000);
	}
	
	@Override
	protected boolean parseComic(StringBuilder partialResponse) {
		Matcher m = imgData.matcher(partialResponse);
		if (m.find()) {
			comic.image = m.group(1);
			comic.title = m.group(2);
			String[] urlParts = m.group(1).split("/");
			String dateStr = urlParts[urlParts.length - 2];
			Calendar calendar = new GregorianCalendar();
			calendar.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(0, 2)) - 1);
			calendar.set(Calendar.DATE, Integer.parseInt(dateStr.substring(2, 4)));
			calendar.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(4, 6)) + 2000);
			calendar.add(Calendar.DATE, 1);
			Calendar now = new GregorianCalendar();
			Date nowDate = new Date();
			now.set(nowDate.getYear() + 1900, nowDate.getMonth(), nowDate.getDate());
			if (calendar.before(now)) {
				comic.nextUrl = getComicUrl(calendar);
				Log.d("NEXT", comic.nextUrl);
			}
			calendar.add(Calendar.DATE, -2);
			comic.prevUrl = getComicUrl(calendar);
			Log.d("PREV", comic.prevUrl);
			return true;
		}
		return false;
	}
	
	@Override
	protected Pattern getComicPattern() {
		return imgData;
	}
	
	@Override
	protected Pattern getNextComicPattern() {
		return null;
	}
	
	@Override
	protected Pattern getPrevComicPattern() {
		return null;
	}
	
	@Override
	protected Comic newComic() {
		Comic c = super.newComic();
		c.randomUrl = rootDomain + "/randomComicViewer.php";
		return c;
	}
}
*/

@end
