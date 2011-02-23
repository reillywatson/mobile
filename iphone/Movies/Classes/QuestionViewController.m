//
//  QuestionViewController.m
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

// This is required in order to access the CALayer properties
#import <QuartzCore/QuartzCore.h>
#import "QuestionViewController.h"
#import "QuestionManager.h"
#import "DatabaseManager.h"
#import "Quote.h"
#import "NominatedItem.h"

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
@synthesize opt1 = _opt1;
@synthesize opt2 = _opt2;
@synthesize opt3 = _opt3;


-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil questionType:(int)questionType {
	self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
	_questionType = questionType;
	return self;
}

-(void)viewDidLoad {
	[super viewDidLoad];
	[self.webView setBackgroundColor:[UIColor clearColor]];
	[self.webView setOpaque:NO];
	// Round corners using CALayer property
	[[self.webView layer] setCornerRadius:4];
	[self.webView setClipsToBounds:YES];
	
	// Create colored border using CALayer property
	[[self.webView layer] setBorderColor:[HEXCOLOR(0x0f5e92) CGColor]];
	[[self.webView layer] setBorderWidth:1];
	
	[self newGame];
}

-(void)newGame {
	_correctAnswers = 0;
	_bestScore = 0;
	[self newQuestion];
}

-(void)newQuestion {
	[self.currentStreakLabel setText:[NSString stringWithFormat:@"Current Streak: %d", _correctAnswers]];
	if (_correctAnswers > _bestScore) {
		_bestScore = _correctAnswers;
	}
	[self.bestScoreLabel setText:[NSString stringWithFormat:@"Best Score: %d", _bestScore]];		
	Question *question = [[QuestionManager sharedInstance] newQuestionInCategory:_questionType];
	NSString *content = question->questionText;
	[self.webView loadHTMLString:[NSString stringWithFormat:@"<html><head><style>body{background-color:transparent; color:white;}</style></head><body>%@</body></html>", content] baseURL:nil];
	NSMutableArray *unset = [NSMutableArray arrayWithObjects:self.opt1, self.opt2,self.opt3, nil];
	[unset shuffle];
	NSString *correct = [question->options objectAtIndex:0];
	NSMutableArray *possibleAnswers = [NSMutableArray arrayWithArray:question->options];
	[possibleAnswers shuffle];
	[possibleAnswers removeObject:correct];
	[possibleAnswers insertObject:correct atIndex:(rand() % [unset count])];
	NSLog(@"CORRECT: %@", correct);
	for (int i = 0; i < [unset count]; i++) {
		UIButton *button = [unset objectAtIndex:i];
		NSLog(@"SETTING ANSWER: %@", [possibleAnswers objectAtIndex:i]);
		[button setTitle:[possibleAnswers objectAtIndex:i] forState:UIControlStateNormal];
	}
	_currentQuestion = question;
}

-(void)showResultIndicator:(BOOL)success {
	[self.rightWrongImage setImage:[UIImage imageNamed:success ? @"trivia_right.png" : @"trivia_wrong.png"]];
	self.rightWrongImage.alpha = 1.0;
	[UIView beginAnimations:nil context:nil];  
	[UIView setAnimationCurve:UIViewAnimationCurveEaseOut];
	[UIView setAnimationDuration:2.0];
	self.rightWrongImage.alpha = 0;
	[UIView commitAnimations];
}

-(void)answerSelected:(UIButton *)button {
	if ([[button titleForState:UIControlStateNormal] isEqualToString:_currentQuestion->correct]) {
		_correctAnswers++;
		[self showResultIndicator:YES];
	}
	else {
		_correctAnswers = 0;
		[self showResultIndicator:NO];
	}
	[self newQuestion];
}


-(IBAction)opt1Selected {
	[self answerSelected:self.opt1];
}

-(IBAction)opt2Selected {
	[self answerSelected:self.opt2];
}

-(IBAction)opt3Selected {
	[self answerSelected:self.opt3];
}


- (void)dealloc {
	[_questions release];
    [super dealloc];
}


@end
