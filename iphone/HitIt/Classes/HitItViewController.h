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

@interface HitItViewController : UIViewController<AdMobDelegate, UIWebViewDelegate, DownloadOperationDelegate> {
	UIActivityIndicatorView *spinner;
	UIButton *noButton;
	UIButton *drunkButton;
	UIButton *yesButton;
	UIWebView *webview;
	NSOperationQueue *opqueue;
	NSMutableArray *items;
}

@property (nonatomic, retain) IBOutlet UIActivityIndicatorView *spinner;
@property (nonatomic, retain) IBOutlet UIButton *noButton;
@property (nonatomic, retain) IBOutlet UIButton *drunkButton;
@property (nonatomic, retain) IBOutlet UIButton *yesButton;
@property (nonatomic, retain) IBOutlet UIWebView *webview;


@end

