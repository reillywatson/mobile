//
//  HighScoreRetrievalOperation.h
//  NameThatTune
//
//  Created by Reilly Watson on 11-01-17.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HighScoreRetrievalDelegate
-(void)gotHighScores:(NSArray *)results;
@optional
-(void)error:(NSError *)error;
@end


@interface HighScoreRetrievalOperation : NSOperation {
	NSObject<HighScoreRetrievalDelegate> *_delegate;
	NSString *_genre;
}

-(id)initWithDelegate:(NSObject<HighScoreRetrievalDelegate>*)delegate genre:(NSString *)genre;

@end
