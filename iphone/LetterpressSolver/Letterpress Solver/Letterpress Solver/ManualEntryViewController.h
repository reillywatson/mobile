//
//  ManualEntryViewController.h
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Board.h"
#import "BoardView.h"

@interface ManualEntryViewController : UIViewController<UITextViewDelegate> {
    Board *_board;
    UITextView *textView;
    int _cursorLocation;
    CellOwner _selectedOwner;
    BoardView* _boardView;
}

@property (nonatomic, strong) IBOutlet BoardView* boardView;

-(IBAction)showKeyboard;
-(IBAction)showWords;
-(IBAction)graySelected;
-(IBAction)blueSelected;
-(IBAction)redSelected;

@end
