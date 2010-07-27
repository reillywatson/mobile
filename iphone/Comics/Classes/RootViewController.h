//
//  RootViewController.h
//  Comics
//
//  Created by Reilly Watson on 10-05-15.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

@interface RootViewController : UITableViewController<UITableViewDelegate, UITableViewDataSource> {
	NSMutableArray *comics;
	NSMutableArray *favourites;
	NSDictionary *lastJSONObject;
	NSString *lastJSONPath;
}

@end
