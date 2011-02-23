//
//  DatabaseManager.h
//  Movies
//
//  Created by Reilly Watson on 11-02-23.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>

@interface DatabaseManager : NSObject {
	sqlite3 *_db;
}

+(DatabaseManager *)sharedInstance;

@end
