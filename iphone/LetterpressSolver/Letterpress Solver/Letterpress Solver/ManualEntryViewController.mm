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
    textView.returnKeyType = UIReturnKeyDone;
    [[self view] addSubview:textView];
    [textView setDelegate:self];
    [textView becomeFirstResponder];
    
    _cursorLocation = 0;
    
    _board = [Board new];
    _selectedOwner = Empty;
    
    self.boardView->_board = _board;
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [self.boardView addGestureRecognizer:singleTap];
}

-(void)handleSingleTap:(UITapGestureRecognizer *)sender {
    if (sender.state == UIGestureRecognizerStateEnded) {
        CGPoint point = [sender locationOfTouch:0 inView:self.boardView];
        int cellWidth = self.boardView.frame.size.width / 5;
        int cellHeight = self.boardView.frame.size.height / 5;
        int row = point.y / cellHeight;
        int col = point.x / cellWidth;
        if (row < 5 && col < 5) {
            _cursorLocation = row*5+col;
            _board->owners[_cursorLocation] = _selectedOwner;
            [self.boardView setNeedsDisplay];
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
    [self.boardView setNeedsDisplay];
}

-(IBAction)graySelected {
    _selectedOwner = Empty;
}
-(IBAction)redSelected {
    _selectedOwner = Theirs;
}
-(IBAction)blueSelected {
    _selectedOwner = Mine;
}

-(IBAction)showWords {
    NSArray *words = [Evaluator findAllWordsWithBoard:_board];
    NSArray *path = [NSArray new];
    NSMutableArray *paths = [NSMutableArray new];
    for (NSString *word in words) {
        [paths addObjectsFromArray:[Evaluator allPossiblePathsForWord:word withBoard:_board subpath:path]];
    }
    NSArray *sorted = [paths sortedArrayUsingComparator:^NSComparisonResult(NSArray *firstPath, NSArray *secondPath) {
        return [Evaluator comparePath:firstPath withPath:secondPath withBoard:_board];
    }];
    NSLog(@"SORTED: %@", sorted);
}

@end
