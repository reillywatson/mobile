/*
 *  ComicInfo.h
 *  Comics
 *
 *  Created by Reilly Watson on 10-05-15.
 *  Copyright 2010 __MyCompanyName__. All rights reserved.
 *
 */

#import <Foundation/Foundation.h>
#import "Downloader.h"

@interface ComicInfo : NSObject {
	NSString *title;
	NSString *startUrl;
	NSString *downloaderType;
	
	NSString *comicPattern;
	NSString *nextComicPattern;
	NSString *prevComicPattern;
	NSString *titlePattern;
	NSString *altTextPattern;
	NSString *randomComicPattern;
	NSString *baseRandomComicURL;
	
	NSString *baseComicURL;
	NSString *basePrevNextURL;
	NSString *randomURL;
	
	// so far only used for ComicsKingdom
	BOOL sundaysOnly;
}

@property (nonatomic, retain) NSString *title;
@property (nonatomic, retain) NSString *startUrl;
@property (nonatomic, retain) NSString *downloaderType;
@property (nonatomic, retain) NSString *comicPattern;
@property (nonatomic, retain) NSString *nextComicPattern;
@property (nonatomic, retain) NSString *prevComicPattern;
@property (nonatomic, retain) NSString *titlePattern;
@property (nonatomic, retain) NSString *altTextPattern;
@property (nonatomic, retain) NSString *randomComicPattern;
@property (nonatomic, retain) NSString *baseRandomComicURL;
@property (nonatomic, retain) NSString *baseComicURL;
@property (nonatomic, retain) NSString *basePrevNextURL;
@property (nonatomic, retain) NSString *randomURL;
@property (nonatomic, assign) BOOL sundaysOnly;


@end