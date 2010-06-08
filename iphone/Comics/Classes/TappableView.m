#import "TappableView.h"

@implementation TappableView

@synthesize delegate;

- (void) singleTap
{	
	[delegate singleTap: self];
}

- (void) touchesBegan: (NSSet *) touches withEvent: (UIEvent *)event
{
	UITouch *touch = [touches anyObject];
	NSUInteger tapCount = [touch tapCount];
	
	switch (tapCount)
	{
		case 1:
			[self performSelector: @selector(singleTap) withObject: nil afterDelay: .4];
		default:
			break;
	}
}
@end
