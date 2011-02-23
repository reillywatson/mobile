//
//  MoviesViewController.m
//  Movies
//
//  Created by Reilly Watson on 11-02-22.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "MoviesViewController.h"
#import "QuestionType.h"
#import "QuestionViewController.h"
#import "MoviesAppDelegate.h"

@implementation MoviesViewController

-(void)loadQuestions:(int)questionType {
	QuestionViewController *viewController = [[QuestionViewController alloc] initWithNibName:@"QuestionViewController" bundle:nil questionType:questionType];
    [((MoviesAppDelegate *)[UIApplication sharedApplication].delegate).window addSubview:viewController.view];
}

-(IBAction)moviesButtonClicked {
	[self loadQuestions:QuestionType_Movies];
}

-(IBAction)actorsButtonClicked {
	[self loadQuestions:QuestionType_Actors];
}

-(IBAction)actressesButtonClicked {
	[self loadQuestions:QuestionType_Actresses];
}

-(IBAction)directorsButtonClicked {
	[self loadQuestions:QuestionType_Directors];
}

/*
// The designated initializer. Override to perform setup that is required before the view is loaded.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
*/

/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView {
}
*/


/*
// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
}
*/


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
