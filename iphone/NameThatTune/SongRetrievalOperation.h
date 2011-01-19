//
//  SongRetrievalOperation.h
//  NameThatTune
//
//  Created by Reilly Watson on 10-07-23.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "FMEngine.h"
#import "JSON.h"

@protocol TrackInfoDelegate
-(void)trackListReady:(NSArray *)tracks;
-(int)entriesToReturn;
@optional
-(void)error:(NSError *)error;
@end

@interface SongRetrievalOperation : NSOperation {
	SBJSON *json;
	NSObject<TrackInfoDelegate> *delegate;
}

-(id)initWithDelegate:(NSObject <TrackInfoDelegate> *)myDelegate;

@end
