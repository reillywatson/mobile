//
//  Board.h
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CellOwner.h"


@interface Board : NSObject {
@public
    CellOwner owners[25];
    char letters[25];
}
-(Board*)boardByAddingPath:(NSArray*)path;
@end
