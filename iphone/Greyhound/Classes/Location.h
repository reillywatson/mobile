//
//  Location.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Location : NSObject {
@public
	NSString *name;
	NSString *locationId;
}

-(id)initWithName:(NSString *)aName locationID:(NSString *)aLocationID;

@end
