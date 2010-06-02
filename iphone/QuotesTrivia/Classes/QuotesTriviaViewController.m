//
//  QuotesTriviaViewController.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-05-31.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "QuotesTriviaViewController.h"

@interface QuotesTriviaViewController (Private)

-(void)answerSelected:(int)answer;
-(void)loadNewQuestion;

@end


@implementation QuotesTriviaViewController

@synthesize opt1, opt2, opt3, webview;

-(void)opt1Clicked {
	[self answerSelected:0];
}

-(void)opt2Clicked {
	[self answerSelected:1];
}

-(void)opt3Clicked {
	[self answerSelected:2];
}

-(void)answerSelected:(int)answerNo {
	if ([[currentQuestion->answers objectAtIndex:answerNo] isEqualToString:currentQuestion->correctAnswer]) {
		NSLog(@"CORRECT");
		answersStreak++;
	}
	else {
		NSLog(@"INCORRECT - ANSWER WAS %@", currentQuestion->correctAnswer);
		answersStreak = 0;
	}
	[self loadNewQuestion];
}

-(void)loadNewQuestion {
	currentQuestion = [questionManager newQuestion];
	NSLog(@"QUESTION: %@", currentQuestion->answers);
	[opt1 setTitle:[currentQuestion->answers objectAtIndex:0] forState:UIControlStateNormal];
	[opt2 setTitle:[currentQuestion->answers objectAtIndex:1] forState:UIControlStateNormal];
	[opt3 setTitle:[currentQuestion->answers objectAtIndex:2] forState:UIControlStateNormal];
	[webview loadHTMLString:currentQuestion->question baseURL:nil];
}

- (void)viewDidLoad {
    [super viewDidLoad];
	currentQuestion = nil;
	answersStreak = 0;
	questionManager = [QuestionManager new];
	[self loadNewQuestion];
}


- (void)dealloc {
    [super dealloc];
}

@end
