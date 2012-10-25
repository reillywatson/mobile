//
//  ManualEntryViewController.m
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import "ManualEntryViewController.h"

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
