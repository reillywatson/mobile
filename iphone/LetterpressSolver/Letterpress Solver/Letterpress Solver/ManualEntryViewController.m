//
//  ManualEntryViewController.m
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import "ManualEntryViewController.h"
#import "Board.h"
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

- (void)viewDidLoad
{
    [super viewDidLoad];
    CGRect aRect = CGRectMake(-100,-100, 1, 1);
    UITextView* textView = [[UITextView alloc] initWithFrame:aRect];
    [[self view] addSubview:textView];
    [textView setDelegate:self];
    [textView becomeFirstResponder];
	// Do any additional setup after loading the view.
    Board *board = [Board new];
    board->letters[0] = 'a';
    board->letters[1] = 'd';
    board->letters[2] = 'b';
    board->letters[3] = 'f';
    board->letters[4] = 'o';
    board->letters[5] = 'y';
    board->letters[6] = 'k';
    board->letters[7] = 'h';
    board->letters[8] = 'n';
    board->letters[9] = 'z';
    board->letters[10] = 'n';
    board->letters[11] = 'm';
    board->letters[12] = 't';
    board->letters[13] = 'z';
    board->letters[14] = 'f';
    board->letters[15] = 'a';
    board->letters[16] = 'w';
    board->letters[17] = 'g';
    board->letters[18] = 'p';
    board->letters[19] = 'y';
    board->letters[20] = 'f';
    board->letters[21] = 't';
    board->letters[22] = 'h';
    board->letters[23] = 'o';
    board->letters[24] = 's';
    for (int i = 0; i < 25; i++) {
        board->owners[i] = Empty;
    }
    board->owners[15] = Theirs;
    Evaluator *eval = [Evaluator new];
 //   NSArray *words = [eval findAllWordsWithBoard:board];
   // NSLog(@"%@", words);
    NSArray *path = [NSArray new];
    NSArray *paths = [eval allPossiblePathsForWord:@"toga" withBoard:board subpath:path];
    NSArray *best;
    Evaluation bestEvaluation = { 0,0,0,0};
    for (NSArray *path in paths) {
        NSLog(@"Testing path: %@", path);
        Evaluation result = [eval evaluatePath:path withBoard:board];
        if ((result.myScore - result.theirScore) > (bestEvaluation.myScore - bestEvaluation.theirScore)) {
            best = path;
            bestEvaluation = result;
        }
    }
    NSLog(@"Best: %@", best);
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)textViewDidChange:(UITextView *)textView {
    NSString *text = [textView text];
    NSLog(@"%@", text);
}

@end
