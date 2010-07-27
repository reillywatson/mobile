//
//  DownloaderFactory.m
//  Comics
//
//  Created by Reilly Watson on 10-05-16.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "DownloaderFactory.h"
#import "DateBasedDownloader.h"
#import "ComicsKingdomDownloader.h"

@implementation DownloaderFactory

+(Downloader *)newDownloaderForComic:(ComicInfo *)info withDelegate:(NSObject<DownloaderDelegate> *)delegate withURL:(NSString *)url {
	Downloader *downloader = nil;
	if (info.downloaderType == nil) {
		downloader = [[Downloader alloc] initWithDelegate:delegate forURL:url withComicInfo:info];
	}
	else if ([info.downloaderType isEqual:@"SharingMachine"]) {
		downloader = [[DateBasedDownloader alloc] initWithDelegate:delegate forURL:url withComicInfo:info];
	}
	else if ([info.downloaderType isEqual:@"ComicsKingdom"]) {
		downloader = [[ComicsKingdomDownloader alloc] initWithDelegate:delegate forURL:url withComicInfo:info];
	}		
	return downloader;
}

@end
