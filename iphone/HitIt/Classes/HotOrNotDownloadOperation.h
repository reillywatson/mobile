//
//  HotOrNotDownloadOperation.h
//  HitIt
//
//  Created by Reilly Watson on 10-05-23.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HotItem.h"

@protocol DownloadOperationDelegate

-(void)requestFailedWithError:(NSError *)error;
-(void)itemReady:(HotItem *)item;

@end


@interface HotOrNotDownloadOperation : NSOperation {
	id delegate;
	NSString *rateID;
	NSString *imageURL;
}

-(id)initWithDelegate:(NSObject<DownloadOperationDelegate> *)aDelegate rateID:(NSString *)lastRateID imageURL:(NSString *)lastImageURL;

@end
