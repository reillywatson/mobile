//
//  HotItem.m
//  HitIt
//
//  Created by Reilly Watson on 10-05-23.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "HotItem.h"


@implementation HotItem

-(id)init {
	self = [super init];
	image = nil;
	imageURL = nil;
	rateID = nil;
	resultTotals = nil;
	resultAverage = nil;
	return self;
}

-(void)dealloc {
	[super dealloc];
	[image release];
	[imageURL release];
	[rateID release];
	[resultTotals release];
	[resultAverage release];
}

@end
