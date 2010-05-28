//
//  HotOrNotDownloadOperation.m
//  HitIt
//
//  Created by Reilly Watson on 10-05-23.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "HotOrNotDownloadOperation.h"
#import "RegexKitLite.h"
#import "HotItem.h"

@implementation HotOrNotDownloadOperation

-(id)initWithDelegate:(NSObject<DownloadOperationDelegate> *)aDelegate rateID:(NSString *)lastRateID imageURL:(NSString *)lastImageURL {
	self = [super init];
	delegate = [aDelegate retain];
	rateID = [lastRateID retain];
	imageURL = [lastImageURL retain];
	return self;
}

-(void)dealloc {
	[super dealloc];
	[delegate release];
	[rateID release];
	[imageURL release];
}

-(void)addRatingResults:(NSString *)str forItem:(HotItem *)hotitem {
	NSString *pattern = @"class=\"score\".*?>(.*?)</div>.*?Based on (.*?) votes";
	NSArray *components = [str captureComponentsMatchedByRegex:pattern options:RKLDotAll range:NSMakeRange(0, [str length]) error:nil];
	if ([components count] > 1) {
		hotitem->resultAverage = [[[components objectAtIndex:1] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] retain];
		hotitem->resultTotals = [[components objectAtIndex:2] retain];
	}
}

-(void)main {
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://www.hotornot.com/?state=rate&sel_sex=female&minage=18&maxage=30"]];
	[request setHTTPMethod:@"POST"];
	if (rateID != nil) {
		NSString *httpBodyString = [NSString stringWithFormat:@"rate=1&state=rate&minR=9&rateAction=vote&ratee=%@&r%@=9", rateID, rateID];
		[request setHTTPBody:[httpBodyString dataUsingEncoding:NSISOLatin1StringEncoding]];
	}
	
	NSError *error;
	NSData *data=[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error];
	if (error != nil) {
		[delegate requestFailedWithError:error];
		return;
	}
	HotItem *hotitem = [HotItem new];
	NSString *str = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
	NSString *pattern = @"<img id='mainPic'.*?src='(.*?)'.*?>.*?<input type=\"hidden\" name=\"ratee\" value=\"(.*?)\".*?>";
	NSArray *components = [str captureComponentsMatchedByRegex:pattern options:RKLDotAll range:NSMakeRange(0, [str length]) error:nil];
	if ([components count] > 1) {
		hotitem->imageURL = [[components objectAtIndex:1] retain];
		hotitem->rateID = [[components objectAtIndex:2] retain];
		hotitem->image = [UIImage newImageFromURL:hotitem->imageURL];
	}
	if (rateID != nil) {
		[self addRatingResults:str forItem:hotitem];
	}
	[delegate itemReady:hotitem];
}
@end
