//
//  QuotesTriviaAppDelegate.h
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-05-31.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import <UIKit/UIKit.h>

@class QuotesTriviaViewController;

@interface QuotesTriviaAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    QuotesTriviaViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet QuotesTriviaViewController *viewController;

@end

