//
//  Location.m
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Location.h"


@implementation Location

-(id)initWithName:(NSString *)aName locationID:(NSString *)aLocationID {
	self = [super init];
	name = [aName retain];
	locationId = [aLocationID retain];
	return self;
}

-(void)dealloc {
	[super dealloc];
	[name release];
	[locationId release];
}

@end
