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
    NSArray *letters;
    NSArray *owners;
}

-(CellOwner)ownerAtCell:(int)cellNum;
-(char)letterAtCell:(int)cellNum;

@end
