//
//  HitItAppDelegate.h
//  HitIt
//
//  Created by Reilly Watson on 10-05-23.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import <UIKit/UIKit.h>

@class HitItViewController;

@interface HitItAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    HitItViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet HitItViewController *viewController;

@end

