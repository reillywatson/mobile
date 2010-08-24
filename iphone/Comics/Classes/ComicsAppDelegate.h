//
//  ComicsAppDelegate.h
//  Comics
//
//  Created by Reilly Watson on 10-05-15.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

@interface ComicsAppDelegate : NSObject <UIApplicationDelegate> {
    
    UIWindow *window;
    UINavigationController *navigationController;
	NSMutableDictionary *_favourites;
}

-(NSMutableDictionary *)favourites;
-(void)saveFavourites;

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;

@end

