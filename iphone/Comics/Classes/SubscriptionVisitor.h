//
//  SubscriptionVisitor.h
//  Comics
//
//  Created by Reilly Watson on 10-06-10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ComicInfo.h"

@protocol SubscriptionDelegate

-(void)subscriptionCheckedWithNewItemCount:(int)itemCount;

@end


@interface SubscriptionVisitor : NSOperation {
	NSMutableArray *visitedItems;
	ComicInfo *info;
	id delegate;
}

-(id)initWithComicInfo:(ComicInfo *)info delegate:(NSObject<SubscriptionDelegate> *)delegate;

@end
