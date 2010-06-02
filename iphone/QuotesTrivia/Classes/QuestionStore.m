//
//  QuestionStore.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-01.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "QuestionStore.h"

@implementation QuestionStore

-(Question *)newQuestion {
	return nil;
}

-(NSString *)storeName {
	return nil;
}

-(int)numQuestions {
	return 0;
}

-(bool)isAvailable {
	return NO;
}

-(id)initWithFilename:(NSString *)filename {
	self = [super init];
	return self;
}

@end
