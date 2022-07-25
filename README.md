# react-native-kidozads

kidoz ads

## Installation

```sh
npm install react-native-kidozads
```

## Usage

```js
import Kidozads from "react-native-kidozads";

  
  //Initialize SDK
  Kidozads.initialize(PID, TOKEN, (callback) => { 
    setIsInitialized(true);
    logStatus('SDK Initialized: '+ callback);

    // Attach ad listeners for rewarded ads, and banner ads
    attachAdListeners();//need to call removeEventListener to remove listeners.
  });
// ...

    //Attach ad Listeners for rewarded ads, and banner ads, and so on.
    function attachAdListeners() {
    Kidozads.addEventListener('onInterstitialDidFailToLoad', (adInfo) => {
      logStatus('Ad fail to Loaded ' );
    });


    Kidozads.addEventListener('onInterstitialDidFailToShow', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('Ads fail to show: ' +adInfo.error);
      
    });
    Kidozads.addEventListener('onInterstitialDidOpen', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.failed);
      logStatus('Ads did open');
      
    });
    Kidozads.addEventListener('onInterstitialDidShow', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.start);
      logStatus('Ads did show  ');
    });
    Kidozads.addEventListener('onDidClickInterstitial', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did click');
    });
    Kidozads.addEventListener('onInterstitialDidClose', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did close');
    });


    //reward video Listeners
    Kidozads.addEventListener('onRewardedVideoDidFailToShow', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('Ads fail to show' );
      
    });
    Kidozads.addEventListener('onRewardedVideoDidOpen', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did open');
    });
    Kidozads.addEventListener('onRewardedVideoDidClose', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('closed: ' );
      
    });
    Kidozads.addEventListener('onRewardedVideoDidStart', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did start');
    });
    Kidozads.addEventListener('onRewardedVideoDidEnd', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did end');
    });
    Kidozads.addEventListener('onRewardedDidReciveError', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('on recive error: ');
      
    });
    Kidozads.addEventListener('onDidReceiveRewardForPlacement', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('did Receive Reward: ');
      
    });

   

    // Banner Ad Listeners
    Kidozads.addEventListener('onBannerDidLoad', (adInfo) => {
      logStatus('Banner ad loaded ');
      setIsNativeUIBannerShowing(!isNativeUIBannerShowing);
    });
    Kidozads.addEventListener('onBannerDidReciveError', (adInfo) => {
      logStatus('Banner did recive error ' );
    });
    Kidozads.addEventListener('onBannerDidFailToLoad', (adInfo) => {
      logStatus('Banner ad fail to loaded ' +adInfo.error);
    });
    Kidozads.addEventListener('onBannerDidOpen', (Info) => {
      logStatus('Banner will present screen ');
    });
    Kidozads.addEventListener('OndidClickBanner', (adInfo) => {
      logStatus('Banner ad clicked');
    });
    Kidozads.addEventListener('onBannerDidClose', (adInfo) => {
      logStatus('Banner full screen content dissmissed');
    });
    Kidozads.addEventListener('onBannerWillLeaveApplication', (adInfo) => {
      logStatus('Called when a user would be taken out of the application context');
    });
    
  //load interstitial ads：
  Kidozads.loadInterstitial();
  
  //load reward ads：
    Kidozads.loadRewardVideo();
    
　//load Banner:
　Kidozads.loadBottomBanner();
　
　ios: test with real devices

  For specific usage, please refer to example.
  How To Run example:
  1,$ cd example && npm install
  2,$ cd ios && pod install
  3,$ cd .. && npm run ios or npm run android

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
