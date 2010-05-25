//
//  UIWebView+Images.m
//  HitIt
//
//  Created by Reilly Watson on 10-05-25.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "UIWebView+Images.h"


@implementation UIWebView (Images)

/*
 ** Display image in UIWebView using loadHTMLString
 */
- (void) loadImageUrlWithAutoZoom:(NSString *)src {
	
	// Create HTML string from image URL
	// Width-value is arbitrary (and found experimentally): 900 works fine for me
	NSString *htmlString = @"<html><body><img src='%@' width='900'></body></html>";
	NSString *imageHTML  = [[NSString alloc] initWithFormat:htmlString, src];
	
	// Load image in UIWebView
	[self loadHTMLString:imageHTML baseURL:nil];
}

@end
