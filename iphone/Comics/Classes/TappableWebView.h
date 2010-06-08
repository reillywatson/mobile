
@protocol TappableWebViewDelegate
@required
- (BOOL) shouldTrackSwipeGesture: (UIWebView *) sender;
- (void) webView: (UIWebView *) sender trackSwipeGesture: (UITouch *) touch event: (UIEvent*) event;
- (void) webView: (UIWebView *) sender endSwipeGesture: (UITouch *) touch event: (UIEvent*) event;
- (void) webView: (UIWebView *) sender tappedWithTouch: (UITouch*) touch event: (UIEvent*) event;
@end

@interface TappableWebView : UIWebView {
	BOOL passMoveToSuper;
}

@property BOOL passMoveToSuper;

@end
