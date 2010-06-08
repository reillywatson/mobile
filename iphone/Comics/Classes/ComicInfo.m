//
//  ComicInfo.m
//  Comics
//
//  Created by Reilly Watson on 10-05-16.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "ComicInfo.h"

@implementation ComicInfo
@synthesize startUrl, title;

@synthesize comicPattern, nextComicPattern, prevComicPattern, titlePattern, altTextPattern, randomComicPattern;
@synthesize baseComicURL, basePrevNextURL, randomURL;
@synthesize pickerStyle, pickerPattern;

-(id)init {
	self = [super init];
	comicPattern = nil;
	nextComicPattern = nil;
	prevComicPattern = nil;
	titlePattern = nil;
	altTextPattern = nil;
	randomURL = nil;
	randomComicPattern = nil;
	baseComicURL = @"";
	basePrevNextURL = @"";
	pickerStyle = nil;
	pickerPattern = nil;
	return self;
}

@end
