//
//  Board.m
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import "Board.h"

@implementation Board

-(id)init {
    self = [super init];
    for (int i = 0; i < 25; i++) {
        self->owners[i] = Empty;
        self->letters[i] = ' ';
    }
    return self;
}

@end
