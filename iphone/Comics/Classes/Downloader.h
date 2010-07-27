//
//  Downloader.h
//  Comics
//
//  Created by Reilly Watson on 10-05-15.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ComicInfo.h"
#import "Comic.h"
#import "RegexKitLite.h"

@protocol DownloaderDelegate
-(void)comicReady:(Comic *)comic;
@end

@interface Downloader : NSOperation {
	NSString *url;
	NSObject<DownloaderDelegate> *delegate;
	Comic *comic;
	
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
}

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


-(id)initWithDelegate:(NSObject<DownloaderDelegate> *)aDelegate forURL:(NSString *)aUrl withComicInfo:(void *)info;
-(id)initWithDelegate:(NSObject<DownloaderDelegate> *)aDelegate forURL:(NSString *)aUrl;
-(Comic *)getComicSynchronously;

@end
