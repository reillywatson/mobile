//
//  Viewer.m
//  Comics
//
//  Created by Reilly Watson on 10-05-15.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "Viewer.h"
#import "ComicInfo.h"
#import "DownloaderFactory.h"
#import "ComicsAppDelegate.h"

@interface Viewer (Private)
-(void)downloadComic:(ComicInfo *)cInfo withURL:(NSString *)url;
-(void)showComicLoadFailed;
-(void)loadRandom;
-(void)loadNewest;
@end

@implementation Viewer

@synthesize webView, prevButton, nextButton, altButton, altText, spinner, toolbar;

-(id)initWithComic:(ComicInfo *)info {
	self = [super init];
	[self setTitle:info.title];
	opQueue = [[NSOperationQueue alloc] init];
	[opQueue setMaxConcurrentOperationCount:1];
	comicInfo = [info retain];
	isRandom = NO;
	NSString *startUrl = info.startUrl;
	NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
	NSString *last = [defaults stringForKey:[info.title stringByAppendingString:@"lastViewed"]];
	if (last != nil) {
		startUrl = last;
	}
	[self downloadComic:info withURL:startUrl];
	return self;
}

-(void)downloadComic:(ComicInfo *)cInfo withURL:(NSString *)url {
	isRandom = NO;
	[spinner startAnimating];
	[self.altText setAlpha:0];
	[self.view sendSubviewToBack:webView];
	[opQueue addOperation:[DownloaderFactory newDownloaderForComic:cInfo withDelegate:self withURL:url]];
}

-(void)viewDidLoad {
	[super viewDidLoad];
	self.altText.alpha = 0;
	
	[self.webView setDelegate:self];
	
	UIBarButtonItem *moreButton = [[UIBarButtonItem alloc]
								   initWithTitle:@"More"
								   style: UIBarButtonItemStyleBordered
								   target:self
								   action:@selector(showMoreMenu)];
	[self.navigationItem setRightBarButtonItem:moreButton];
	[self.prevButton setEnabled:NO];
	[self.nextButton setEnabled:NO];
	[self.prevButton setAction:@selector(loadPrev)];
	[self.nextButton setAction:@selector(loadNext)];
	[self.altButton setAction:@selector(toggleAltText)];
	[self.altButton setEnabled:NO];
}

- (void)viewWillDisappear:(BOOL)animated {
	if (comic != nil && comic->url != nil && comicInfo != nil && comicInfo.title != nil && !isRandom) {
		[[NSUserDefaults standardUserDefaults] setObject:comic->url forKey:[comicInfo.title stringByAppendingString:@"lastViewed"]];
	}
}

-(void)loadPrev {
	[self downloadComic:comicInfo withURL:comic->prevUrl];
}

-(void)loadNext {
	[self downloadComic:comicInfo withURL:comic->nextUrl];
}

-(void)loadRandom {
	[self downloadComic:comicInfo withURL:comic->randomUrl];
	isRandom = YES;
}

-(void)loadNewest {
	[self downloadComic:comicInfo withURL:comicInfo.startUrl];
}
	

-(void)showMoreMenu {
	
	UIActionSheet *actionsheet = [[UIActionSheet alloc] initWithTitle:@"More" delegate:self cancelButtonTitle:nil destructiveButtonTitle:nil otherButtonTitles:nil];
	if (comic != nil && comic->randomUrl != nil) {
		[actionsheet addButtonWithTitle:@"Random comic"];
	}
	[actionsheet addButtonWithTitle:@"Newest comic"];
	
	NSLog(@"%@", [((ComicsAppDelegate *)[[UIApplication sharedApplication] delegate]) favourites]);
	if ([[((ComicsAppDelegate *)[[UIApplication sharedApplication] delegate]) favourites] objectForKey:comicInfo.title] == nil) {
		[actionsheet addButtonWithTitle:@"Add to favorites"];
	}
	else {
		[actionsheet addButtonWithTitle:@"Remove from favorites"];
	}
	
	[actionsheet addButtonWithTitle:@"Cancel"];
	[actionsheet setCancelButtonIndex:[actionsheet numberOfButtons] - 1];
	[actionsheet showInView:self.view];
	[actionsheet release];
}

- (BOOL) shouldAutorotateToInterfaceOrientation: (UIInterfaceOrientation) interfaceOrientation {
	return YES;
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
	NSString *title = [actionSheet buttonTitleAtIndex:buttonIndex];
	if ([title isEqual:@"Random comic"]) {
		[self loadRandom];
	}
	else if ([title isEqual:@"Newest comic"]) {
		[self loadNewest];
	}
	else if ([title isEqual:@"Add to favorites"]) {
		[[((ComicsAppDelegate *)[[UIApplication sharedApplication] delegate]) favourites] setObject:@"y" forKey:comicInfo.title];
	}
	else if ([title isEqual:@"Remove from favorites"]) {
		[[((ComicsAppDelegate *)[[UIApplication sharedApplication] delegate]) favourites] removeObjectForKey:comicInfo.title];
	}
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}

-(void)showComicLoadFailed {
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error!" message:@"Loading comic failed!  Perhaps your internet connection is unavailable?"
												   delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
	[alert show];
	[alert release];
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error {
	[self showComicLoadFailed];
	[spinner stopAnimating];
}

- (void)webViewDidFinishLoad:(UIWebView *)webView {
	[spinner stopAnimating];
}

-(void)setAltTextViewText:(NSString *)text {
	[self.altText setText:text];
	[self.altText setAlpha:0];
	[self.altButton setEnabled:(text != nil && ![text isEqual:@""])];
}

- (BOOL) shouldTrackSwipeGesture: (UIWebView *) sender {
	return NO;
}
- (void) webView: (UIWebView *) sender trackSwipeGesture: (UITouch *) touch event: (UIEvent*) event {
}
- (void) webView: (UIWebView *) sender endSwipeGesture: (UITouch *) touch event: (UIEvent*) event {
}
- (void) webView: (UIWebView *) sender tappedWithTouch: (UITouch*) touch event: (UIEvent*) event {
	[self singleTap:nil];
}

-(void)toggleAltText {
	if (self.altText.text != nil && ![self.altText.text isEqual:@""]) {
		double alpha = self.altText.alpha;
		[UIView beginAnimations:@"fadeAltText" context:nil];
		[UIView setAnimationCurve:UIViewAnimationCurveEaseIn];
		[UIView setAnimationDuration:0.3];
		self.altText.alpha = (alpha == 0) ? 1 : 0;
		[UIView commitAnimations];
	}
	else {
		self.altText.alpha = 0;
	}
}

- (void) singleTap: (TappableView *) TappableView {
}

-(void)comicReady:(Comic *)aComic {
	if (aComic == nil) {
		[self showComicLoadFailed];
		return;
	}
	comic = [aComic retain];
	[self.webView loadWithAutoZoomForImageSRC:comic->image withBaseURL:[NSURL URLWithString:comic->url]];
	[self setTitle:[comic->title stringByDecodingXMLEntities]];
	[self.prevButton setEnabled:(comic->prevUrl != nil)];
	[self.nextButton setEnabled:(comic->nextUrl != nil)];
	[self setAltTextViewText:[comic->altText stringByDecodingXMLEntities]];
		
	[self.webView becomeFirstResponder];
}

@end
