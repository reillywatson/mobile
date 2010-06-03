//
//  TriviaStore.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-03.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "TriviaStore.h"


@implementation TriviaStore

-(void)loadQuestionsWithFilename:(NSString *)filename {
	NSString *filestr = [NSString stringWithContentsOfFile:filename encoding:NSUTF8StringEncoding error:nil];
	NSArray *lines = [filestr componentsSeparatedByCharactersInSet:[NSCharacterSet newlineCharacterSet]];
	int numQuotes = [lines count] / 4;
	for (int i = 0; i < numQuotes; i++) {
		// We don't do anything with season right now
		//NSString *season = [lines objectAtIndex:i];
		Question *question = [Question new];
		question->question = [[NSString alloc] initWithString:[lines objectAtIndex:(i * 4)]];
		for (int j = 1; j < 4; j++) {
			NSString *answer = [lines objectAtIndex:(i * 4) + j];
			if ([answer hasPrefix:@"* "]) {
				answer = [answer substringFromIndex:2];
				question->correctAnswer = [[NSString alloc] initWithString:answer];
			}
			[question->answers addObject:[[NSString alloc] initWithString:answer]];
		}
		[questions addObject:question];
	}
}

-(id)initWithFilename:(NSString *)filename {
	self = [super init];
	questions = [NSMutableArray new];
	[self loadQuestionsWithFilename:filename];
	return self;
}

-(void)dealloc {
	[super dealloc];
	[questions release];
}

-(NSString *)storeName {
	return @"TRIVIA";
}
-(bool)isAvailable {
	return [questions count] > 0;
}
-(int)numQuestions {
	return [questions count];
}
-(Question *)newQuestion {
	int rand = random() % [questions count];
	return [[questions objectAtIndex:rand] retain];
}

@end
