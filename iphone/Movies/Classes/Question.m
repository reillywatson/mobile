//
//  Question.m
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Question.h"


@implementation Question

-(id)initWithQuestionText:(NSString *)text options:(NSArray *)aoptions {
	self = [super init];
	options = [aoptions retain];
	correct = [[aoptions objectAtIndex:0] retain];
	questionText = [text retain];
	return self;
}

-(void)dealloc {
	[super dealloc];
	[options release];
	[correct release];
	[questionText release];
}

@end
