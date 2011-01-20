//
//  HighScoreSubmitOperation.h
//  NameThatTune
//
//  Created by Reilly Watson on 11-01-17.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HighScoreSubmitDelegate
-(void)highScoreSubmissionSuccessful;
-(void)highScoreSubmissionError:(NSError *)error;
@end


@interface HighScoreSubmitOperation : NSOperation {
	int _score;
	NSString *_userName;
	NSString *_genre;
	NSObject<HighScoreSubmitDelegate> *_delegate;
}

-(id)initWithDelegate:(NSObject<HighScoreSubmitDelegate> *) delegate score:(int)score userName:(NSString *)userName genre:(NSString *)genre;

@end
