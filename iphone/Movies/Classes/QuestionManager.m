//
//  QuestionManager.m
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "QuestionManager.h"
#import "DatabaseManager.h"
#import "QuestionType.h"
#import "NominatedItem.h"
#import "Quote.h"

@interface QuestionManager (Private)
-(void)populateRanges;
@end

@implementation QuestionManager

static QuestionManager *INSTANCE = nil;

-(id)init {
	self = [super init];
	yearRanges = [NSMutableDictionary new];
	return self;
}

+(QuestionManager *)sharedInstance {
	if (INSTANCE == nil) {
		INSTANCE = [QuestionManager new];
		[INSTANCE populateRanges];
	}
	return INSTANCE;
}

-(int)getRandomYear:(NSString *)questionType {
	NSArray *range = [yearRanges objectForKey:questionType];
	int min = [[range objectAtIndex:0] intValue];
	int max = [[range objectAtIndex:1] intValue];
	return (rand() % (max - min)) + min;
}

-(NSString *)textForQuoteQuestion:(Quote *)quote year:(int)year { 
	return [NSString stringWithFormat:@"Which Oscar-nominated film, from %d, is the following quote from:<br/><br/>%@", year, quote->quote];
}

-(Question *)quoteQuestion {
	Quote *quote = [[DatabaseManager sharedInstance] getRandomQuote];
	NSArray *movies = [[DatabaseManager sharedInstance] getMoviesFromSameYear:quote->film];
	NSMutableArray *answers = [NSMutableArray new];
	for (int i = 0; i < [movies count]; i++) {
		NominatedItem *item = [movies objectAtIndex:i];
		[answers addObject:item->film];
		if ([item->film isEqualToString:quote->film]) {
			[answers exchangeObjectAtIndex:0 withObjectAtIndex:i];
		}
	}
	NominatedItem *item = [movies objectAtIndex:0];
	return [[Question alloc] initWithQuestionText:[self textForQuoteQuestion:quote year:item->year] options:answers];
}

typedef NSString *(^TextGenFn)(NominatedItem *item);
-(Question *)newQuestionForAward:(NSArray *)items textGenFn:(TextGenFn)getText {
	NSMutableArray *answers = [NSMutableArray new];
	NominatedItem *winner = nil;
	for (int i = 0; i < [items count]; i++) {
		NominatedItem *item = [items objectAtIndex:i];
		[answers addObject:item->nominee];
		if (item->isWinner) {
			winner = item;
			[answers exchangeObjectAtIndex:0 withObjectAtIndex:i];
		}
	}
	return [[Question alloc] initWithQuestionText:getText(winner) options:answers];
}

-(Question *)movieQuestion {
	NSArray *items = [[DatabaseManager sharedInstance] getBestDirectorEntries:[self getRandomYear:@"Best Picture"]];
	NSMutableArray *answers = [NSMutableArray new];
	NominatedItem *winner = nil;
	for (int i = 0; i < [items count]; i++) {
		NominatedItem *item = [items objectAtIndex:i];
		[answers addObject:item->film];
		if (item->isWinner) {
			winner = item;
			[answers exchangeObjectAtIndex:0 withObjectAtIndex:i];
		}
	}
	NominatedItem *item = [items objectAtIndex:0];
	return [[Question alloc] initWithQuestionText:[NSString stringWithFormat:@"Which film won the Oscar for Best Picture in %d?", item->year] options:answers];
}

TextGenFn genActorText = ^NSString *(NominatedItem *item) {
	return [NSString stringWithFormat:@"Who won the %@ Oscar in %d for his role as %@, in the movie %@?", item->awardName, item->year, item->role, item->film];
};
TextGenFn genActressText = ^NSString *(NominatedItem *item) {
	return [NSString stringWithFormat:@"Who won the %@ Oscar in %d for her role as %@, in the movie %@?", item->awardName, item->year, item->role, item->film];
};
TextGenFn genDirectorText = ^NSString *(NominatedItem *item) {
	return [NSString stringWithFormat:@"Who won the Oscar for Best Director in %d for the movie %@?", item->year, item->film];
};
TextGenFn genPictureText = ^NSString *(NominatedItem *item) {
	return [NSString stringWithFormat:@"Which film won the Oscar for Best Picture in %d?", item->year];
};

-(Question *)newQuestionInCategory:(int)category {
	Question *result = nil;
	if (category == QuestionType_Actors) {
		result = [self newQuestionForAward:[[DatabaseManager sharedInstance] getBestActorEntries:[self getRandomYear:@"Best Actor"]] textGenFn:genActorText];
	}
	if (category == QuestionType_Actresses) {
		result = [self newQuestionForAward:[[DatabaseManager sharedInstance] getBestActressEntries:[self getRandomYear:@"Best Actress"]] textGenFn:genActressText];
	}
	else if (category == QuestionType_SupportingActors) {
		result = [self newQuestionForAward:[[DatabaseManager sharedInstance] getBestSupportingActorEntries:[self getRandomYear:@"Best Supporting Actor"]] textGenFn:genActorText];
	}
	else if (category == QuestionType_SupportingActresses) {
		result = [self newQuestionForAward:[[DatabaseManager sharedInstance] getBestSupportingActressEntries:[self getRandomYear:@"Best Supporting Actress"]] textGenFn:genActressText];
	}
	else if (category == QuestionType_Directors) {
		result = [self newQuestionForAward:[[DatabaseManager sharedInstance] getBestDirectorEntries:[self getRandomYear:@"Best Director"]] textGenFn:genDirectorText];
	}
	else if (category == QuestionType_Movies) {
		result = [self movieQuestion];
//		result = [self newQuestionForAward:[[DatabaseManager sharedInstance] getBestPictureEntries:[self getRandomYear:@"Best Picture"]] textGenFn:genPictureText];
	}
	else if (category == QuestionType_Quotes) {
		result = [self quoteQuestion];
	}
	return result;
}

-(void)addYearRange:(NSString *)awardName {
	[yearRanges setValue:[[DatabaseManager sharedInstance] getYearRanges:awardName] forKey:awardName];
}

-(void)populateRanges {
	[self addYearRange:@"Best Actor"];
	[self addYearRange:@"Best Actress"];
	[self addYearRange:@"Best Supporting Actor"];
	[self addYearRange:@"Best Supporting Actress"];
	[self addYearRange:@"Best Director"];
	[self addYearRange:@"Best Picture"];
}

@end
