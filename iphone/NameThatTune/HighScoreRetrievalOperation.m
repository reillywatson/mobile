//
//  HighScoreRetrievalOperation.m
//  NameThatTune
//
//  Created by Reilly Watson on 11-01-17.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "HighScoreRetrievalOperation.h"
#import "SBJSON.h"
#import "Score.h"

@implementation HighScoreRetrievalOperation

-(id)initWithDelegate:(NSObject<HighScoreRetrievalDelegate>*)delegate genre:(NSString *)genre{
	self = [super init];
	_delegate = delegate;
	_genre = [genre retain];
	return self;
}

-(void)main {
	SBJSON *json = [SBJSON new];
	
	NSString *urlstr = [NSString stringWithFormat:@"http://vaskenmusic.appspot.com/vaskenmusicserver?genre=%@", _genre];
	NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"http://vaskenmusic.appspot.com/vaskenmusicserver?genre=%@", _genre]];
	NSLog(@"URL: %@", url);
	NSLog(@"URLSTR: %@", urlstr);
	NSLog(@"GENRE: %@", _genre);
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
	
	NSError *error = nil;
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
	if (error != nil) {
		NSLog(@"ERROR getting high scores: %@", error);
	}
	NSDictionary *parsedJSON = [json objectWithString:response];
	NSDictionary *scores = [parsedJSON objectForKey:@"HighScores"];
	NSArray *scoresTodayJson = [scores objectForKey:@"Today"];
	NSArray *allTimeJson = [scores objectForKey:@"Ever"];	
	
	NSLog(@"Scores: %@", scores);
	
	NSMutableArray *results = [NSMutableArray new];
	NSMutableArray *today = [NSMutableArray new];
	for (NSDictionary *scoreDict in scoresTodayJson) {
		Score *score = [[Score alloc] initWithName:[scoreDict objectForKey:@"name"] score:[[scoreDict objectForKey:@"score"] intValue]];
		[today addObject:score];
	}
	[results addObject:today];
	
	NSMutableArray *allTime = [NSMutableArray new];
	for (NSDictionary *scoreDict in allTimeJson) {
		Score *score = [[Score alloc] initWithName:[scoreDict objectForKey:@"name"] score:[[scoreDict objectForKey:@"score"] intValue]];
		[allTime addObject:score];
	}	
	[results addObject:allTime];
	
	[_delegate performSelectorOnMainThread:@selector(gotHighScores:) withObject:results waitUntilDone:NO];
}


@end
