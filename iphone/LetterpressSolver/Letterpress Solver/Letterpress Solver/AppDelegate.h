//
//  AppDelegate.h
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate> {
    NSArray *words;
}

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) NSArray *words;

@end
