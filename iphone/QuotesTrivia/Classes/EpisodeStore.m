//
//  EpisodeStore.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-01.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "EpisodeStore.h"
#import "Quote.h"

@interface EpisodeStore (Private)
-(Quote *)newQuoteWithEpisodeTitle:(NSString *)title quote:(NSString *)quoteStr;
-(Quote *)randomQuote;
@end


@implementation EpisodeStore

-(Quote *)newQuoteWithEpisodeTitle:(NSString *)title quote:(NSString *)quoteStr {
	Quote *quote = [Quote new];
	quote->quote = [NSString stringWithString:quoteStr];
	quote->episode = [NSString stringWithString:title];
	quote->speaker = nil;
	return quote;
}

-(void)loadQuestionsWithFilename:(NSString *)filename {
	NSString *filestr = [NSString stringWithContentsOfFile:filename encoding:NSUTF8StringEncoding error:nil];
	NSArray *lines = [filestr componentsSeparatedByCharactersInSet:[NSCharacterSet newlineCharacterSet]];
	int numQuotes = [lines count] / 3;
	for (int i = 0; i < numQuotes; i++) {
		// We don't do anything with season right now
		//NSString *season = [lines objectAtIndex:i];
		NSString *epTitle = [NSString stringWithString:[lines objectAtIndex:(i * 3) + 1]];
		NSString *quoteStr = [NSString stringWithString:[lines objectAtIndex:(i * 3) + 2]];
		Quote *quote = [self newQuoteWithEpisodeTitle:epTitle quote:quoteStr];
		[quotes addObject:quote];
	}
}

-(id)initWithFilename:(NSString *)filename {
	self = [super init];
	quotes = [NSMutableArray new];
	[self loadQuestionsWithFilename:filename];
	return self;
}

-(int)numQuestions {
	return [quotes count];
}

-(bool)isAvailable {
	return [quotes count] > 0;
}

-(NSString *)storeName {
	return @"EPISODE";
}

-(Question *)newQuestionFromQuote:(Quote *)quote {
	Question *question = [Question new];
	question->question = [NSString stringWithString:quote->quote];
	question->correctAnswer = [NSString stringWithString:quote->episode];
	int correctPos = random() % 3;
	while ([question->answers count] < 3) {
		if ([question->answers count] - 1 == correctPos) {
			[question->answers addObject:[NSString stringWithString:quote->episode]];
		}
		else {
			Quote *randquote = [self randomQuote];
			// TODO: remove duplicate answers
			[question->answers addObject:[NSString stringWithString:randquote->episode]];
		}
	}
	return question;
}

-(Quote *)randomQuote {
	int rand = random() % [quotes count];
	return [quotes objectAtIndex:rand];
}

-(Question *)newQuestion {
	Quote *quote = [self randomQuote];
	return [self newQuestionFromQuote:quote];
}

@end
