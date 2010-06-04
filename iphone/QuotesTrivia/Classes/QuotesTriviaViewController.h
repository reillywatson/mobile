//
//  QuotesTriviaViewController.h
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-05-31.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QuestionManager.h"
#import "AdMobDelegateProtocol.h"

@interface QuotesTriviaViewController : UIViewController<AdMobDelegate> {
	UILabel *opt1;
	UILabel *opt2;
	UILabel *opt3;
	UIWebView *webview;
	UIImageView *progress;
	UILabel *questionNo;
	Question *currentQuestion;
	QuestionManager *questionManager;
	int answersStreak;
}

@property (nonatomic, retain) IBOutlet UILabel *opt1;
@property (nonatomic, retain) IBOutlet UILabel *opt2;
@property (nonatomic, retain) IBOutlet UILabel *opt3;
@property (nonatomic, retain) IBOutlet UIWebView *webview;
@property (nonatomic, retain) IBOutlet UIImageView *progress;
@property (nonatomic, retain) IBOutlet UILabel *questionNo;
-(IBAction)opt1Clicked;
-(IBAction)opt2Clicked;
-(IBAction)opt3Clicked;

@end

