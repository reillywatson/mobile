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
	quote->quote = [[NSString alloc] initWithString:quoteStr];
	quote->episode = [[NSString alloc] initWithString:title];
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
		NSString *epTitle = [[NSString alloc] initWithString:[lines objectAtIndex:(i * 3) + 1]];
		NSString *quoteStr = [[NSString alloc] initWithString:[lines objectAtIndex:(i * 3) + 2]];
		Quote *quote = [self newQuoteWithEpisodeTitle:epTitle quote:quoteStr];
		if (quote != nil)
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

-(NSString *)newQuestionStringForQuote:(Quote *)quote {
	return [[NSString alloc] initWithFormat:@"<span style='color: black'><span><b>Name the episode:</b></span><p><div style='margin-left: -40px'>%@</div></span>", quote->quote];
}

-(NSString *)newAnswerStringForQuote:(Quote *)quote {
	return [[NSString alloc] initWithString:quote->episode];
}

-(Question *)newQuestionFromQuote:(Quote *)quote {
	Question *question = [Question new];
	question->question = [self newQuestionStringForQuote:quote];
	question->correctAnswer = [self newAnswerStringForQuote:quote];
	int correctPos = random() % 3;
	while ([question->answers count] < 3) {
		if ([question->answers count] == correctPos) {
			[question->answers addObject:[self newAnswerStringForQuote:quote]];
		}
		else {
			Quote *randquote = [self randomQuote];
			NSString *answer = [self newAnswerStringForQuote:randquote];
			if ([question->answers containsObject:answer] || [answer isEqual:question->correctAnswer]) {
				[answer release];
			}
			else {
				[question->answers addObject:answer];
			}
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
