//
//  LocationsDataRetrievalOperation.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "LocationsDataRetrievalOperation.h"
#import "SBJSON.h"
#import "Location.h"
#import "URLResolver.h"

@implementation LocationsDataRetrievalOperation

@synthesize searchText;

-(id)initWithDelegate:(NSObject<LocationsDataRetrievalDelegate>*)aDelegate searchText:(NSString *)theSearchText {
	self = [super init];
	delegate = aDelegate;
	searchText = [[NSString alloc] initWithString:theSearchText];
	return self;
}

-(NSArray *)locationsForString:(NSString *)text {
	NSString *url = [URLResolver locationsURLForText:text];
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
	[request setHTTPMethod:@"POST"];
	[request setValue:@"application/json; charset=utf-8" forHTTPHeaderField:@"Content-type"];
	NSString *params = [NSString stringWithFormat:@"{\"context\":{\"Text\":\"%@\",\"NumberOfItems\":0}}", text];
	[request setHTTPBody:[params dataUsingEncoding:NSUTF8StringEncoding]];
	NSError *error = nil;
	NSLog(@"HEY, MY REQUEST: %@", request);
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
	SBJSON *jsonEngine = [[SBJSON new] autorelease];
	NSDictionary *json = [jsonEngine objectWithString:response];
	NSArray *items = [[json objectForKey:@"d"] objectForKey:@"Items"];
	NSMutableArray *locations = [NSMutableArray array];
	for (NSDictionary *item in items) {
		bool enabled = [item objectForKey:@"Enabled"];
		if (enabled) {
			NSString *name = [item objectForKey:@"Text"];
			NSString *locationId = [item objectForKey:@"Value"];
			Location *location = [[Location alloc] initWithName:name locationID:locationId];
			[locations addObject:location];
		}
	}
	if (error != nil) {
		[delegate locationsError:error];
		NSLog(@"OH SHIT DAWG, GETTING LOCATION DATA FAILED: %@", error);
	}
	else {
		[delegate performSelectorOnMainThread:@selector(locationsFound:) withObject:locations waitUntilDone:NO];
	}
	//NSLog(@"RESPONSE FROM SERVER: %@", response);
	return locations;
}

-(void)main {
	[self locationsForString:searchText];
}

-(void)dealloc {
	[super dealloc];
	[searchText release];
}

@end
