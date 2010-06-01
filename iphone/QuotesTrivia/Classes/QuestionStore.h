//
//  QuestionStore.h
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-01.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Question.h"

@interface QuestionStore : NSObject {

}

-(id)initWithFilename:(NSString *)filename;


-(NSString *)storeName;
-(bool)isAvailable;
-(int)numQuestions;
-(Question *)newQuestion;

@end
