//
//  Question.h
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-01.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Question : NSObject {
@public
	NSString *question;
	NSString *correctAnswer;
	NSMutableArray *answers;
}

@end