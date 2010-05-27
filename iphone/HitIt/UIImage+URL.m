//
//  UIImage+URL.m
//  HitIt
//
//  Created by Reilly Watson on 10-05-26.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "UIImage+URL.h"


@implementation UIImage (URL)

+(UIImage *)newImageFromURL:(NSString *)urlstr {
	NSURL *url = [NSURL URLWithString:urlstr];
	NSData *data = [NSData dataWithContentsOfURL:url];
	UIImage *img = [[UIImage alloc] initWithData:data];
	return img;
}

@end
