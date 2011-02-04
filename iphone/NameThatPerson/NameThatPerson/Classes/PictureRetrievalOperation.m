//
//  PictureRetrievalOperation.m
//  NameThatPerson
//
//  Created by Reilly Watson on 11-01-31.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "PictureRetrievalOperation.h"
#import "SBJSON.h"
#import "NSMutableArray-Shuffle.h"

@implementation PictureRetrievalOperation

-(id)initWithSearchQuery:(NSString *)aquery delegate:(NSObject<PictureRetrievalDelegate>*)adelegate {
	self = [super init];
	query = [aquery retain];
	delegate = adelegate;
	return self;	
}

-(void)dealloc {
	[super dealloc];
	[query release];
}

-(void)main {
	NSLog(@"QUERY: %@", query);
	NSString *searchURL = [NSString stringWithFormat:@"https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%@", [query stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:searchURL]];
	NSError *error = nil;
	NSLog(@"HEY, MY REQUEST: %@", request);
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
	SBJSON *jsonEngine = [[SBJSON new] autorelease];
	NSDictionary *json = [jsonEngine objectWithString:response];
	NSArray *results = [[json objectForKey:@"responseData"] objectForKey:@"results"];
	NSString *imageUrl = [[results randomElement] objectForKey:@"unescapedUrl"];
	NSLog(@"IMAGE URL: %@", imageUrl);
	NSURLRequest *imageRequest = [NSURLRequest requestWithURL:[NSURL URLWithString:imageUrl]];
	error = nil;
	NSData *image = [NSURLConnection sendSynchronousRequest:imageRequest returningResponse:nil error:&error];
	[delegate performSelectorOnMainThread:@selector(imageReady:) withObject:image waitUntilDone:NO];
	
}

@end
