//
//  SpeakerStore.h
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-02.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "EpisodeStore.h"

@interface SpeakerStore : EpisodeStore {
	NSMutableSet *speakers;
}

@end
