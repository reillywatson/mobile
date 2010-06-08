//
//  DownloaderFactory.h
//  Comics
//
//  Created by Reilly Watson on 10-05-16.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Downloader.h"

@interface DownloaderFactory : NSObject {
}

+(Downloader *)newDownloaderForComic:(ComicInfo *)info withDelegate:(NSObject<DownloaderDelegate> *)delegate withURL:(NSString *)url;

@end
