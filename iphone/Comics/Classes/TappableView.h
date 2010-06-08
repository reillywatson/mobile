#import <UIKit/UIKit.h>

@class TappableView;

@protocol TappableViewDelegate

- (void) singleTap: (TappableView *) TappableView;

@end

@interface TappableView : UIView
{
	id<TappableViewDelegate> delegate;
}

@property (nonatomic, assign) id<TappableViewDelegate> delegate;

@end