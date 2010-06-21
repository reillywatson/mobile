#import "TappableWebView.h"
#import <objc/runtime.h>

@interface TappableWebView (Private)

- (void) fireTappedWithTouch: (UITouch*) touch event: (UIEvent*) event;

- (BOOL) fireTrackSwipeGesture: (UITouch *) touch event: (UIEvent *) event;

@end

@implementation UIView (__TapHook)

- (void) __touchesBegan: (NSSet *) touches withEvent: (UIEvent *) event
{
	NSObject *o = [[self superview] superview];
	if (![o isKindOfClass: [UIWebView class]])
	{
		if ([self respondsToSelector:@selector(__touchesBegan:withEvent:)]) {
			[self __touchesBegan: touches withEvent: event];
		}
		return;
	}
	
	if (touches.count > 1)
	{
		[self touchesCancelled: touches withEvent: event];
		return;
	}
	
	UIWebView *webView = (UIWebView *) o;
	if (![webView.delegate conformsToProtocol: @protocol(TappableWebViewDelegate)])
	{
		if ([self respondsToSelector:@selector(__touchesBegan:withEvent:)]) {
			[self __touchesBegan: touches withEvent: event];
		}
		return;
	}
	
	if ([[touches anyObject] tapCount] > 1)
	{
		[self touchesCancelled: touches withEvent: event];
		return;
	}
	
	/* do nadda for now */
	if ([self respondsToSelector:@selector(__touchesBegan:withEvent:)]) {
		[self __touchesBegan: touches withEvent: event];
	}
}

- (void) __touchesMoved: (NSSet *) touches withEvent: (UIEvent *) event
{
	NSObject *o = [[self superview] superview];
	if (![o isKindOfClass: [UIWebView class]])
	{
		[self __touchesMoved: touches withEvent: event];
		return;
	}
	
	if (touches.count > 1)
	{
		[self touchesCancelled: touches withEvent: event];
		return;
	}
	
	UIWebView *webView = (UIWebView *) o;
	if (![webView.delegate conformsToProtocol: @protocol(TappableWebViewDelegate)])
	{
		[self __touchesMoved: touches withEvent: event];
		return;
	}
	
	UITouch *touch = [touches anyObject];
	
	if (touches.count == 1 && [touch tapCount] == 1)
	{
		if ([webView respondsToSelector: @selector(fireTrackSwipeGesture:event:)])
		{
			if (![(TappableWebView *) webView fireTrackSwipeGesture: touch event: event])
			{
				[self touchesCancelled: touches withEvent: event];
				return;
			}
		}
	}
	
	if ([o isKindOfClass: [TappableWebView class]] && ((TappableWebView*)webView).passMoveToSuper) [self __touchesMoved: touches withEvent: event];
}

- (void) __touchesEnded: (NSSet *) touches withEvent: (UIEvent *) event
{
	NSObject *o = [[self superview] superview];
	if (![o isKindOfClass: [UIWebView class]])
	{
		[self __touchesEnded: touches withEvent: event];
		return;
	}
	
	UIWebView *webView = (UIWebView *) o;
	if (![webView.delegate conformsToProtocol: @protocol(TappableWebViewDelegate)])
	{
		[self __touchesEnded: touches withEvent: event];
		return;
	}

	
	if ([[touches anyObject] tapCount] > 1)
	{
		[self touchesCancelled: touches withEvent: event];
		return;
	}

	/* we always need to call touchesEnded otherwise UIWebView goes crazazy */
	[self __touchesEnded: touches withEvent: event];
	
	if (touches.count == 1)
	{
		if ([webView respondsToSelector: @selector(fireTappedWithTouch:event:)])
		{
			[(TappableWebView *) webView fireTappedWithTouch: [touches anyObject] event: event];
		}
	}

}

@end

static BOOL hookInstalled = NO;

static void installHook()
{
	if (hookInstalled) return;
	
	hookInstalled = YES;
	
	Class klass = objc_getClass("UIWebDocumentView");
	{
		Method targetMethod = class_getInstanceMethod(klass, @selector(touchesBegan:withEvent:));
		Method newMethod = class_getInstanceMethod(klass, @selector(__touchesBegan:withEvent:));
		method_exchangeImplementations(targetMethod, newMethod);
	}
	{
		Method targetMethod = class_getInstanceMethod(klass, @selector(touchesEnded:withEvent:));
		Method newMethod = class_getInstanceMethod(klass, @selector(__touchesEnded:withEvent:));
		method_exchangeImplementations(targetMethod, newMethod);
	}
	{
		Method targetMethod = class_getInstanceMethod(klass, @selector(touchesMoved:withEvent:));
		Method newMethod = class_getInstanceMethod(klass, @selector(__touchesMoved:withEvent:));
		method_exchangeImplementations(targetMethod, newMethod);
	}
}

@implementation TappableWebView

@synthesize passMoveToSuper;

- (id) initWithCoder: (NSCoder*) coder
{
    if (self = [super initWithCoder: coder])
	{
		installHook();
    }
	self.passMoveToSuper = YES;
    return self;
}

- (id) initWithFrame: (CGRect) frame
{
    if (self = [super initWithFrame: frame])
	{
		installHook();
    }
	self.passMoveToSuper = YES;
    return self;
}

- (BOOL) fireTrackSwipeGesture: (UITouch *) touch event: (UIEvent *) event
{
	if (![(NSObject<TappableWebViewDelegate> *) self.delegate shouldTrackSwipeGesture: self])
	{
		return YES;
	}
	
	if ([self.delegate respondsToSelector: @selector(webView:trackSwipeGesture:event:)])
	{
		[(NSObject<TappableWebViewDelegate> *) self.delegate webView: self trackSwipeGesture: touch event: event];
	}
	
	return NO;
}

- (void) fireTappedWithTouch: (UITouch*) touch event: (UIEvent*) event
{
	if (touch.tapCount == 0 && [self.delegate respondsToSelector: @selector(webView:endSwipeGesture:event:)])
	{
		if (![(NSObject<TappableWebViewDelegate> *) self.delegate shouldTrackSwipeGesture: self])
		{
			return;
		}
		
		[(NSObject<TappableWebViewDelegate> *) self.delegate webView: self endSwipeGesture: touch event: event];
	}
	else if ([self.delegate respondsToSelector: @selector(webView:tappedWithTouch:event:)])
	{
		[(NSObject<TappableWebViewDelegate> *) self.delegate webView: self tappedWithTouch: touch event: event];
	}
}

@end