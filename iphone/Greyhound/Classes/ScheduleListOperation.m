//
//  ScheduleListOperation.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleListOperation.h"
#import "RegexKitLite.h"
#import "Schedule.h"


@implementation ScheduleListOperation

-(id)initWithStartLocation:(Location *)start endLocation:(Location *)end requestID:(NSString *)requestID date:(NSDate *)date delegate:(NSObject<ScheduleListDelegate>*)aDelegate {
	self = [super init];
	_start = [start retain];
	_end = [end retain];
	_requestID = [requestID retain];
	_date = [date retain];
	delegate = aDelegate;
	return self;
}

-(void)dealloc {
	[super dealloc];
	[_start release];
	[_end release];
	[_date release];
	[_requestID release];
}


-(void)main {
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://www.greyhound.ca/home/ticketcenter/en/step3.asp"]];
	[request setHTTPMethod:@"POST"];
	NSArray *startNameComponents = [_start->name componentsSeparatedByString:@","];
	if ([startNameComponents count] != 2) {
		NSError *error = [NSError errorWithDomain:@"listSchedule" code:0 userInfo:nil];
		[delegate scheduleError:error];
		return;
	}
	NSString *ocityName = [[[startNameComponents objectAtIndex:0] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSString *ocodeName = _start->locationId;
	NSString *ostateName = [[startNameComponents objectAtIndex:1] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
	
	NSArray *endNameComponents = [_end->name componentsSeparatedByString:@","];
	if ([endNameComponents count] != 2) {
		NSError *error = [NSError errorWithDomain:@"listSchedule" code:0 userInfo:nil];
		[delegate scheduleError:error];
		return;
	}
	NSString *dcityName = [[[endNameComponents objectAtIndex:0] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSString *dcodeName = _end->locationId;
	NSString *dstateName = [[endNameComponents objectAtIndex:1] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
	NSCalendar *gregorian = [[[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar] autorelease];
	unsigned unitFlags = NSMonthCalendarUnit |  NSDayCalendarUnit;
	NSDateComponents *comps = [gregorian components:unitFlags fromDate:_date];
	NSString *month = [NSString stringWithFormat:@"%d", [comps month]];
	NSString *day = [NSString stringWithFormat:@"%d", [comps day]];
	
	NSString *params = [NSString stringWithFormat:@"Legs=1&Adults=1&Child2=0&Child5=0&Seniors=0&DMonth=%@&DDay=%@&DHr=&RMonth=%@&RDay=%@&RHr=&OriginState=%@&OriginLocationData=%@%@&DestinationState=%@&DestinationLocationData=%@%@&DiscountCode=&Password=&RequestID=%@&x=48&y=6"
																								, month, day, month, day, ostateName, ocodeName, ocityName, dstateName, dcodeName, dcityName, _requestID];
	[request setHTTPBody:[params dataUsingEncoding:NSUTF8StringEncoding]];
	NSError *error = nil;
	NSLog(@"HEY, MY REQUEST: %@", request);
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
	//<input type="radio" name="OSched" id="idOSched14" value="14|13|5537|GLC|10:30p|01:25a|00,02:55|0|Y|Y|">
	NSString *regex = @"<input[^>]*?name=\"OSched\"[^>]*?value=\"([^>]*?)\"";
	//	openWin('ScheduleDetails.asp?ScheduleIndex=1&amp;CS1=3473192467537936%3ASock1&amp;ID=100678696','Details','width=750,height=350,toolbar=0,location=0,directories=0,status=0,menuBar=0,scrollBars=1,resizable=1' ); newWindow.focus()" >5503</a><br>
	NSString *detailsRegex = @"ScheduleDetails.asp?(.*?)'";
	NSArray *detailsComponents = [response arrayOfCaptureComponentsMatchedByRegex:detailsRegex options:RKLDotAll range:NSMakeRange(0, [response length]) error:nil];
	NSArray *components = [response arrayOfCaptureComponentsMatchedByRegex:regex options:RKLDotAll range:NSMakeRange(0, [response length]) error:nil];
	NSMutableArray *schedules = [NSMutableArray new];
	for (NSArray *comp in components) {
		NSArray *data = [[comp objectAtIndex:0] componentsSeparatedByString:@"|"];
		NSString *scheduleID = [data objectAtIndex:2];
		NSString *carrier = [data objectAtIndex:3];
		NSString *departureTime = [data objectAtIndex:4];
		NSString *arrivalTime = [data objectAtIndex:5];
		NSString *duration = [data objectAtIndex:6];
		NSString *numStops = [data objectAtIndex:7];
		NSString *detailsURLArgs = [[detailsComponents objectAtIndex:[schedules count]] objectAtIndex:0];
		detailsURLArgs = [[[detailsURLArgs stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding] stringByReplacingOccurrencesOfString:@"&amp;" withString:@"&"];
		Schedule *schedule = [[Schedule alloc] initWithScheduleID:scheduleID carrier:carrier departureTime:departureTime arrivalTime:arrivalTime duration:duration numStops:numStops detailsArgs:detailsURLArgs];
		[schedules addObject:schedule];
	}
	if (error != nil) {
		NSLog(@"OH SHIT DAWG, GETTING LOCATION DATA FAILED: %@", error);
	}
	else {
		[delegate performSelectorOnMainThread:@selector(gotSchedules:) withObject:schedules waitUntilDone:NO];
	}
//	NSLog(@"RESPONSE FROM SERVER: %@", response);
}

@end
