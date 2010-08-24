//
//  ComicsAppDelegate.m
//  Comics
//
//  Created by Reilly Watson on 10-05-15.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "ComicsAppDelegate.h"
#import "RootViewController.h"


@implementation ComicsAppDelegate

@synthesize window;
@synthesize navigationController;


#pragma mark -
#pragma mark Application lifecycle

- (void)applicationDidFinishLaunching:(UIApplication *)application {
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	NSString *path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"favourites"];
    _favourites = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
	if (_favourites == nil)
		_favourites = [NSMutableDictionary new];
	
	[window addSubview:[navigationController view]];
    [window makeKeyAndVisible];
}

-(NSMutableDictionary *)favourites {
	return _favourites;
}

-(void)saveFavourites {
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	NSString *path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"favourites"];
	[_favourites writeToFile:path atomically:NO];	
}

- (void)applicationWillTerminate:(UIApplication *)application {
	[self saveFavourites];
}


#pragma mark -
#pragma mark Memory management

- (void)dealloc {
	[navigationController release];
	[window release];
	[super dealloc];
}


@end

