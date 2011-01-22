//
//  ScheduleConfirmOperation.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleConfirmOperation.h"
#import "RegexKitLite.h"

@implementation ScheduleConfirmOperation

-(id)initWithStartLocation:(Location *)start endLocation:(Location *)end date:(NSDate *)date delegate:(id <ScheduleConfirmDelegate>)aDelegate {
	self = [super init];
	_start = [start retain];
	_end = [end retain];
	_date = [date retain];
	delegate = aDelegate;
	return self;
}

-(void)dealloc {
	[super dealloc];
	[_start release];
	[_end release];
	[_date release];
}

-(void)main {
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://www.greyhound.ca/home/ticketcenter/en/step2.asp"]];
	[request setHTTPMethod:@"POST"];
	
	NSCalendar *gregorian = [[[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar] autorelease];
	unsigned unitFlags = NSMonthCalendarUnit |  NSDayCalendarUnit | NSYearCalendarUnit;
	NSDateComponents *comps = [gregorian components:unitFlags fromDate:_date];
	NSString *month = [NSString stringWithFormat:@"%d", [comps month]];
	NSString *day = [NSString stringWithFormat:@"%d", [comps day]];
	NSString *year = [NSString stringWithFormat:@"%d", [comps year]];
	
	
	NSString *params = [NSString stringWithFormat:@"FormVersion=1.0&OriginCity=%@&DestinationCity=%@&Child2=0&Child5=0&Legs=1&Adults=1&Seniors=0&DYear=%@&DMonth=%@&DDay=%@&DHr=", [_start->name stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding], [_end->name stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding], year, month, day];
	[request setHTTPBody:[params dataUsingEncoding:NSUTF8StringEncoding]];
	NSError *error = nil;
	NSLog(@"HEY, MY REQUEST: %@", request);
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
	// this is the key to the whole response!
//	<input type="hidden" name="RequestID" value="100657914" />
	if (error != nil) {
		NSLog(@"OH SHIT DAWG, GETTING LOCATION DATA FAILED: %@", error);
		[delegate confirmationError:error];
	}
	else {
		NSArray *components = [response captureComponentsMatchedByRegex:@"<input type=\"hidden\" name=\"RequestID\" value=\"([^>]*?)\"" options:RKLDotAll range:NSMakeRange(0, [response length]) error:nil];
		if ([components count] > 1) {
			NSString *confirmation = [components objectAtIndex:1];
			[delegate gotConfirmationCode:confirmation];
		}
	}
//	NSLog(@"RESPONSE FROM SERVER: %@", response);
}
@end
