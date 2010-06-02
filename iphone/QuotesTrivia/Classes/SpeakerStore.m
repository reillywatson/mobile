//
//  SpeakerStore.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-02.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "SpeakerStore.h"
#import "Quote.h"

@implementation SpeakerStore

-(id)initWithFilename:(NSString *)filename {
	self = [super initWithFilename:filename];
	speakers = [NSMutableSet new];
	return self;
}

-(void)dealloc {
	[super dealloc];
	[speakers release];
}

-(NSString *)newQuestionStringForQuote:(Quote *)quote {
	return [[NSString alloc] initWithFormat:@"<span style='color: black'><span><b>Who said it?</b></span><p><div style='margin-left: -40px'>%@</div></span>", quote->quote];
}

-(NSString *)newAnswerStringForQuote:(Quote *)quote {
	if (quote->speaker == nil)
		return nil;
	return [[NSString alloc] initWithString:quote->speaker];
}

-(NSString *)newSpeakerForQuote:(NSString *)quote {
	NSArray *components = [quote componentsSeparatedByString:@"<b>"];
	if ([components count] == 2) {
		components = [[components objectAtIndex:1] componentsSeparatedByString:@"</b>"];
		if ([components count] == 2) {
			NSString *speaker = [[components objectAtIndex:0] stringByReplacingOccurrencesOfString:@":" withString:@""];
			return [[speaker stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] retain];
		}
	}
	return nil;
}

-(NSString *)quoteWithoutSpeaker:(NSString *)quote {
	NSRange slashB = [quote rangeOfString:@"</b>"];
	NSString *noSpeaker = [quote substringFromIndex:slashB.location + 5];
	if ([noSpeaker characterAtIndex:0] == ':') {
		noSpeaker = [noSpeaker substringFromIndex:1];
	}
	return [NSString stringWithFormat:@"<dl><dd>%@", noSpeaker];
}

-(Quote *)newQuoteWithEpisodeTitle:(NSString *)title quote:(NSString *)quoteStr {
	Quote *sq = nil;
	NSString *speaker = [self newSpeakerForQuote:quoteStr];
	if (speaker != nil) {
		[speakers addObject:speaker];
		quoteStr = [self quoteWithoutSpeaker:quoteStr];
		sq = [super newQuoteWithEpisodeTitle:title quote:quoteStr];
		sq->speaker = speaker;
	}
	return sq;
}

-(Quote *)randomQuote {
	Quote *quote = nil;
	while (quote == nil || quote->speaker == nil) {
		quote = [super randomQuote];
	}
	return quote;
}

@end
