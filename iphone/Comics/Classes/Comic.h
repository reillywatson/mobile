//
//  Comic.h
//  Comics
//
//  Created by Reilly Watson on 10-05-16.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Comic : NSObject {
@public
	NSString *image;
	NSString *url;
	NSString *permalink;
	NSString *nextUrl;
	NSString *prevUrl;
	NSString *title;
	NSString *altText;
	NSString *randomUrl;
}

@end
