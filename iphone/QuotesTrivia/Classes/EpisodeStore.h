//
//  EpisodeStore.h
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-01.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "QuestionStore.h"

@interface EpisodeStore : QuestionStore {
	NSMutableArray *quotes;
}

@end
