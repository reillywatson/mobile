//
//  NameThatPersonAppDelegate.h
//  NameThatPerson
//
//  Created by Reilly Watson on 11-01-31.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@class NameThatPersonViewController;

@interface NameThatPersonAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    NameThatPersonViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet NameThatPersonViewController *viewController;

@end

