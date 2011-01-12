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

@interface SongRetrievalOperation : NSOperation {
	FMEngine *engine;
	SBJSON *json;
}

@end
