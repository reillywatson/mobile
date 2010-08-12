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
@synthesize baseComicURL, basePrevNextURL, randomURL, baseRandomComicURL, downloaderType, sundaysOnly, useReferer;

-(id)init {
	self = [super init];
	comicPattern = nil;
	nextComicPattern = nil;
	prevComicPattern = nil;
	titlePattern = nil;
	altTextPattern = nil;
	randomURL = nil;
	randomComicPattern = nil;
	baseRandomComicURL = @"";
	baseComicURL = @"";
	basePrevNextURL = @"";
	downloaderType = nil;
	sundaysOnly = NO;
	useReferer = YES;
	return self;
}

@end
