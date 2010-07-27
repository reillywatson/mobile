//
//  Downloader.m
//  Comics
//
//  Created by Reilly Watson on 10-05-15.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//
#import "Downloader.h"
#import "ComicInfo.h"

@interface Downloader (Protected)

-(void)parseComic:(NSString *)str;
-(void)parseTitle:(NSString *)str;
-(void)parseAltText:(NSString *)str;
-(void)parsePermalink:(NSString *)str;
-(void)parseRandomURL:(NSString *)str;
@end


@implementation Downloader

@synthesize comicPattern, nextComicPattern, prevComicPattern, titlePattern, altTextPattern, randomComicPattern;
@synthesize baseComicURL, basePrevNextURL, randomURL, baseRandomComicURL;


-(void)parseComic:(NSString *)str {
	NSString *p = [self comicPattern];
	NSLog(@"COMIC PATTERN: %@", p);
	NSLog(@"BASE COMIC URL: %@", baseComicURL);
	if (p == nil)
		return;
	NSArray *components = [str captureComponentsMatchedByRegex:p options:RKLDotAll range:NSMakeRange(0, [str length]) error:nil];
	if ([components count] > 1) {
		NSString *imageURL = [[components objectAtIndex:1] stringByReplacingOccurrencesOfString:@"&amp;" withString:@"&"];
		comic->image = [[baseComicURL stringByAppendingString:imageURL] retain];
		NSLog(@"COMIC URL: %@", comic->image);
	}
	else {
		NSLog(@"NO COMIC FOUND!!!  HTML: %@", str);
	}
}

-(void)parsePrevLink:(NSString *)str {
	NSString *p = [self prevComicPattern];
	if (p == nil)
		return;
	NSArray *components = [str captureComponentsMatchedByRegex:p options:RKLDotAll range:NSMakeRange(0, [str length]) error:nil];
	if ([components count] > 1) {
		NSString *prev = [components objectAtIndex:1];
		if (![prev isEqual:@"#"] && ![prev isEqual:@"/"] && ![prev isEqual:url] && [prev length] > 0) {
			comic->prevUrl = [[[self basePrevNextURL] stringByAppendingString:prev] retain];
			NSLog(@"PREV URL: %@", comic->prevUrl);
		}
	}
}

-(void)parseNextLink:(NSString *)str {
	NSString *p = [self nextComicPattern];
	if (p == nil)
		return;
	NSArray *components = [str captureComponentsMatchedByRegex:p options:RKLDotAll range:NSMakeRange(0, [str length]) error:nil];
	if ([components count] > 1) {
		NSString *next = [components objectAtIndex:1];
		if (![next isEqual:@"#"] && ![next isEqual:@"/"] && ![next isEqual:url] && [next length] > 0) {
			comic->nextUrl = [[[self basePrevNextURL] stringByAppendingString:next] retain];
			NSLog(@"NEXT URL: %@", comic->nextUrl);
		}
	}
}

-(void)parseTitle:(NSString *)str {
	NSString *p = [self titlePattern];
	if (p == nil)
		return;
	NSArray *components = [str captureComponentsMatchedByRegex:p options:RKLDotAll range:NSMakeRange(0, [str length]) error:nil];
	if ([components count] > 1) {
		comic->title = [[components objectAtIndex:1] retain];
	}
}

-(void)parseAltText:(NSString *)str {
	NSString *p = [self altTextPattern];
	if (p == nil)
		return;
	NSArray *components = [str captureComponentsMatchedByRegex:p options:RKLDotAll range:NSMakeRange(0, [str length]) error:nil];
	if ([components count] > 1) {
		comic->altText = [[components objectAtIndex:1] retain];
	}
}

-(void)parseRandomURL:(NSString *)str {
	NSString *rurl = [self randomURL];
	if (rurl != nil) {
		comic->randomUrl = [rurl retain];
		return;
	}
	NSString *p = [self randomComicPattern];
	if (p == nil)
		return;
	NSArray *components = [str captureComponentsMatchedByRegex:p options:RKLDotAll range:NSMakeRange(0, [str length]) error:nil];
	if ([components count] > 1) {
		comic->randomUrl = [[[self baseRandomComicURL] stringByAppendingString:[components objectAtIndex:1]] retain];
	}
}

-(void)parsePermalink:(NSString *)str {
}


-(id)initWithDelegate:(NSObject<DownloaderDelegate> *)aDelegate forURL:(NSString *)aUrl withComicInfo:(void *)info {
	self = [super init];
	delegate = [aDelegate retain];
	url = [aUrl retain];
	comic = [Comic new];
	ComicInfo *comicInfo = (ComicInfo *)info;
	comicPattern = comicInfo.comicPattern;
	nextComicPattern = comicInfo.nextComicPattern;
	prevComicPattern = comicInfo.prevComicPattern;
	titlePattern = comicInfo.titlePattern;
	altTextPattern = comicInfo.altTextPattern;
	randomComicPattern = comicInfo.randomComicPattern;
	baseComicURL = comicInfo.baseComicURL;
	basePrevNextURL = comicInfo.basePrevNextURL;
	baseRandomComicURL = comicInfo.baseRandomComicURL;
	randomURL = comicInfo.randomURL;
	if (baseComicURL == nil)
		baseComicURL = @"";
	if (basePrevNextURL == nil)
		basePrevNextURL = @"";
	return self;
}

-(id)initWithDelegate:(NSObject<DownloaderDelegate> *)aDelegate forURL:(NSString *)aUrl {
	self = [super init];
	delegate = [aDelegate retain];
	url = [aUrl retain];
	comic = [Comic new];
	return self;
}

-(Comic *)getComicSynchronously {
	NSLog(@"REQUESTING %@", url);
	comic->url = [url retain];
	NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
	NSError *error;
	NSData *data=[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error];
	NSString *str = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
	NSLog(@"received response with %d bytes", [str length]);
	[self parseComic:str];
	[self parsePrevLink:str];
	[self parseNextLink:str];
	[self parseTitle:str];
	[self parseAltText:str];
	[self parsePermalink:str];
	[self parseRandomURL:str];
	return comic;
}

-(void)main {
	[self getComicSynchronously];
	[delegate performSelectorOnMainThread:@selector(comicReady:) withObject:comic waitUntilDone:NO];
}

@end
