//
//  FoundWordsTableViewController.m
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-26.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import "FoundWordsTableViewController.h"
#import "FoundWordsTableViewCell.h"
#import "Evaluator.h"
#import "ManualEntryViewController.h"

@interface FoundWordsTableViewController ()

@end

@implementation FoundWordsTableViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    ManualEntryViewController *view = sender;
    self.board = view.board;
    self.paths = view.foundWords;
    [self.tableView reloadData];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.tableView reloadData];
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [_paths count] - 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"com.reilly.boardcells";
    FoundWordsTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    int row = [indexPath row] + 1;
    NSArray *pathForRow = [_paths objectAtIndex:row];
    NSMutableString *string = [NSMutableString new];
    for (NSNumber* num in pathForRow) {
        [string appendFormat:@"%c", _board->letters[[num intValue]]];
    }
    Evaluation eval = [Evaluator evaluatePath:pathForRow withBoard:_board];
    cell.word.text = string;
    cell.myScore.text = [NSString stringWithFormat:@"%d", eval.myScore];
    cell.theirScore.text = [NSString stringWithFormat:@"%d", eval.theirScore];
    cell.board.board = [_board boardByAddingPath:pathForRow];
    [cell.board setNeedsDisplay];
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
}

@end
