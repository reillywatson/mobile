//
//  NameThatPersonViewController.h
//  NameThatPerson
//
//  Created by Reilly Watson on 11-01-31.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PictureRetrievalOperation.h"

@interface NameThatPersonViewController : UIViewController<PictureRetrievalDelegate> {
	NSArray *people;
	NSOperationQueue *opQueue;
	UIImageView *_imageView;
	UIButton *_opt1;
	UIButton *_opt2;
	UIButton *_opt3;
	NSMutableArray *answers;
}

@property (nonatomic, retain) IBOutlet UIImageView *imageView;
@property (nonatomic, retain) IBOutlet UIButton *opt1;
@property (nonatomic, retain) IBOutlet UIButton *opt2;
@property (nonatomic, retain) IBOutlet UIButton *opt3;

-(IBAction)opt1Clicked;
-(IBAction)opt2Clicked;
-(IBAction)opt3Clicked;


@end

