//
//  UIWebView+Images.h
//  HitIt
//
//  Created by Reilly Watson on 10-05-25.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface UIWebView (Images)

- (void) loadWithAutoZoomForImageSRC:(NSString *)src withBaseURL:(NSURL *)baseURL;

@end
