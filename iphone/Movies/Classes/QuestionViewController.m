//
//  QuestionViewController.m
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "QuestionViewController.h"
#import "QuestionManager.h"


@interface QuestionViewController (Private)
-(void)loadQuestions;
-(void)newGame;
-(void)newQuestion;
@end

@implementation QuestionViewController

@synthesize webView = _webView;
@synthesize rightWrongImage = _rightWrongImage;

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
	NSString *content = @"MY TEST CONTENT";
	[self.webView loadHTMLString:[NSString stringWithFormat:@"<html><head><style>body{background-color:transparent; color:white;}</style></head><body>%@</body></html>", content] baseURL:nil];
}


- (void)dealloc {
	[_questions release];
    [super dealloc];
}


@end
