//
//  MasterViewController.h
//  test
//
//  Created by Reilly Watson on 2012-08-16.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DetailViewController;

@interface MasterViewController : UITableViewController

@property (strong, nonatomic) DetailViewController *detailViewController;

@end
