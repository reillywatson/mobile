//
//  QuotesTriviaViewController.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-05-31.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "QuotesTriviaViewController.h"
#import "NSString-Encoding.h"
#import "AdMobView.h"

@interface QuotesTriviaViewController (Private)

-(void)answerSelected:(int)answer;
-(void)loadNewQuestion;
-(void)displayQuestionNo;

@end


@implementation QuotesTriviaViewController

@synthesize opt1, opt2, opt3, webview, progress, questionNo;

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
	[[NSRunLoop currentRunLoop] cancelPerformSelectorsWithTarget:self];
	if ([[currentQuestion->answers objectAtIndex:answerNo] isEqualToString:currentQuestion->correctAnswer]) {
		[questionNo setText:@"Correct!"];
		[self performSelector:@selector(displayQuestionNo) withObject:nil afterDelay:2];
		NSLog(@"CORRECT");
		answersStreak++;
	}
	else {
		NSLog(@"INCORRECT - ANSWER WAS %@", [currentQuestion->correctAnswer stringByDecodingXMLEntities]);
		answersStreak = 0;
		[self displayQuestionNo];
	}
	if (answersStreak == 0) {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"You Lose!" message:[NSString stringWithFormat:@"Nope, it was actually \"%@\".  Try again.", currentQuestion->correctAnswer]
													   delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		[alert release];
	}
	else if (answersStreak == 10) {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"You Win!" message:@"Clearly you're awesome!"
													   delegate:nil cancelButtonTitle:@"Obviously..." otherButtonTitles:nil];
		[alert show];
		[alert release];
		answersStreak = 0;
	}
	
	[self loadNewQuestion];
}

-(void)loadNewQuestion {
	currentQuestion = [questionManager newQuestion];
	[opt1 setText:[[currentQuestion->answers objectAtIndex:0] stringByDecodingXMLEntities]];
	[opt2 setText:[[currentQuestion->answers objectAtIndex:1] stringByDecodingXMLEntities]];
	[opt3 setText:[[currentQuestion->answers objectAtIndex:2] stringByDecodingXMLEntities]];
	[webview loadHTMLString:currentQuestion->question baseURL:nil];
	[progress setImage:[UIImage imageNamed:[NSString stringWithFormat:@"progress%i.png", answersStreak]]];
}

-(void)displayQuestionNo {
	[questionNo setText:[NSString stringWithFormat:@"Question %d of 10", answersStreak + 1]];
}

- (void)viewDidLoad {
    [super viewDidLoad];
	webview.opaque = NO;
	webview.backgroundColor = [UIColor clearColor];
	AdMobView *ad = [AdMobView requestAdWithDelegate:self]; // start a new ad request
    ad.frame = CGRectMake(0, 412, 320, 48); // set the frame, in this case at the bottom of the screen
    [self.view addSubview:ad]; // attach the ad to the view hierarchy; self.view is responsible for retaining the ad	
	currentQuestion = nil;
	answersStreak = 0;
	questionManager = [QuestionManager new];
	[self loadNewQuestion];
	[self displayQuestionNo];
}

- (NSString *)publisherIdForAd:(AdMobView *)adView {
	return @"a14c0899dcb1233";
}
- (UIViewController *)currentViewControllerForAd:(AdMobView *)adView {
	return self;
}

- (void)dealloc {
    [super dealloc];
}

@end
