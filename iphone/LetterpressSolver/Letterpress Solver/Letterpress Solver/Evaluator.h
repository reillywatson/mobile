//
//  Evaluator.h
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Board.h"

@interface Evaluator : NSObject

-(NSArray*)findAllWordsWithBoard:(Board*)board;

@end
