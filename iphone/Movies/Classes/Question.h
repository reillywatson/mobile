//
//  Question.h
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Question : NSObject {
@public
	NSString *questionText;
	NSString *imageUrl;
	NSArray *options;
}

-(id)initWithQuestionText:(NSString *)text options:(NSArray *)aoptions;

@end
