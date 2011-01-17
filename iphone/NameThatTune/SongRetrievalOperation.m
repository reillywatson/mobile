//
//  SongRetrievalOperation.m
//  NameThatTune
//
//  Created by Reilly Watson on 10-07-23.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "SongRetrievalOperation.h"
#import "NameThatTuneAppDelegate.h"

@implementation SongRetrievalOperation

-(void)getRandomTracks {
	
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/sf=143441/limit=400/explicit=true/json"]];

	NSError *error = nil;
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
	if (error != nil) {
		NSLog(@"ERROR retrieving tracklist: %@", error);
	}
	NSDictionary *parsedJSON = [json objectWithString:response];
	NSArray *entries = [[parsedJSON objectForKey:@"feed"] objectForKey:@"entry"];
	
	[((NameThatTuneAppDelegate*)[[UIApplication sharedApplication] delegate]) setTrackEntries:entries];

	int numEntries = [delegate entriesToReturn];
	NSMutableArray *returnedEntries = [NSMutableArray new];
	while ([returnedEntries count] < numEntries) {
		NSDictionary *randomTrack = [entries randomElement];
		[returnedEntries addObject:randomTrack];
	}
	[delegate performSelectorOnMainThread:@selector(trackListReady:) withObject:returnedEntries waitUntilDone:NO];
}

-(id)initWithDelegate:(NSObject<TrackInfoDelegate>*) myDelegate {
	self = [super init];
	json = [SBJSON new];
	delegate = myDelegate;
	return self;
}

-(void)dealloc {
	[super dealloc];
	[json release];
}

-(void)main {
	[self getRandomTracks];
}

@end
