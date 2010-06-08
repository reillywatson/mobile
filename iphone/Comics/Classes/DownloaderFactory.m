//
//  DownloaderFactory.m
//  Comics
//
//  Created by Reilly Watson on 10-05-16.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "DownloaderFactory.h"

@implementation DownloaderFactory


+(Downloader *)newDownloaderForComic:(ComicInfo *)info withDelegate:(NSObject<DownloaderDelegate> *)delegate withURL:(NSString *)url {
	Downloader *downloader = nil;
	if (info.comicPattern != nil) {
		downloader = [[Downloader alloc] initWithDelegate:delegate forURL:url withComicInfo:info];
	}
	else {
	// Toothpaste for Dinner!
	}
	return downloader;
}

@end
