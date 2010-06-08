//
//  Comic.m
//  Comics
//
//  Created by Reilly Watson on 10-05-16.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "Comic.h"


@implementation Comic

-(id)init {
	self = [super init];
	image = nil;
	url = nil;
	permalink = nil;
	nextUrl = nil;
	prevUrl = nil;
	title = nil;
	altText = nil;
	randomUrl = nil;
	return self;
}

@end
