//
//  Evaluator.m
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import "Evaluator.h"
#include "AppDelegate.h"
#include <UIKit/UIKit.h>
#include <vector>

@implementation Evaluator

+(NSArray *)allPossiblePathsForWord:(NSString*)word withBoard:(Board *)board subpath:(NSArray *)currentSubpath {
    NSMutableArray *paths = [NSMutableArray new];
    if ([word length] == 0) {
        [paths addObject:currentSubpath];
        return paths;
    }
    char goal = [word characterAtIndex:0];
    for (int i = 0; i < 25; i++) {
        NSNumber *num = [NSNumber numberWithInt:i];
        if (board->letters[i] == goal && ![currentSubpath containsObject:num]) {
            NSMutableArray *newSubpath = [NSMutableArray arrayWithArray:currentSubpath];
            [newSubpath addObject:num];
            NSArray *subpaths = [self allPossiblePathsForWord:[word substringFromIndex:1] withBoard:board subpath:newSubpath];
            for (NSArray *path in subpaths) {
                [paths addObject:path];
            }
        }
    }
    return paths;
}

std::vector<int> neighbours(int i) {
    std::vector<int> neighbours;
    if (i >= 5) {
        neighbours.push_back(i - 5);
    }
    if (i <= 19) {
        neighbours.push_back(i + 5);
    }
    if (i % 5 > 0) {
        neighbours.push_back(i - 1);
    }
    if (i % 5 < 4) {
        neighbours.push_back(i + 1);
    }
    return neighbours;
}

+(bool)isProtected:(int) cell owners:(CellOwner*) owners {
    if (owners[cell] == Empty) {
        return false;
    }
    std::vector<int> n = neighbours(cell);
    bool result = true;
    for (int i = 0; i < n.size(); i++) {
        if (owners[n[i]] != owners[cell]) {
            result = false;
            break;
        }
    }
    return result;
}

+(Evaluation) evaluatePath:(NSArray *)path withBoard:(Board*)board {
    CellOwner newOwners[25];
    memcpy(&newOwners, board->owners, 25 * sizeof(CellOwner));
    for (NSNumber *cell in path) {
        int cellVal = [cell intValue];
        if (![Evaluator isProtected:cellVal owners:board->owners]) {
            newOwners[cellVal] = Mine;
        }
    }
    int myScore = 0;
    int theirScore = 0;
    int myProtected = 0;
    int theirProtected = 0;
    for (int i = 0; i < 25; i++) {
        if (newOwners[i] == Mine) {
            myScore++;
            if (![Evaluator isProtected:i owners:board->owners]) {
                myProtected++;
            }
        }
        if (newOwners[i] == Theirs) {
            theirScore++;
            if (![Evaluator isProtected:i owners:board->owners]) {
                theirProtected++;
            }
        }
    }
    return Evaluation { myScore, theirScore, myProtected, theirProtected };
}

+(NSArray*)findAllWordsWithBoard:(Board*)board {
    AppDelegate *delegate = [[UIApplication sharedApplication] delegate];
    NSArray *words = [delegate words];
    NSMutableArray *results = [NSMutableArray new];
    bool used[25];
    for (NSString* word in words) {
        for (int i = 0; i < 25; i++) {
            used[i] = false;
        }
        bool hasWord = true;
        for (int i = 0; i < [word length]; i++) {
            bool found = false;
            for (int cell = 0; cell < 25; cell++) {
                if (board->letters[cell] == [word characterAtIndex:i] && !used[cell]) {
                    used[cell] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                hasWord = false;
                break;
            }
        }
        if (hasWord) {
            [results addObject:word];
        }
    }
    return results;
}

+(NSComparisonResult)comparePath:(NSArray *)firstPath withPath:(NSArray *)secondPath withBoard:(Board *)board {
    Evaluation firstEval = [Evaluator evaluatePath:firstPath withBoard:board];
    Evaluation secondEval = [Evaluator evaluatePath:secondPath withBoard:board];
    int firstScoreDiff = firstEval.myScore - firstEval.theirScore;
    int secondScoreDiff = secondEval.myScore - secondEval.theirScore;
    if (ABS(firstScoreDiff - secondScoreDiff) > 2) {
        if (firstScoreDiff > secondScoreDiff) {
            return NSOrderedDescending;
        }
        return NSOrderedAscending;
    }
    int firstProtected = firstEval.myProtected - firstEval.theirProtected;
    int secondProtected = secondEval.myProtected - secondEval.theirProtected;
    if (ABS(firstProtected - secondProtected) > 2) {
        if (firstProtected > secondProtected) {
            return NSOrderedDescending;
        }
        return NSOrderedAscending;
    }
    if (firstScoreDiff > secondScoreDiff) {
        return NSOrderedDescending;
    }
    if (secondScoreDiff > firstScoreDiff) {
        return NSOrderedAscending;
    }
    if (firstProtected > secondProtected) {
        return NSOrderedDescending;
    }
    if (secondProtected > firstProtected) {
        return NSOrderedAscending;
    }
    return NSOrderedSame;

}
@end
