//
//  Question.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-01.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "Question.h"


@implementation Question

-(id)init {
	self = [super init];
	answers = [NSMutableArray new];
	return self;
}

@end
