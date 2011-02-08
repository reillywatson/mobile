//
//  NameThatPersonViewController.m
//  NameThatPerson
//
//  Created by Reilly Watson on 11-01-31.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "NameThatPersonViewController.h"
#import "NSMutableArray-Shuffle.h"

@interface NameThatPersonViewController (Private)

-(void)newRound;

@end


@implementation NameThatPersonViewController

@synthesize imageView = _imageView;
@synthesize opt1 = _opt1;
@synthesize opt2 = _opt2;
@synthesize opt3 = _opt3;

-(NSArray *)listForPath:(NSString *)filename {
	NSString *filestr = [NSString stringWithContentsOfFile:filename encoding:NSUTF8StringEncoding error:nil];
	NSArray *lines = [filestr componentsSeparatedByCharactersInSet:[NSCharacterSet newlineCharacterSet]];
	return lines;
}


// The designated initializer. Override to perform setup that is required before the view is loaded.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization		
    }
    return self;
}


/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView {
}
*/



// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	opQueue = [NSOperationQueue new];
	answers = [NSMutableArray new];
	people = [[self listForPath:[[NSBundle mainBundle] pathForResource:@"oscarnominees" ofType:@"txt"]] retain];
	[self newRound];
}

-(void)newRound {
	NSString *correct = [people randomElement];
	PictureRetrievalOperation *op = [[PictureRetrievalOperation alloc] initWithSearchQuery:correct delegate:self];
	[answers removeAllObjects];
	[answers addObject:correct];
	[answers addObject:[people randomElement]];
	[answers addObject:[people randomElement]];
	[opQueue addOperation:op];
}

-(void)imageReady:(NSData *)image {
	[self.imageView setImage:[UIImage imageWithData:image]];
	NSArray *buttons = [NSArray arrayWithObjects:_opt1,_opt2,_opt3,nil];
	for (UIButton *button in buttons) {
		[button setTitle:@"" forState:UIControlStateNormal];
	}
	int numAssigned = 0;
	while (numAssigned < 3) {
		UIButton *button = [buttons randomElement];
		if ([[button titleForState:UIControlStateNormal] isEqualToString:@""]) {
			NSLog(@"SETTING: %@", [answers objectAtIndex:numAssigned]);
			[button setTitle:[answers objectAtIndex:numAssigned] forState:UIControlStateNormal];
			numAssigned++;
		}
	}
}

-(void)answerSelected:(UIButton *)button {
	UIAlertView *alertView;
	if ([[button titleForState:UIControlStateNormal] isEqualToString:[answers objectAtIndex:0]]) {
		alertView = [[UIAlertView alloc] initWithTitle:@"NameThatPerson" message:@"RIGHT!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
	}
	else {
		alertView = [[UIAlertView alloc] initWithTitle:@"NameThatPerson" message:[NSString stringWithFormat:@"WRONG, IT WAS %@!",[answers objectAtIndex:0]] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
	}
	[alertView show];
	[self newRound];
}

-(IBAction)opt1Clicked {
	[self answerSelected:_opt1];
}
-(IBAction)opt2Clicked {
	[self answerSelected:_opt2];
}
-(IBAction)opt3Clicked {
	[self answerSelected:_opt3];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}

@end
