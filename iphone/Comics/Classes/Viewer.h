//
//  Viewer.h
//  Comics
//
//  Created by Reilly Watson on 10-05-15.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ComicInfo.h"
#import "Downloader.h"
#import "TappableView.h"
#import "TappableWebView.h"

@interface Viewer : UIViewController<DownloaderDelegate, UIWebViewDelegate, UIActionSheetDelegate, TappableViewDelegate, TappableWebViewDelegate> {
	NSOperationQueue *opQueue;
	UIWebView *webView;
	UIBarButtonItem *prevButton;
	UIBarButtonItem *nextButton;
	UIActivityIndicatorView *spinner;
	UILabel *altText;
	UIToolbar *toolbar;
	ComicInfo *comicInfo;
	Comic *comic;
	BOOL isRandom;
}

@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) IBOutlet UIBarButtonItem *prevButton;
@property (nonatomic, retain) IBOutlet UIBarButtonItem *nextButton;
@property (nonatomic, retain) IBOutlet UILabel *altText;
@property (nonatomic, retain) IBOutlet UIActivityIndicatorView *spinner;
@property (nonatomic, retain) IBOutlet UIToolbar *toolbar;

- (BOOL) shouldTrackSwipeGesture: (UIWebView *) sender;
- (void) webView: (UIWebView *) sender trackSwipeGesture: (UITouch *) touch event: (UIEvent*) event;
- (void) webView: (UIWebView *) sender endSwipeGesture: (UITouch *) touch event: (UIEvent*) event;
- (void) webView: (UIWebView *) sender tappedWithTouch: (UITouch*) touch event: (UIEvent*) event;


-(id)initWithComic:(ComicInfo *)info;

@end
