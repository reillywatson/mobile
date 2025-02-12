/**
 * AdViewController.m
 * AdMob iPhone SDK publisher code.
 *
 * Sample code. See AdViewController.h for instructions.
 */

#import "AdMobView.h"
#import "AdViewController.h"

@implementation AdViewController

- (void)awakeFromNib {
  // If using this object in a nib other than MainWindow.xib (like this example), keep the following [self retain] line, else this object will be autoreleased
  // out of existence -- see http://developer.apple.com/releasenotes/DeveloperTools/RN-InterfaceBuilder/index.html#//apple_ref/doc/uid/TP40001016-SW5
  // section "FAQ: What is different about NIB loading in a Cocoa Touch application?"
  // You can tell that this is occurring if you never get any calls back to -didReceiveAd: or -didFailToReceiveAd:.
  [self retain];
  self.view.hidden = YES; // when there's no ad, let our (placeholder) view be unobstructive
  adMobAd = [AdMobView requestAdWithDelegate:self]; // start a new ad request
  [adMobAd retain]; // this will be released when it loads (or fails to load)
}

- (void)dealloc {
  [refreshTimer invalidate];
  [adMobAd release];
  [super dealloc];
}

// Request a new ad. If a new ad is successfully loaded, it will be animated into location.
- (void)refreshAd:(NSTimer *)timer {
  [adMobAd requestFreshAd];
}

#pragma mark -
#pragma mark AdMobDelegate methods

- (NSString *)publisherId {
  return @"a14bf8b7dde92c0"; // this should be prefilled; if not, get it from www.admob.com
}

- (UIViewController *)currentViewController {
  return currentViewController;
}

- (UIColor *)adBackgroundColor {
  return [UIColor colorWithRed:0 green:0 blue:0 alpha:1]; // this should be prefilled; if not, provide an RGB-based UIColor
}

- (UIColor *)primaryTextColor {
  return [UIColor colorWithRed:1 green:1 blue:1 alpha:1]; // this should be prefilled; if not, provide a UIColor
}

- (UIColor *)secondaryTextColor {
  return [UIColor colorWithRed:1 green:1 blue:1 alpha:1]; // this should be prefilled; if not, provide a UIColor
}

// To receive test ads rather than real ads...
/*
 // Test ads are returned to these devices.  Device identifiers are the same used to register
 // as a development device with Apple.  To obtain a value open the Organizer 
 // (Window -> Organizer from Xcode), control-click or right-click on the device's name, and
 // choose "Copy Device Identifier".  Alternatively you can obtain it through code using
 // [UIDevice currentDevice].uniqueIdentifier.
 //
 // For example:
 //    - (NSArray *)testDevices {
 //      return [NSArray arrayWithObjects:
 //              ADMOB_SIMULATOR_ID,                             // Simulator
 //              //@"28ab37c3902621dd572509110745071f0101b124",  // Test iPhone 3GS 3.0.1
 //              //@"8cf09e81ef3ec5418c3450f7954e0e95db8ab200",  // Test iPod 2.2.1
 //              nil];
 //    }
 
- (NSArray *)testDevices {
  return [NSArray arrayWithObjects: ADMOB_SIMULATOR_ID, nil];
}
 
- (NSString *)testAdAction {
  return @"url"; // see AdMobDelegateProtocol.h for a listing of valid values here
}
*/

// Sent when an ad request loaded an ad; this is a good opportunity to attach
// the ad view to the hierachy.
- (void)didReceiveAd:(AdMobView *)adView {
  NSLog(@"AdMob: Did receive ad");
  self.view.hidden = NO;
  adMobAd.frame = [self.view convertRect:self.view.frame fromView:self.view.superview]; // put the ad in the placeholder's location
  [self.view addSubview:adMobAd];
  [refreshTimer invalidate];
  refreshTimer = [NSTimer scheduledTimerWithTimeInterval:AD_REFRESH_PERIOD target:self selector:@selector(refreshAd:) userInfo:nil repeats:YES];
}

// Sent when an ad request failed to load an ad
- (void)didFailToReceiveAd:(AdMobView *)adView {
  NSLog(@"AdMob: Did fail to receive ad");
  [adMobAd release];
  adMobAd = nil;
  // we could start a new ad request here, but it is unlikely that anything has changed in the last few seconds,
  // so in the interests of the user's battery life, let's not
}

@end