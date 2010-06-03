//
//  QuestionManager.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-01.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "QuestionManager.h"
#import "EpisodeStore.h"
#import "SpeakerStore.h"
#import "TriviaStore.h"

@implementation QuestionManager

-(id)init {
	self = [super init];
	questionStores = [NSMutableArray new];
	[questionStores addObject:[[EpisodeStore alloc] initWithFilename:[[NSBundle mainBundle] pathForResource:@"quotes" ofType:@"txt"]]];
	[questionStores addObject:[[SpeakerStore alloc] initWithFilename:[[NSBundle mainBundle] pathForResource:@"quotes" ofType:@"txt"]]];
	[questionStores addObject:[[TriviaStore alloc] initWithFilename:[[NSBundle mainBundle] pathForResource:@"trivia" ofType:@"txt"]]];
	return self;
}

-(Question *)newQuestion {
	int storeno = random() % [questionStores count];
	return [[questionStores objectAtIndex:storeno] newQuestion];
}

@end
