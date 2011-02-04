//
//  NSMutableArray-Shuffle.m
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-03.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "NSMutableArray-Shuffle.h"

@implementation NSArray (Random)

-(id)randomElement {
	return [self objectAtIndex:random() % [self count]];
}

@end


@implementation NSMutableArray (Shuffle)

- (void)shuffle
{
    NSUInteger count = [self count];
    for (NSUInteger i = 0; i < count; ++i) {
        // Select a random element between i and end of array to swap with.
        int nElements = count - i;
        int n = (random() % nElements) + i;
        [self exchangeObjectAtIndex:i withObjectAtIndex:n];
    }
}

@end
