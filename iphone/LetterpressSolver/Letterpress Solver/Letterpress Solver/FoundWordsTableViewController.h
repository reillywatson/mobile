//
//  FoundWordsTableViewController.h
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-26.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Board.h"

@interface FoundWordsTableViewController : UITableViewController {
    NSArray *_paths;
    Board *_board;
}

@property (nonatomic, strong) NSArray *paths;
@property (nonatomic, strong) Board *board;

@end
