//
//  HighScoreSubmitOperation.m
//  NameThatTune
//
//  Created by Reilly Watson on 11-01-17.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "HighScoreSubmitOperation.h"

@implementation HighScoreSubmitOperation

-(void)submitHighScore {
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://vaskenmusic.appspot.com/vaskenmusicserver"]];
	[request setHTTPMethod:@"POST"];
	[request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-type"];
	NSString *params = [NSString stringWithFormat:@"name=%s-=-=-=-=-=score=%d-=-=-=-=-=genre=%s-=-=-=-=-=version=%s", _userName, _score, _genre, @"1.0"];
	[request setValue:[params stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding] forHTTPHeaderField:@"data"];
	NSError *error = nil;
	NSLog(@"HEY, MY REQUEST: %@", request);
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
	if (error != nil) {
		NSLog(@"OH SHIT DAWG, HIGH SCORES FAILED: %@", error);
	}
	NSLog(@"RESPONSE FROM SERVER: %@", response);
}

-(id)initWithDelegate:(NSObject<HighScoreSubmitDelegate> *)delegate score:(int)score userName:(NSString *)userName genre:(NSString *)genre {
	self = [super init];
	_delegate = delegate;
	_score = score;
	_userName = userName;
	_genre = genre;
	return self;
}

-(void)main {
	[self submitHighScore];
}


@end
