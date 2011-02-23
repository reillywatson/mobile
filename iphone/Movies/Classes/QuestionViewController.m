//
//  QuestionViewController.m
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "QuestionViewController.h"
#import "QuestionManager.h"
#import "DatabaseManager.h"
#import "Quote.h"

@interface QuestionViewController (Private)
-(void)loadQuestions;
-(void)newGame;
-(void)newQuestion;
@end

@implementation QuestionViewController

@synthesize webView = _webView;
@synthesize rightWrongImage = _rightWrongImage;
@synthesize currentStreakLabel = _currentStreakLabel;
@synthesize bestScoreLabel = _bestScoreLabel;

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil questionType:(int)questionType {
	self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
	_questionType = questionType;
	return self;
}

-(void)viewDidLoad {
	[self.webView setBackgroundColor:[UIColor clearColor]];
	[self.webView setOpaque:NO];
	[self loadQuestions];
}

-(void)loadQuestions {
	[_questions release];
	_questions = [QuestionManager loadQuestions:_questionType];
	[self newGame];
}

-(void)newGame {
	_correctAnswers = 0;
	[self newQuestion];
}

-(void)newQuestion {
	[self.currentStreakLabel setText:[NSString stringWithFormat:@"Current Streak: %d", _correctAnswers]];
	int bestScore = 100;
	[self.bestScoreLabel setText:[NSString stringWithFormat:@"Best Score: %d", bestScore]];
	Quote *quote = [[DatabaseManager sharedInstance] getRandomQuote];
	NSString *content = quote->quote;
	[self.webView loadHTMLString:[NSString stringWithFormat:@"<html><head><style>body{background-color:transparent; color:white; border:1px solid #0f5e92; border-radius:2px; padding:8px;}</style></head><body>%@</body></html>", content] baseURL:nil];
}


- (void)dealloc {
	[_questions release];
    [super dealloc];
}


@end
