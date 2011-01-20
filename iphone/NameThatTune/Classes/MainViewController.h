//
//  MainViewController.h
//  NameThatTune
//
//  Created by Reilly Watson on 10-07-23.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "SongRetrievalOperation.h"
#import "HighScoreSubmitOperation.h"
#import "AudioStreamer.h"

@interface MainViewController : UIViewController<TrackInfoDelegate, HighScoreSubmitDelegate> {
	UILabel *_numCorrectLabel;
	UILabel *_timerLabel;
	UIImageView *_resultImage;
	UIButton *_button1;
	UIButton *_button2;
	UIButton *_button3;
	NSArray *_tracks;
	int _numCorrect;
	NSTimer *_currentTimer;
	AudioStreamer *_streamer;
	NSOperationQueue *_opQueue;
}

@property (nonatomic, retain) IBOutlet UILabel *numCorrectLabel;
@property (nonatomic, retain) IBOutlet UILabel *timerLabel;
@property (nonatomic, retain) IBOutlet UIImageView *resultImage;
@property (nonatomic, retain) IBOutlet UIButton *button1;
@property (nonatomic, retain) IBOutlet UIButton *button2;
@property (nonatomic, retain) IBOutlet UIButton *button3;

-(IBAction)button1Pressed;
-(IBAction)button2Pressed;
-(IBAction)button3Pressed;

@end
