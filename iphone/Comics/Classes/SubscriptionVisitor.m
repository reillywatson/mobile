//
//  SubscriptionVisitor.m
//  Comics
//
//  Created by Reilly Watson on 10-06-10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "SubscriptionVisitor.h"
#import "Comic.h"

@implementation SubscriptionVisitor

-(id)initWithComicInfo:(ComicInfo *)anInfo delegate:(NSObject<SubscriptionDelegate> *)aDelegate {
	self = [super init];
	delegate = [aDelegate retain];
	info = [anInfo retain];
	return self;
}

-(void)dealloc {
	[super dealloc];
	[delegate release];
	[info release];
}

-(void)main {
	NSString *furthestPrevURL = [[NSUserDefaults standardUserDefaults] objectForKey:[info.title stringByAppendingString:@"furthest"]];
	if (furthestPrevURL == nil) {
		return;
	}
	
}


//-parse prev link, follow prev link until a visited URL is found, storing all the links visited into an ordered list (in-memory)
//-when visiting a comic store furthest prev link ever visited


@end
