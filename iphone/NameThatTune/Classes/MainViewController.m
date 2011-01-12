//
//  MainViewController.m
//  NameThatTune
//
//  Created by Reilly Watson on 10-07-23.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "MainViewController.h"
#import "SongRetrievalOperation.h"
#import "AudioStreamer.h"

@interface MainViewController (Private)
-(void)newRound;
@end

@implementation MainViewController

@synthesize button1 = _button1;
@synthesize button2 = _button2;
@synthesize button3 = _button3;
@synthesize numCorrectLabel = _numCorrectLabel;
@synthesize timerLabel = _timerLabel;
@synthesize resultImage = _resultImage;

-(void)dealloc {
	[super dealloc];
	[[NSNotificationCenter defaultCenter] removeObserver:self];
}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
	_opQueue = [NSOperationQueue new];
	[super viewDidLoad];
	_numCorrect = 0;
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(playbackStatusChanged:) name:ASStatusChangedNotification object:nil];
	[self newRound];
}

-(int)entriesToReturn {
	return 3;
}

-(void)trackListReady:(NSArray *)tracks {
	[_tracks release];
	NSMutableArray *trackNames = [NSMutableArray new];
	for (int i = 0; i < 3; i++) {
		NSString *link = [[[[[tracks objectAtIndex:i] objectForKey:@"link"] objectAtIndex:1] objectForKey:@"attributes"] objectForKey:@"href"];
		NSString *title = [[[tracks objectAtIndex:i] objectForKey:@"title"] objectForKey:@"label"];
		if (i == 0) {
			if (_streamer != nil) {
				[_streamer stop];
				[_streamer release];
			}
			_streamer = [[AudioStreamer alloc] initWithURL:[NSURL URLWithString:link]];
			[_streamer start];
		}
		[trackNames addObject:title];
	}
	_tracks = trackNames;
	[tracks release];
}

-(void)updateTime {
	NSLog(@"UPDATE TIME STREAMER STATE: %d", [_streamer state]);
	int time = [[self.timerLabel text] intValue];
	if (time == 0) {
		// TODO: do something awesome like tell the user time is up
		return;
	}
	time = time - 1;
	[self.timerLabel setText:[NSString stringWithFormat:@"%d", time]];
}

-(void)startTimer {
	if ([_currentTimer isValid]) {
		[_currentTimer invalidate];
	}
	_currentTimer = [NSTimer scheduledTimerWithTimeInterval:1.0
									 target:self
								   selector:@selector(updateTime)
								   userInfo:nil
									repeats:YES];
	[self.timerLabel setHidden:NO];
	[self.timerLabel setText:@"10"];
	NSArray *buttons = [NSArray arrayWithObjects:self.button1, self.button2, self.button3, nil];
	int setButtons = 0;
	for (UIButton *button in buttons) {
		[button setEnabled:NO];
	}
	while (setButtons < 3) {
		UIButton *button = [buttons randomElement];
		if (![button isEnabled]) {
			[button setTitle:[_tracks objectAtIndex:setButtons] forState:UIControlStateNormal];
			[button setEnabled:YES];
			setButtons++;
		}
	}	
}

-(void)playbackStatusChanged:(NSNotification *)notification {
	NSLog(@"STREAMER STATE: %d", [_streamer state]);
	if ([notification object] == _streamer) {
		if ([_streamer state] == AS_PLAYING) {
			[self startTimer];
		}
	}
	else {
		NSLog(@"WHO ARE YOU, AUDIOSTREAM? %@", [notification object]);
		[[notification object] stop];
	}
}

-(void)newRound {
	NSLog(@"NUM CORRECT: %d", _numCorrect);
	[self.numCorrectLabel setText:[NSString stringWithFormat:@"%d in a row", _numCorrect]];
	[_opQueue addOperation:[[SongRetrievalOperation alloc] initWithDelegate:self]];
	[self.timerLabel setHidden:YES];
}

-(void)showResultIndicator:(BOOL)success {
	[self.resultImage setImage:[UIImage imageNamed:success ? @"correct.png" : @"wrong.png"]];
	self.resultImage.alpha = 1.0;
	[UIView beginAnimations:nil context:nil];  
	[UIView setAnimationCurve:UIViewAnimationCurveEaseOut];
	[UIView setAnimationDuration:1.5];
	self.resultImage.alpha = 0;
	[UIView commitAnimations];
}

-(void)answerSelected:(int)answer {
	NSArray *buttons = [NSArray arrayWithObjects:self.button1, self.button2, self.button3, nil];
	for (UIButton *button in buttons) {
		[button setEnabled:NO];
	}
	if ([[[buttons objectAtIndex:answer] titleForState:UIControlStateNormal] isEqualToString:[_tracks objectAtIndex:0]]) {
		_numCorrect++;
		[self showResultIndicator:YES];
	}
	else {
		_numCorrect = 0;
		[self showResultIndicator:NO];
	}
	[self newRound];
}

-(IBAction)button1Pressed {
	[self answerSelected:0];
}
-(IBAction)button2Pressed {
	[self answerSelected:1];
}
-(IBAction)button3Pressed {
	[self answerSelected:2];
}

@end
