//
//  HitItViewController.h
//  HitIt
//
//  Created by Reilly Watson on 10-05-23.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AdMobDelegateProtocol.h"
#import "HotOrNotDownloadOperation.h"
#import "HotItem.h"

@interface HitItViewController : UIViewController<AdMobDelegate, DownloadOperationDelegate> {
	UIActivityIndicatorView *spinner;
	UIButton *noButton;
	UIButton *drunkButton;
	UIButton *yesButton;
	UIImageView *imageview;
	UIImageView *resultImageView;
	UIImageView *resultImageIcon;
	UILabel *resultNumRatings;
	NSOperationQueue *opqueue;
	NSMutableArray *items;
	UIView *resultsView;
	HotItem *lastItem;
	HotItem *currentItem;
}

@property (nonatomic, retain) IBOutlet UIActivityIndicatorView *spinner;
@property (nonatomic, retain) IBOutlet UIButton *noButton;
@property (nonatomic, retain) IBOutlet UIButton *drunkButton;
@property (nonatomic, retain) IBOutlet UIButton *yesButton;
@property (nonatomic, retain) IBOutlet UIImageView *imageview;
@property (nonatomic, retain) IBOutlet UIView *resultsView;
@property (nonatomic, retain) IBOutlet UIImageView *resultImageView;
@property (nonatomic, retain) IBOutlet UIImageView *resultImageIcon;
@property (nonatomic, retain) IBOutlet UILabel *resultNumRatings;


@end

