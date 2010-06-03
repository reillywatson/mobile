//
//  TriviaStore.h
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-03.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "QuestionStore.h"

@interface TriviaStore : QuestionStore {
	NSMutableArray *questions;
}

@end
