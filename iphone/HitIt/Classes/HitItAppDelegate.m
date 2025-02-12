//
//  HitItAppDelegate.m
//  HitIt
//
//  Created by Reilly Watson on 10-05-23.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "HitItAppDelegate.h"
#import "HitItViewController.h"

@implementation HitItAppDelegate

@synthesize window;
@synthesize viewController;


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {    
    
    // Override point for customization after app launch    
    [window addSubview:viewController.view];
    [window makeKeyAndVisible];
	
	return YES;
}


- (void)dealloc {
    [viewController release];
    [window release];
    [super dealloc];
}


@end
