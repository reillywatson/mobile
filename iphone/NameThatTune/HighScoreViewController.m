//
//  HighScoreViewController.m
//  NameThatTune
//
//  Created by Reilly Watson on 11-01-18.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "HighScoreViewController.h"
#import "Score.h"

@implementation HighScoreViewController


@synthesize tableView=_tableView;
@synthesize selector=_selector;

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	_opQueue = [NSOperationQueue new];
	[_opQueue addOperation:[[HighScoreRetrievalOperation alloc] initWithDelegate:self genre:@"21"]];
}

- (void)viewDidUnload {
    [super viewDidUnload];
	[_opQueue release];
	[_scores release];
	_scores = nil;
}

-(void)gotHighScores:(NSArray *)results {
	[_scores release];
	_scores = results;
	[self.tableView reloadData];
}

-(IBAction)selectorChanged {
	[self.tableView reloadData];
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	if (_scores == nil || [_scores count] == 0)
		return 0;
	return [[_scores objectAtIndex:[_selector selectedSegmentIndex]] count];
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)path {
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HighScoreCell"];
	if (cell == nil) {
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewStylePlain reuseIdentifier:@"HighScoreCell"];
	}
	Score *score = [[_scores objectAtIndex:[_selector selectedSegmentIndex]] objectAtIndex:[path row]];
	NSString *description = [NSString stringWithFormat:@"%@: %d", score.name, score.score];
	[cell.textLabel setText:description];
	return cell;
}

@end
