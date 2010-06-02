//
//  QuotesTriviaViewController.h
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-05-31.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QuestionManager.h"

@interface QuotesTriviaViewController : UIViewController {
	UIButton *opt1;
	UIButton *opt2;
	UIButton *opt3;
	UIWebView *webview;
	UIImageView *progress;
	Question *currentQuestion;
	QuestionManager *questionManager;
	int answersStreak;
}

@property (nonatomic, retain) IBOutlet UIButton *opt1;
@property (nonatomic, retain) IBOutlet UIButton *opt2;
@property (nonatomic, retain) IBOutlet UIButton *opt3;
@property (nonatomic, retain) IBOutlet UIWebView *webview;
@property (nonatomic, retain) IBOutlet UIImageView *progress;
-(IBAction)opt1Clicked;
-(IBAction)opt2Clicked;
-(IBAction)opt3Clicked;

@end

