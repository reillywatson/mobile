//
//  QuestionManager.h
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Question.h"

@interface QuestionManager : NSObject {
	NSDictionary *yearRanges;
}

+(QuestionManager *)sharedInstance;
-(Question *)newQuestionInCategory:(int)category;

@end
