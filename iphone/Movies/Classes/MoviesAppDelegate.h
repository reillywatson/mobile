//
//  MoviesAppDelegate.h
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@class MoviesViewController;

@interface MoviesAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    MoviesViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet MoviesViewController *viewController;

@end

