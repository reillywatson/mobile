//
//  PictureRetrievalOperation.h
//  NameThatPerson
//
//  Created by Reilly Watson on 11-01-31.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol PictureRetrievalDelegate

-(void)imageReady:(UIImage *)image;

@end


@interface PictureRetrievalOperation : NSOperation {
	NSString *query;
	NSObject<PictureRetrievalDelegate> *delegate;
}

-(id)initWithSearchQuery:(NSString *)aquery delegate:(NSObject<PictureRetrievalDelegate>*)adelegate;

@end
