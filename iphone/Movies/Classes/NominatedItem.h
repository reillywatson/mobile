//
//  NominatedItem.h
//  Movies
//
//  Created by Reilly Watson on 11-02-23.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface NominatedItem : NSObject {
@public
	NSString *film;
	NSString *nominee;
	NSString *role;
	BOOL isWinner;
	NSString *awardName;
	int year;
	NSString *actors;
}

@end
