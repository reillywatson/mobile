//
//  DetailViewController.h
//  test
//
//  Created by Reilly Watson on 2012-08-16.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DetailViewController : UIViewController <UISplitViewControllerDelegate>

@property (strong, nonatomic) id detailItem;

@property (weak, nonatomic) IBOutlet UILabel *detailDescriptionLabel;
@end
