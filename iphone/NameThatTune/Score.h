//
//  Score.h
//  NameThatTune
//
//  Created by Reilly Watson on 11-01-17.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Score : NSObject {
@public
	NSString *_name;
	int _score;
}

@property (nonatomic, retain) NSString *name;
@property int score;

-(id)initWithName:(NSString *)name score:(int)score;

@end
