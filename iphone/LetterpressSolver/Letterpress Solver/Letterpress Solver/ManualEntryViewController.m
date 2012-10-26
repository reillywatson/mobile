//
//  ManualEntryViewController.m
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import "ManualEntryViewController.h"
#import "Evaluator.h"

@interface ManualEntryViewController ()

@end

@implementation ManualEntryViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(IBAction)showKeyboard {
    [textView becomeFirstResponder];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    CGRect aRect = CGRectMake(-100,-100, 1, 1);
    textView = [[UITextView alloc] initWithFrame:aRect];
    [textView setAutocorrectionType:UITextAutocorrectionTypeNo];
    [[self view] addSubview:textView];
    [textView setDelegate:self];
    [textView becomeFirstResponder];

    _cursorLocation = 0;
    
    _board = [Board new];
    _selectedOwner = Empty;
    
    boardView = (BoardView*)[self view];
    boardView->_board = _board;
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [boardView addGestureRecognizer:singleTap];

#if 0
    Board *board = [Board new];

    board->letters[0] =  'd';
    board->letters[1] =  't';
    board->letters[2] =  'p';
    board->letters[3] =  'w';
    board->letters[4] =  'u';
    board->letters[5] =  'c';
    board->letters[6] =  't';
    board->letters[7] =  'm';
    board->letters[8] =  'y';
    board->letters[9] =  'v';
    board->letters[10] = 'r';
    board->letters[11] = 'e';
    board->letters[12] = 'n';
    board->letters[13] = 'f';
    board->letters[14] = 'x';
    board->letters[15] = 'c';
    board->letters[16] = 'i';
    board->letters[17] = 'l';
    board->letters[18] = 'u';
    board->letters[19] = 'n';
    board->letters[20] = 'd';
    board->letters[21] = 'm';
    board->letters[22] = 'c';
    board->letters[23] = 'b';
    board->letters[24] = 'p';
/*
    board->letters[0] =  'l';
    board->letters[1] =  's';
    board->letters[2] =  'a';
    board->letters[3] =  'r';
    board->letters[4] =  'b';
    board->letters[5] =  'd';
    board->letters[6] =  'n';
    board->letters[7] =  'm';
    board->letters[8] =  'g';
    board->letters[9] =  'e';
    board->letters[10] = 'q';
    board->letters[11] = 'c';
    board->letters[12] = 'p';
    board->letters[13] = 'p';
    board->letters[14] = 'g';
    board->letters[15] = 'g';
    board->letters[16] = 'b';
    board->letters[17] = 'i';
    board->letters[18] = 'h';
    board->letters[19] = 'a';
    board->letters[20] = 's';
    board->letters[21] = 'a';
    board->letters[22] = 'e';
    board->letters[23] = 's';
    board->letters[24] = 'a';
*/
    
    board->owners[0] = Mine;
    board->owners[1] = Theirs;
    board->owners[2] = Mine;
    board->owners[3] = Empty;
    board->owners[4] = Mine;
    
    board->owners[5] = Theirs;
    board->owners[6] = Theirs;
    board->owners[7] = Theirs;
    board->owners[8] = Theirs;
    board->owners[9] = Empty;

    board->owners[10] = Theirs;
    board->owners[11] = Theirs;
    board->owners[12] = Theirs;
    board->owners[13] = Mine;
    board->owners[14] = Empty;
    
    board->owners[15] = Theirs;
    board->owners[16] = Theirs;
    board->owners[17] = Theirs;
    board->owners[18] = Theirs;
    board->owners[19] = Theirs;
    
    board->owners[20] = Theirs;
    board->owners[21] = Theirs;
    board->owners[22] = Theirs;
    board->owners[23] = Theirs;
    board->owners[24] = Theirs;

    Evaluator *eval = [Evaluator new];
    NSArray *words = [eval findAllWordsWithBoard:board];
    NSArray *path = [NSArray new];
    NSArray *best;
    
    Evaluation bestEvaluation = { -1000,1000,0,1000};
    for (NSString *word in words) {
        NSArray *paths = [eval allPossiblePathsForWord:word withBoard:board subpath:path];
        for (NSArray *path in paths) {
            // NSLog(@"Testing path: %@", path);
            Evaluation result = [eval evaluatePath:path withBoard:board];
            if ((result.myScore - result.theirScore) > (bestEvaluation.myScore - bestEvaluation.theirScore)) {
                best = path;
                NSLog(@"New best: %@ path: %@", word, path);
                bestEvaluation = result;
            }
        }
    }
    NSLog(@"Best: %@", best);
#endif
}

-(void)handleSingleTap:(UITapGestureRecognizer *)sender {
    if (sender.state == UIGestureRecognizerStateEnded) {
        CGPoint point = [sender locationOfTouch:0 inView:boardView];
        int cellWidth = boardView.frame.size.width / 5;
        int cellHeight = cellWidth;//boardView.frame.size.height / 5;
        int row = point.y / cellHeight;
        int col = point.x / cellWidth;
        if (row >= 5) {
            int segmentSize = boardView.frame.size.width / 3;
            if (point.x <= segmentSize) {
                _selectedOwner = Mine;
            }
            else if (point.x <= segmentSize * 2) {
                _selectedOwner = Empty;
            }
            else {
                _selectedOwner = Theirs;
            }
        }
        else {
            _cursorLocation = row*5+col;
            _board->owners[_cursorLocation] = _selectedOwner;
            [boardView setNeedsDisplay];
        }
        NSLog(@"Single Tap!!!");
    }
}

- (void)textViewDidChange:(UITextView *)aTextView {
    if ([aTextView.text isEqualToString:@"\n"]) {
        [aTextView resignFirstResponder];
        aTextView.text = @"";
        return;
    }
    NSString *text = [[[aTextView text] uppercaseString] stringByTrimmingCharactersInSet:[[NSCharacterSet uppercaseLetterCharacterSet] invertedSet]];
    char newChar = ' ';
    if ([text length] > 0) {
        newChar = [text characterAtIndex:[text length] - 1];
    }
    _board->letters[_cursorLocation] = newChar;
    if (newChar != ' ' && _cursorLocation < 24) {
        _cursorLocation++;
    }
    aTextView.text = @"";
    [boardView setNeedsDisplay];
}

-(IBAction)showWords {

}

@end
