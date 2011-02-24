//
//  QuestionViewController.h
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QuestionType.h"
#import "Question.h"
#import "AdViewController.h"

@interface QuestionViewController : UIViewController {
	int _questionType;
	NSArray *_questions;
	int _correctAnswers;
	int _bestScore;
	UIWebView *_webView;
	UIImageView *_rightWrongImage;
	UILabel *_currentStreakLabel;
	UILabel *_bestScoreLabel;
	UIButton *_opt1;
	UIButton *_opt2;
	UIButton *_opt3;
	Question *_currentQuestion;
	AdViewController *_adView;
}

@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) IBOutlet UIImageView *rightWrongImage;
@property (nonatomic, retain) IBOutlet UILabel *currentStreakLabel;
@property (nonatomic, retain) IBOutlet UILabel *bestScoreLabel;
@property (nonatomic, retain) IBOutlet UIButton *opt1;
@property (nonatomic, retain) IBOutlet UIButton *opt2;
@property (nonatomic, retain) IBOutlet UIButton *opt3;
@property (nonatomic, retain) IBOutlet AdViewController *adView;

-(IBAction)opt1Selected;
-(IBAction)opt2Selected;
-(IBAction)opt3Selected;

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil questionType:(int)questionType;

@end
