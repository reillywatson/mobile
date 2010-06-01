//
//  QuestionManager.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-01.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "QuestionManager.h"
#import "EpisodeStore.h"

@implementation QuestionManager

-(id)init {
	self = [super init];
	questionStores = [NSMutableArray new];
	[questionStores addObject:[[EpisodeStore alloc] initWithFilename:[[NSBundle mainBundle] pathForResource:@"quotes" ofType:@"txt"]]];
	return self;
}

-(Question *)newQuestion {
	return [[questionStores objectAtIndex:0] newQuestion];
}

@end
