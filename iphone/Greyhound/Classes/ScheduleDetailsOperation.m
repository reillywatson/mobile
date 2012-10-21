//
//  ScheduleDetailsOperation.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleDetailsOperation.h"
#import "ScheduleDetails.h"
#import "URLResolver.h"
#import "SBJSON.h"

@interface NSDictionary (Extensions)
-(id)objectForKey:(NSString *)key defaultValue:(id)def;
@end

@implementation NSDictionary (Extensions)
-(id)objectForKey:(NSString *)key defaultValue:(id)def {
	id obj = [self objectForKey:key];
	if (obj == nil || [obj isKindOfClass:[NSNull class]]) {
		return def;
	}
	return obj;
}
@end

@implementation ScheduleDetailsOperation

-(id)initWithSchedule:(Schedule *)schedule delegate:(NSObject<ScheduleDetailsDelegate> *)adelegate {
	self = [super init];
	_schedule = [schedule retain];
	delegate = adelegate;
	return self;
}

-(id)init {
    self = [super init];
    _schedule = nil;
    return self;
}

-(void)dealloc {
	[super dealloc];
	[_schedule release];
}

-(void)main {
	NSString *url = [URLResolver scheduleDetailsURLForSchedule:_schedule];
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
	[request setHTTPMethod:@"POST"];
	NSString *data = [NSString stringWithFormat:@"{\"request\":{\"__type\":\"Greyhound.Website.DataObjects.ClientScheduleDetailRequest\",\"Key\":\"%@\"}}", _schedule->scheduleID];
	[request setValue:@"application/json; charset=utf-8" forHTTPHeaderField:@"Content-Type"];	
	[request setHTTPBody:[data dataUsingEncoding:NSUTF8StringEncoding]];
	
	NSLog(@"HEY, MY REQUEST: %@", request);
	NSError *error =nil;
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
	NSLog(@"RESPONSE FROM SERVER: %@", response);
	if (error != nil) {
		NSLog(@"DETAILS ERROR: %@", error);
		return;
	}
	
	SBJSON *engine = [[SBJSON new] autorelease];
	NSArray *stops = [[[engine objectWithString:response] objectForKey:@"d"] objectForKey:@"Items"];
	NSMutableArray *detailsList = [[NSMutableArray new] autorelease];
	for (NSDictionary *stop in stops) {
		NSString *cityName = [stop objectForKey:@"Location" defaultValue:@""];
		cityName = [cityName stringByReplacingOccurrencesOfString:@"(START)" withString:@""];
		cityName = [cityName stringByReplacingOccurrencesOfString:@"(END)" withString:@""];
		if ([cityName hasPrefix:@" - "]) {
			cityName = [cityName substringFromIndex:3];
		}
		NSString *arrives = [stop objectForKey:@"Arrives" defaultValue:@""];
		NSString *departs = [stop objectForKey:@"Departs" defaultValue:@""];
		if ([departs isEqualToString:@"(TRANSFER)"]) {
			departs = @"TRANSFER";
		}
		NSString *layover = [stop objectForKey:@"Layover" defaultValue:@""];
		NSString *schedule = [stop objectForKey:@"Schedule" defaultValue:@""];
		NSString *company = [stop objectForKey:@"Carrier" defaultValue:@""];
		NSLog(@"City: %@ Arrives: %@ Departs: %@ Layover: %@ Company: %@ Schedule:%@", cityName, arrives, departs, layover, company, schedule);
		ScheduleDetails *details = [[ScheduleDetails alloc] initWithCity:cityName arrives:arrives departs:departs layover:layover company:company schedule:schedule];
		[detailsList addObject:details];
	}
	[delegate performSelectorOnMainThread:@selector(detailsReady:) withObject:detailsList waitUntilDone:NO];
}

@end
