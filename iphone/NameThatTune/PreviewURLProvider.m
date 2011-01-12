//
//  PreviewURLProvider.m
//  NameThatTune
//
//  Created by Reilly Watson on 10-07-23.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "PreviewURLProvider.h"
#import "RegexKitLite.h"

@implementation PreviewURLProvider

-(NSString *)validatorForURL:(NSString *)url userAgent:(NSString *)userAgent {
	NSString *strRandom = [NSString stringWithFormat:@"%08X", random()];
	NSString *staticBytes = [[NSString alloc] initWithData:[NSData dataWithBase64EncodedString:@"ROkjAaKid4EUF5kGtTNn3Q=="] encoding:NSASCIIStringEncoding];
	NSArray *split = [url componentsSeparatedByString:@"/"];
	NSString *urlPart = [split objectAtIndex:[split count] - 1];
	NSString *md5Str = [NSString stringWithFormat:@"%@%@%@%@", urlPart, userAgent, staticBytes, strRandom];
	return [NSString stringWithFormat:@"%@-%@", strRandom, md5Str];
}


-(NSString *)getPreviewURLWithSearchTerms:(NSString *)searchTerms {
	
	NSString *url = [NSString stringWithFormat:@"http://ax.search.itunes.apple.com/WebObjects/MZSearch.woa/wa/search?submit=edit&restrict=true&media=music&term=%@", [searchTerms stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
	NSString *userAgent = @"iTunes/9.1.1 (Macintosh; Intel Mac OS X 10.6.3) AppleWebKit/531.22.7";
	NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
	[request setValue:userAgent forHTTPHeaderField:@"User-Agent"];
	[request setValue:[self validatorForURL:url userAgent:userAgent] forHTTPHeaderField:@"X-Apple-Validation"];
	NSError *error;
	NSString *response = [[NSString alloc] initWithData:[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:&error] encoding:NSUTF8StringEncoding];
	NSArray *components = [response captureComponentsMatchedByRegex:@"<key>preview-url.*?<string>(.*?)</string>" options:RKLDotAll range:NSMakeRange(0, [response length]) error:nil];
	if ([components count] > 1) {
		NSString *previewURL = [components objectAtIndex:1];
		NSLog(@"PREVIEW URL: %@", previewURL);
		return previewURL;
	}
	
	return nil;
}

@end
