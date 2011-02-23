//
//  NameThatPersonAppDelegate.h
//  NameThatPerson
//
//  Created by Reilly Watson on 11-01-31.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ListSelectionViewController;

@interface NameThatPersonAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    ListSelectionViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet ListSelectionViewController *viewController;

@end

