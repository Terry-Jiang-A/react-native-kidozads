#import "Kidozads.h"

#define ROOT_VIEW_CONTROLLER (UIApplication.sharedApplication.keyWindow.rootViewController)


@interface Kidozads()

// Parent Fields
@property (nonatomic, assign, getter=isPluginInitialized) BOOL pluginInitialized;
@property (nonatomic, assign, getter=isSDKInitialized) BOOL sdkInitialized;


// Banner Fields
// This is the Ad Unit or Placement that will display banner ads:
@property (strong) NSString* placementId;


@property (nonatomic, strong) UIView *safeAreaBackground;

// React Native's proposed optimizations to not emit events if no listeners
@property (nonatomic, assign) BOOL hasListeners;

@end
@implementation Kidozads

static NSString *const SDK_TAG = @"Kidoz　Sdk";
static NSString *const TAG = @"Kidoz Ads";

RCTResponseSenderBlock _onInitialized = nil;

static Kidozads *KidozShared; // Shared instance of this bridge module.

RCT_EXPORT_MODULE()

// `init` requires main queue b/c of UI code
+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

// Invoke all exported methods from main queue
- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (Kidozads *)shared
{
    return KidozShared;
}

- (instancetype)init
{
    self = [super init];
    if ( self )
    {
        KidozShared = self;
    }
    return self;
}

RCT_EXPORT_BLOCKING_SYNCHRONOUS_METHOD(isInitialized)
{
    return @([self isPluginInitialized] && [self isSDKInitialized]);
}

RCT_EXPORT_METHOD(initialize :(NSString *)PID :(NSString *)token :(RCTResponseSenderBlock)callback)
{
    // Guard against running init logic multiple times
    if ( [self isPluginInitialized] )
    {
        callback(@[@" Unity Sdk has Initiallized"]);
        return;
    }
    
    self.pluginInitialized = YES;
    _onInitialized = callback;
    
    [[KidozSDK instance]initializeWithPublisherID:PID securityToken:token withDelegate:self];
    
}

#pragma mark - Interstitials

RCT_EXPORT_METHOD(loadInterstitial)
{
    if([[KidozSDK instance]isInterstitialInitialized]){
        [[KidozSDK instance]loadInterstitial];
    }else{
        NSLog(@"Interstitial Not Initialized");

    }
}

RCT_EXPORT_METHOD(loadRewardVideo)
{
    if([[KidozSDK instance]isRewardedInitialized]){
        [[KidozSDK instance]loadRewarded];
    }else{
        NSLog(@"Rewarded not Initialized");
    }
    
}

RCT_EXPORT_METHOD(loadBottomBanner:(NSString *)adUnitIdentifier)
{
    if([[KidozSDK instance]isBannerInitialized]){
        [[KidozSDK instance]loadBanner];
    }else{
        NSLog(@"Banner Not Initialized");

    }
    
}

RCT_EXPORT_METHOD(unLoadBottomBanner)
{
    [[KidozSDK instance]hideBanner];
}


#pragma mark - Init
-(void)onInitSuccess{
    
    [[KidozSDK instance] initializeInterstitialWithDelegate:self];
    [[KidozSDK instance] initializeRewardedWithDelegate:self];
    
    [[KidozSDK instance]initializeBannerWithDelegate:self withViewController:ROOT_VIEW_CONTROLLER];
    [[KidozSDK instance]setBannerPosition:BOTTOM_CENTER];
    
    self.sdkInitialized = YES;
    _onInitialized(@[@" success"]);
    
}

-(void)onInitError:(NSString *)error{
    self.sdkInitialized = NO;
        _onInitialized(@[@"failed: @%", error]);
}

#pragma mark -Banner
- (void)bannerDidClose {
    [self sendReactNativeEventWithName: @"OnbannerDidClose" body: nil];
    
}

- (void)bannerDidInitialize {
    NSLog(@"banner is Initialized");
    
}

- (void)bannerDidOpen {
    [self sendReactNativeEventWithName: @"OnbannerDidOpen" body: nil];
    
}

- (void)bannerDidReciveError:(NSString *)errorMessage {
    [self sendReactNativeEventWithName: @"OnbannerDidReciveError" body: @{@"error" : errorMessage}];
    
}

- (void)bannerIsReady {
    [[KidozSDK instance]showBanner];
    [self sendReactNativeEventWithName: @"OnbannerDidLoad" body: nil];
}

- (void)bannerLeftApplication {
    [self sendReactNativeEventWithName: @"OnbannerWillLeaveApplication" body: nil];
    
}

- (void)bannerLoadFailed {
    [self sendReactNativeEventWithName: @"OnbannerDidFailToLoad" body: nil];
    
}

- (void)bannerShowFailed{
    NSLog(@"bannerShowFailed");
}

- (void)bannerReturnedWithNoOffers {
    NSLog(@"bannerReturnedWithNoOffers");
}

#pragma mark Reward
-(void)rewardedDidInitialize{
    NSLog(@"Rewarded is Initialized");
    
}

-(void)rewardedDidClose{
    [self sendReactNativeEventWithName: @"OnrewardedVideoDidClose" body: nil];
}

-(void)rewardedDidOpen{
    [self sendReactNativeEventWithName: @"OnrewardedVideoDidOpen" body: nil];
}

-(void)rewardedIsReady{
    [[KidozSDK instance]showRewarded:ROOT_VIEW_CONTROLLER];
    
}

-(void)rewardedReturnedWithNoOffers{
    NSLog(@"rewardedReturnedWithNoOffers");
    
}

-(void)rewardedDidPause{
    NSLog(@"rewardedDidPause");
    
}

-(void)rewardedDidResume{
    NSLog(@"rewardedDidResume");
    
}


-(void)rewardedDidReciveError:(NSString*)errorMessage{
    [self sendReactNativeEventWithName: @"OnrewardedDidReciveError" body: @{@"error" : errorMessage}];
    
}

-(void)rewardReceived{
    NSLog(@"rewardReceived");
    
}

-(void)rewardedStarted{
    [self sendReactNativeEventWithName: @"OnrewardedVideoDidStart" body: nil];
    
}

- (void)rewardedLoadFailed {
    [self sendReactNativeEventWithName: @"OnrewardedVideoDidFailToShow" body: nil];
}

- (void)rewardedLeftApplication {
    NSLog(@"rewardedLeftApplication");
}

#pragma mark Interstitial
-(void)interstitialDidInitialize{
    NSLog(@"Interstitial Initialized");
    
}

-(void)interstitialDidClose{
    [self sendReactNativeEventWithName: @"OninterstitialDidClose" body: nil];
}

-(void)interstitialDidOpen{
    [self sendReactNativeEventWithName: @"OninterstitialDidOpen" body: nil];
}


-(void)interstitialIsReady{
    
    [[KidozSDK instance]showInterstitial:ROOT_VIEW_CONTROLLER];
    
}

-(void)interstitialDidPause{
    NSLog(@"interstitialDidPause");
    
}

-(void)interstitialDidResume{
    NSLog(@"interstitialDidResume");
}



-(void)interstitialReturnedWithNoOffers{
    NSLog(@"interstitialReturnedWithNoOffers");
}


-(void)interstitialDidReciveError:(NSString*)errorMessage{
    [self sendReactNativeEventWithName: @"OninterstitialDidFailToLoad" body: @{@"error" : errorMessage}];
    
}

- (void)interstitialLoadFailed {
    [self sendReactNativeEventWithName: @"OninterstitialDidFailToLoad" body: nil];
}

- (void)interstitialLeftApplication {
    NSLog(@"interstitialLeftApplication");
    
}

#pragma mark - React Native Event Bridge

- (void)sendReactNativeEventWithName:(NSString *)name body:(NSDictionary<NSString *, id> *)body
{
    [self sendEventWithName: name body: body];
}

// From RCTBridgeModule protocol
- (NSArray<NSString *> *)supportedEvents
{
    return @[@"OninterstitialDidFailToLoad",
             @"OninterstitialDidFailToShow",
             
             @"OninterstitialDidOpen",
             @"OninterstitialDidShow",
             @"OndidClickInterstitial",
             @"OninterstitialDidClose",
             
             @"OnrewardedVideoDidFailToShow",
             @"OnrewardedVideoDidOpen",
             @"OnrewardedVideoDidClose",
             @"OnrewardedVideoDidStart",
             @"OndidReceiveRewardForPlacement",
             
             @"OnrewardedVideoDidEnd",
             @"OnrewardedDidReciveError",
             @"OnbannerDidFailToLoad",
             @"OnbannerDidLoad",
             @"OnbannerDidReciveError",
             @"OnbannerDidOpen",
             @"OnbannerDidClose",
             @"OnbannerWillLeaveApplication",];
}

- (void)startObserving
{
    self.hasListeners = YES;
}

- (void)stopObserving
{
    self.hasListeners = NO;
}

@end
