//
//  NSArray-Additions.m
//  NameThatTune
//
//  Created by Reilly Watson on 10-07-23.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "NSArray-Additions.h"


@implementation NSArray (Additions)

-(id)randomElement {
	int randval = random() % [self count];
	return [self objectAtIndex:randval];
}

@end
