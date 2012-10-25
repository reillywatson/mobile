//
//  Evaluator.h
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Board.h"

typedef struct {
    int myScore;
    int theirScore;
    int myProtected;
    int theirProtected;
} Evaluation;

@interface Evaluator : NSObject

-(NSArray*)findAllWordsWithBoard:(Board*)board;
-(NSArray *)allPossiblePathsForWord:(NSString*)word withBoard:(Board *)board subpath:(NSArray *)currentSubpath;
-(Evaluation) evaluatePath:(NSArray *)path withBoard:(Board*)board;

@end
