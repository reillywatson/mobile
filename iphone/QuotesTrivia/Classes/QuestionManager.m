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
#import "NSMutableArray-Shuffle.h"

@implementation QuestionManager

-(void)addIfAvailable:(QuestionStore *)store {
	if ([store isAvailable]) {
		[questionStores addObject:store];
	}
	else {
		[store release];
	}
}

-(id)init {
	self = [super init];
	questionStores = [NSMutableArray new];
	
	[self addIfAvailable:[[EpisodeStore alloc] initWithFilename:[[NSBundle mainBundle] pathForResource:@"quotes" ofType:@"txt"]]];
	[self addIfAvailable:[[SpeakerStore alloc] initWithFilename:[[NSBundle mainBundle] pathForResource:@"quotes" ofType:@"txt"]]];
	[self addIfAvailable:[[TriviaStore alloc] initWithFilename:[[NSBundle mainBundle] pathForResource:@"trivia" ofType:@"txt"]]];
	return self;
}

-(Question *)newQuestion {
	int storeno = random() % [questionStores count];
	Question *question = [[questionStores objectAtIndex:storeno] newQuestion];
	[question->answers shuffle];
	return question;
}

@end
