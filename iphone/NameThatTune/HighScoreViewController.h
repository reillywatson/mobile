//
//  HighScoreViewController.h
//  NameThatTune
//
//  Created by Reilly Watson on 11-01-18.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HighScoreRetrievalOperation.h"

@interface HighScoreViewController : UIViewController<UITableViewDelegate, UITableViewDataSource, HighScoreRetrievalDelegate> {
	NSOperationQueue *_opQueue;
	NSArray *_scores;
	UITableView *_tableView;
	UISegmentedControl *_selector;
}

-(IBAction) selectorChanged;

@property (nonatomic, retain) IBOutlet UITableView *tableView;
@property (nonatomic, retain) IBOutlet UISegmentedControl *selector;

@end
