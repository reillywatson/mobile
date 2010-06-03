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
	if ([[currentQuestion->answers objectAtIndex:answerNo] isEqualToString:currentQuestion->correctAnswer]) {
		NSLog(@"CORRECT");
		answersStreak++;
	}
	else {
		NSLog(@"INCORRECT - ANSWER WAS %@", currentQuestion->correctAnswer);
		answersStreak = 0;
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
	NSLog(@"QUESTION: %@", currentQuestion->answers);
	int indices[3] = {0, 1, 2};
	[opt1 setTitle:[currentQuestion->answers objectAtIndex:0] forState:UIControlStateNormal];
	[opt2 setTitle:[currentQuestion->answers objectAtIndex:1] forState:UIControlStateNormal];
	[opt3 setTitle:[currentQuestion->answers objectAtIndex:2] forState:UIControlStateNormal];
	[webview loadHTMLString:currentQuestion->question baseURL:nil];
	[progress setImage:[UIImage imageNamed:[NSString stringWithFormat:@"progress%i.png", answersStreak]]];
	[questionNo setText:[NSString stringWithFormat:@"Question %d of 10", answersStreak + 1]];
}

- (void)viewDidLoad {
    [super viewDidLoad];
	webview.opaque = NO;
	webview.backgroundColor = [UIColor clearColor];
//	AdMobView *ad = [AdMobView requestAdWithDelegate:self]; // start a new ad request
 //   ad.frame = CGRectMake(0, 412, 320, 48); // set the frame, in this case at the bottom of the screen
  //  [self.view addSubview:ad]; // attach the ad to the view hierarchy; self.view is responsible for retaining the ad	
	currentQuestion = nil;
	answersStreak = 0;
	questionManager = [QuestionManager new];
	[self loadNewQuestion];
}

- (NSString *)publisherId {
	return @"a14bf8b7dde92c0";
}


- (void)dealloc {
    [super dealloc];
}

@end
