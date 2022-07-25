import Kidozads from 'react-native-kidozads';

import React, {useState} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';
import AppButton from './AppButton';
import 'react-native-gesture-handler';
import {NavigationContainer} from "@react-navigation/native";

var adLoadState = {
  notLoaded: 'NOT_LOADED',
  loading: 'LOADING',
  loaded: 'LOADED',
};

var adsShowState = {
  notStarted: 'NOT_STARTED',
  completed: 'COMPLETED',
  failed: 'FAILED',
  start: 'STARTED',
  click: 'CLICKED',
};

const App = () => {

  // GameID
  const PID = Platform.select({
    ios: '8',
    android: '8',
  });

  const TOKEN = Platform.select({
    ios: 'QVBIh5K3tr1AxO4A1d4ZWx1YAe5567os',
    android: 'QVBIh5K3tr1AxO4A1d4ZWx1YAe5567os',
  });

  /*const INTERSTITIAL_AD_UNIT_ID = Platform.select({
    ios: 'DefaultInterstitial',
    android: 'DefaultInterstitial',
  });

  const REWARDED_AD_UNIT_ID = Platform.select({
    ios: 'DefaultRewardedVideo',
    android: 'DefaultRewardedVideo',
  });

  const BANNER_AD_UNIT_ID = Platform.select({
    ios: 'DefaultBanner',
    android: 'DefaultBanner',
  });*/

  // Create states
  const [isInitialized, setIsInitialized] = useState(false);
  const [interstitialAdLoadState, setInterstitialAdLoadState] = useState(adLoadState.notLoaded);
  const [KidozadshowCompleteState, setKidozadshowCompleteState] = useState(adsShowState.notStarted);
  const [interstitialRetryAttempt, setInterstitialRetryAttempt] = useState(0);
  const [rewardedAdLoadState, setRewardedAdLoadState] = useState(adLoadState.notLoaded);
  const [isNativeUIBannerShowing, setIsNativeUIBannerShowing] = useState(false);
  const [statusText, setStatusText] = useState('Initializing SDK...');


  Kidozads.initialize(PID, TOKEN, (callback) => { 
    setIsInitialized(true);
    logStatus('SDK Initialized: '+ callback);

    // Attach ad listeners for rewarded ads, and banner ads
    attachAdListeners();
  });

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

  }

  function getInterstitialButtonTitle() {
    if (interstitialAdLoadState === adLoadState.notLoaded) {
      return 'Load Interstitial';
    } else if (interstitialAdLoadState === adLoadState.loading) {
      return 'Loading...';
    } else {
      return 'Show Interstitial'; // adLoadState.loaded
    }
  }

  function getRewardedButtonTitle() {
    if (rewardedAdLoadState === adLoadState.notLoaded) {
      return 'Load Rewarded Ad';
    } else if (rewardedAdLoadState === adLoadState.loading) {
      return 'Loading...';
    } else {
      return 'Show Rewarded Ad'; // adLoadState.loaded
    }
  }

  function logStatus(status) {
    console.log(status);
    setStatusText(status);
  }

  return (
    <NavigationContainer>
      <View style={styles.container}>
        <Text style={styles.statusText}>
          {statusText}
        </Text>
        <AppButton
          title={getInterstitialButtonTitle()}
          enabled={
            isInitialized && interstitialAdLoadState !== adLoadState.loading
          }
          onPress={() => {
            Kidozads.loadInterstitial();
          }}
        />
        <AppButton
          title='Show Rewarded Ad'
          enabled={isInitialized && rewardedAdLoadState !== adLoadState.loading}
          onPress={() => {
            Kidozads.loadRewardVideo();
          }}
        />
        <AppButton
          title={isNativeUIBannerShowing ? 'Hide Native UI Banner' : 'Show Native UI Banner'}
          enabled={isInitialized}
          onPress={() => {
            if (isNativeUIBannerShowing) {
              Kidozads.unLoadBottomBanner();
            }else{
              Kidozads.loadBottomBanner();
            
            } 
            
          }}
        /> 
        
      </View>
    </NavigationContainer>
  );
};


const styles = StyleSheet.create({
  container: {
    paddingTop: 80,
    flex: 1, // Enables flexbox column layout
  },
  statusText: {
    marginBottom: 10,
    backgroundColor: 'green',
    padding: 10,
    fontSize: 20,
    textAlign: 'center',
  },
  banner: {
    // Set background color for banners to be fully functional
    backgroundColor: '#000000',
    position: 'absolute',
    width: '100%',
    height: 300,
    bottom: Platform.select({
      ios: 36, // For bottom safe area
      android: 0,
    })
  }
});

export default App;
