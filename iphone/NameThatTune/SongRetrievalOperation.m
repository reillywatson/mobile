//
//  SongRetrievalOperation.m
//  NameThatTune
//
//  Created by Reilly Watson on 10-07-23.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "SongRetrievalOperation.h"
#import "PreviewURLProvider.h"
#import "AudioStreamer.h"

@implementation SongRetrievalOperation

-(NSString *)getRandomTrack {
	
	/*
	NSMutableDictionary *params = [[NSMutableDictionary new] autorelease];

	[params setObject:@"United%20States" forKey:@"country"];
	[params setObject:_LASTFM_API_KEY_ forKey:@"api_key"];
	NSString *data = [engine stringForMethod:@"geo.getTopTracks" withParameters:params useSignature:NO httpMethod:@"GET" error:nil];
	NSDictionary *parsedJSON = [json objectWithString:data];
	NSArray *tracks = [[parsedJSON objectForKey:@"toptracks"] objectForKey:@"track"];
	NSDictionary *randomTrack = [tracks randomElement];
	NSString *artistName = [[randomTrack objectForKey:@"artist"] objectForKey:@"name"];
	NSString *trackName = [randomTrack objectForKey:@"name"];
	NSString *fullName = [NSString stringWithFormat:@"%@ - %@", artistName, trackName];
	NSLog(@"%@", fullName);*/
	
	static NSArray *entries = nil;
	
	if (entries == nil) {
		
		NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/sf=143441/limit=400/explicit=true/json"]];

		NSError *error;
		NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
		NSDictionary *parsedJSON = [json objectWithString:response];
		entries = [[parsedJSON objectForKey:@"feed"] objectForKey:@"entry"];
	}
	NSDictionary *randomTrack = [entries randomElement];
	NSString *link = [[[[randomTrack objectForKey:@"link"] objectAtIndex:1] objectForKey:@"attributes"] objectForKey:@"href"];
	//NSLog(@"name: %@", randomTrack);
	NSString *title = [[randomTrack objectForKey:@"title"] objectForKey:@"label"];
	NSLog(@"Track: %@", randomTrack);
	NSLog(@"TITLE: %@", title);

	AudioStreamer *streamer = [[AudioStreamer alloc] initWithURL:[NSURL URLWithString:link]];
	[streamer start];
	return @"";
}

-(id)init {
	self = [super init];
	engine = [FMEngine new];
	json = [SBJSON new];
	return self;
}

-(void)dealloc {
	[super dealloc];
	[engine release];
	[json release];
}

-(void)main {
	[self getRandomTrack];
}

@end
