#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTConvert.h>
#import "KidozSDK.h"

#define KEY_WINDOW [UIApplication sharedApplication].keyWindow
#define DEVICE_SPECIFIC_ADVIEW_AD_FORMAT ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad) ? MAAdFormat.leader : MAAdFormat.banner

NS_ASSUME_NONNULL_BEGIN

/**
 * The primary bridge between JS <-> native code for the Unity ads React Native module.
 */
@interface Kidozads : RCTEventEmitter<RCTBridgeModule, KDZInitDelegate,KDZInterstitialDelegate,KDZRewardedDelegate,KDZBannerDelegate>

/**
 * Shared instance of this bridge module.
 */
@property (nonatomic, strong, readonly, class) Kidozads *shared;

@end

NS_ASSUME_NONNULL_END

