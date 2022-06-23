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
    Kidozads.addEventListener('OninterstitialDidFailToLoad', (adInfo) => {
      logStatus('Ad fail to Loaded ' );
    });


    Kidozads.addEventListener('OninterstitialDidFailToShow', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('Ads fail to show: ' +adInfo.error);
      
    });
    Kidozads.addEventListener('OninterstitialDidOpen', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.failed);
      logStatus('Ads did open');
      
    });
    Kidozads.addEventListener('OninterstitialDidShow', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.start);
      logStatus('Ads did show  ');
    });
    Kidozads.addEventListener('OndidClickInterstitial', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did click');
    });
    Kidozads.addEventListener('OninterstitialDidClose', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did close');
    });


    //reward video Listeners
    Kidozads.addEventListener('OnrewardedVideoDidFailToShow', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('Ads fail to show' );
      
    });
    Kidozads.addEventListener('OnrewardedVideoDidOpen', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did open');
    });
    Kidozads.addEventListener('OnrewardedVideoDidClose', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('closed: ' );
      
    });
    Kidozads.addEventListener('OnrewardedVideoDidStart', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did start');
    });
    Kidozads.addEventListener('OnrewardedVideoDidEnd', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.click);
      logStatus('Ads did end');
    });
    Kidozads.addEventListener('OnrewardedDidReciveError', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('on recive error: ');
      
    });
    Kidozads.addEventListener('OndidReceiveRewardForPlacement', (adInfo) => {
      //setKidozadshowCompleteState(adsShowState.completed);
      logStatus('did Receive Reward: ');
      
    });

   

    // Banner Ad Listeners
    Kidozads.addEventListener('OnbannerDidLoad', (adInfo) => {
      logStatus('Banner ad loaded ');
      setIsNativeUIBannerShowing(!isNativeUIBannerShowing);
    });
    Kidozads.addEventListener('OnbannerDidReciveError', (adInfo) => {
      logStatus('Banner did recive error ' );
    });
    Kidozads.addEventListener('OnbannerDidFailToLoad', (adInfo) => {
      logStatus('Banner ad fail to loaded ' +adInfo.error);
    });
    Kidozads.addEventListener('OnbannerDidOpen', (Info) => {
      logStatus('Banner will present screen ');
    });
    Kidozads.addEventListener('OndidClickBanner', (adInfo) => {
      logStatus('Banner ad clicked');
    });
    Kidozads.addEventListener('OnbannerDidClose', (adInfo) => {
      logStatus('Banner full screen content dissmissed');
    });
    Kidozads.addEventListener('OnbannerWillLeaveApplication', (adInfo) => {
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
