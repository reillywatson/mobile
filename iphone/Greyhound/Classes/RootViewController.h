//
//  RootViewController.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LocationsDataRetrievalOperation.h"
#import "Location.h"

@interface RootViewController : UITableViewController<UISearchDisplayDelegate, UISearchBarDelegate, LocationsDataRetrievalDelegate> {
	NSMutableArray *itemList;
	NSOperationQueue *_opQueue;
	UISearchBar *_searchBar;
	Location *_startLocation;
	Location *_endLocation;
	NSTimer *_autoCompleteTimer;
}

@property (nonatomic, retain) Location *startLocation;

@property (nonatomic, retain) IBOutlet UISearchBar *searchBar;

@end
