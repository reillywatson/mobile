//
//  LocationsDataRetrievalOperation.h
//  Greyhound
//
//  Created by Reilly Watson on 11-01-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol LocationsDataRetrievalDelegate

-(void)locationsFound:(NSArray *)locations;
-(void)locationsError:(NSError *)error;

@end


@interface LocationsDataRetrievalOperation : NSOperation {
	NSObject<LocationsDataRetrievalDelegate> *delegate;
	NSString *searchText;
}

@property (nonatomic, readonly) NSString *searchText;

-(id)initWithDelegate:(NSObject<LocationsDataRetrievalDelegate>*) aDelegate searchText:(NSString *)searchText;

@end
