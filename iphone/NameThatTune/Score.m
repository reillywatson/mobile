//
//  Score.m
//  NameThatTune
//
//  Created by Reilly Watson on 11-01-17.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Score.h"


@implementation Score

@synthesize name=_name;
@synthesize score=_score;

-(id)initWithName:(NSString *)aName score:(int)aScore {
	self = [super init];
	_name = [aName retain];
	_score = aScore;
	return self;
}

-(void)dealloc {
	[super dealloc];
	[_name release];
}

@end
