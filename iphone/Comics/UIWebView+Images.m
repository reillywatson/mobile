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
- (void) loadWithAutoZoomForImageSRC:(NSString *)src withBaseURL:(NSURL *)baseURL {
	
	// Create HTML string from image URL
	// Width-value is arbitrary (and found experimentally): 900 works fine for me
	NSString *htmlString = @"<html><body><img src='%@' width='900'></body></html>";
	
	// Surely there are more breaking characters than this, but this is the only one I've come across
	// (see for instance http://www.firmanproductions.com/comics/2010-06-03-343-Okay-Let's-Just-Wrap-This-Up.gif)
	// Most web browsers escape this invalid URL encoding properly, but Mobile Safari sure doesn't!
	src = [src stringByReplacingOccurrencesOfString:@"'" withString:@"%27"];
	NSString *imageHTML  = [[NSString alloc] initWithFormat:htmlString, src];
	
	// Load image in UIWebView
	[self loadHTMLString:imageHTML baseURL:baseURL];
}

@end
