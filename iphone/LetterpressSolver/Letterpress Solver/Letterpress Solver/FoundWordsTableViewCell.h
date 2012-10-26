//
//  FoundWordsTableViewCell.h
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-26.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BoardView.h"

@interface FoundWordsTableViewCell : UITableViewCell {
    UILabel *_word;
    UILabel *_myScore;
    UILabel *_theirScore;
    BoardView *_board;
}

@property (nonatomic, strong) IBOutlet UILabel *word;
@property (nonatomic, strong) IBOutlet UILabel *myScore;
@property (nonatomic, strong) IBOutlet UILabel *theirScore;
@property (nonatomic, strong) IBOutlet BoardView *board;

@end
