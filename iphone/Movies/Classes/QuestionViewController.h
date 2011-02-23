//
//  QuestionViewController.h
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QuestionType.h"

@interface QuestionViewController : UIViewController {
	int _questionType;
	NSArray *_questions;
	int _correctAnswers;
	UIWebView *_webView;
	UIImageView *_rightWrongImage;
	UILabel *_currentStreakLabel;
	UILabel *_bestScoreLabel;
}

@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) IBOutlet UIImageView *rightWrongImage;
@property (nonatomic, retain) IBOutlet UILabel *currentStreakLabel;
@property (nonatomic, retain) IBOutlet UILabel *bestScoreLabel;

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil questionType:(int)questionType;

@end
