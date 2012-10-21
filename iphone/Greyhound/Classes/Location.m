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
	name = aName;
	locationId = aLocationID;
	return self;
}


@end
