package com.reactnativekidozads;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import static com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

import com.kidoz.sdk.api.KidozInterstitial;
import com.kidoz.sdk.api.KidozSDK;
import com.kidoz.sdk.api.interfaces.SDKEventListener;
import com.kidoz.sdk.api.ui_views.interstitial.BaseInterstitial;
import com.kidoz.sdk.api.ui_views.kidoz_banner.KidozBannerListener;
import com.kidoz.sdk.api.ui_views.new_kidoz_banner.BANNER_POSITION;
import com.kidoz.sdk.api.ui_views.new_kidoz_banner.KidozBannerView;

import java.util.concurrent.TimeUnit;
import android.util.Log;
import android.text.TextUtils;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.content.pm.ActivityInfo;

@ReactModule(name = KidozadsModule.NAME)
public class KidozadsModule extends ReactContextBaseJavaModule {
    public static final String NAME = "Kidozads";
    private static final String SDK_TAG = "Kidoz Ads Sdk";
    private static final String TAG     = "Kidoz Ads Module";


    public static  KidozadsModule instance;
    private static Activity          sCurrentActivity;
    private RelativeLayout bottomBannerView;
    public static final int BANNER_WIDTH = 320;
    public static final int BANNER_HEIGHT = 50;
    /**
     * Kidoz interstitial instance
     */
    private KidozInterstitial mKidozInterstitial;

    /**
     * Kidoz rewarded instance
     */
    private KidozInterstitial mKidozRewarded;

    /**
     * Kidoz banner instance
     */
    private KidozBannerView mKidozBannerView;

    private Callback mInitCallback;

    // Parent Fields
    private boolean                  isPluginInitialized;
    private boolean                  isSdkInitialized;

    public static KidozadsModule getInstance()
    {
      return instance;
    }

    public KidozadsModule(ReactApplicationContext reactContext) {
        super(reactContext);
      instance = this;
      sCurrentActivity = reactContext.getCurrentActivity();
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


    @Nullable
    private Activity maybeGetCurrentActivity()
    {
      // React Native has a bug where `getCurrentActivity()` returns null: https://github.com/facebook/react-native/issues/18345
      // To alleviate the issue - we will store as a static reference (WeakReference unfortunately did not suffice)
      if ( getReactApplicationContext().hasCurrentActivity() )
      {
        sCurrentActivity = getReactApplicationContext().getCurrentActivity();
      }

      return sCurrentActivity;
    }


    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod(isBlockingSynchronousMethod = true)
    public boolean isInitialized()
    {
      return isPluginInitialized && isSdkInitialized;
    }

    @ReactMethod
    public void initialize(final String pid, final String token, final Callback callback)
    {
      // Check if Activity is available
      Activity currentActivity = maybeGetCurrentActivity();
      if ( currentActivity != null )
      {
        performInitialization( pid, token, currentActivity, callback );
      }
      else
      {
        Log.d( TAG, "No current Activity found! Delaying initialization..." );

        new Handler().postDelayed(new Runnable()
        {
          @Override
          public void run()
          {
            Context contextToUse = maybeGetCurrentActivity();
            if ( contextToUse == null )
            {
              Log.d( TAG,"Still unable to find current Activity - initializing SDK with application context" );
              contextToUse = getReactApplicationContext();
            }

            performInitialization( pid, token, contextToUse, callback );
          }
        }, TimeUnit.SECONDS.toMillis( 3 ) );
      }
    }

    @ReactMethod()
    public void loadInterstitial()
    {
      mKidozInterstitial = new KidozInterstitial(sCurrentActivity, KidozInterstitial.AD_TYPE.INTERSTITIAL);
      mKidozInterstitial.loadAd();

      mKidozInterstitial.setOnInterstitialEventListener(new BaseInterstitial.IOnInterstitialEventListener()
      {
        @Override
        public void onClosed()
        {
          Log.i(TAG, "onClosed: ");
          WritableMap params = Arguments.createMap();
          params.putString( "message", "" );
          sendReactNativeEvent( "onInterstitialDidClose", params );

        }

        @Override
        public void onOpened()
        {
          Log.i(TAG, "onOpened: ");
          WritableMap params = Arguments.createMap();
          params.putString( "message", "" );
          sendReactNativeEvent( "onInterstitialDidOpen", params );

        }

        @Override
        public void onReady()
        {
          Log.i(TAG, "onReady: ");
          mKidozInterstitial.show();
        }

        @Override
        public void onLoadFailed()
        {
          Log.i(TAG, "onLoadFailed: ");
          WritableMap params = Arguments.createMap();
          params.putString( "message", "" );
          sendReactNativeEvent( "onInterstitialDidFailToLoad", params );

        }

        @Override
        public void onNoOffers()
        {
          Log.i(TAG, "onNoOffers: ");

        }
      });

    }

    @ReactMethod()
    public void loadRewardVideo()
    {

      mKidozRewarded = new KidozInterstitial(sCurrentActivity, KidozInterstitial.AD_TYPE.REWARDED_VIDEO);
      mKidozRewarded.loadAd();

      mKidozRewarded.setOnInterstitialEventListener(new BaseInterstitial.IOnInterstitialEventListener()
      {
        @Override
        public void onClosed()
        {
          Log.i(TAG, "onClosed: ");
          WritableMap params = Arguments.createMap();
          params.putString( "message", "" );
          sendReactNativeEvent( "onRewardedVideoDidClose", params );

        }

        @Override
        public void onOpened()
        {
          Log.i(TAG, "onOpened: ");
          WritableMap params = Arguments.createMap();
          params.putString( "message", "" );
          sendReactNativeEvent( "onRewardedVideoDidOpen", params );

        }

        @Override
        public void onReady()
        {
          Log.i(TAG, "onReady: ");
          mKidozRewarded.show();
        }

        @Override
        public void onLoadFailed()
        {
          Log.i(TAG, "onLoadFailed: ");
          WritableMap params = Arguments.createMap();
          params.putString( "message", "" );
          sendReactNativeEvent( "onRewardedDidReciveError", params );

        }

        @Override
        public void onNoOffers()
        {
          Log.i(TAG, "onNoOffers: ");

        }
      });


      mKidozRewarded.setOnInterstitialRewardedEventListener(new BaseInterstitial.IOnInterstitialRewardedEventListener()
      {
        @Override
        public void onRewardReceived()
        {
          Log.i(TAG, "onRewardReceived: ");
          WritableMap params = Arguments.createMap();
          params.putString( "message", "" );
          sendReactNativeEvent( "onDidReceiveRewardForPlacement", params );

        }

        @Override
        public void onRewardedStarted()
        {
          Log.i(TAG, "onRewardedStarted: ");

        }
      });

    }

    @ReactMethod()
    public void loadBottomBanner()
    {
      loadBannerView();

    }

    @ReactMethod()
    public void unLoadBottomBanner()
    {
        /*if (bottomBannerView != null && sCurrentActivity != null){
          sCurrentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              bottomBannerView.setVisibility(View.INVISIBLE);
              bottomBannerView.removeAllViews();
              bottomBannerView = null;

            }
          });
          WritableMap params = Arguments.createMap();
          params.putString( "adUnitId", "unload banner view" );

          sendReactNativeEvent( "OnbannerViewDidLeaveApplication", params );

        }*/
      destroyAndDetachBanner();



    }

    private static  int toPixelUnits(int dipUnit) {
      float density = sCurrentActivity.getResources().getDisplayMetrics().density;
      return Math.round(dipUnit * density);
    }

    private void loadBannerView(){
      sCurrentActivity.runOnUiThread(new Runnable(){

        @Override
        public void run(){
          mKidozBannerView = KidozSDK.getKidozBanner(sCurrentActivity);
          mKidozBannerView.setBannerPosition(BANNER_POSITION.BOTTOM_CENTER);
          mKidozBannerView.load();

          mKidozBannerView.setKidozBannerListener(new KidozBannerListener()
          {
            @Override
            public void onBannerViewAdded()
            {
              Log.d(TAG, "onBannerViewAdded()");
            }

            @Override
            public void onBannerReady()
            {
              Log.d(TAG, "onBannerReady()");

              mKidozBannerView.show();
              WritableMap params = Arguments.createMap();
              params.putString( "message", "" );
              sendReactNativeEvent( "onBannerDidLoad", params );
            }

            @Override
            public void onBannerError(String errorMsg)
            {
              Log.d(TAG, "onBannerError(" + errorMsg + ")");
              WritableMap params = Arguments.createMap();
              params.putString( "message", errorMsg );
              sendReactNativeEvent( "onBannerDidReciveError", params );

            }

            @Override
            public void onBannerClose()
            {
              Log.d(TAG, "onBannerClose()");
              WritableMap params = Arguments.createMap();
              params.putString( "message", "" );
              sendReactNativeEvent( "onBannerDidClose", params );

            }

            @Override
            public void onBannerNoOffers() {
              Log.d(TAG, "onBannerNoOffers()");
            }
          });
          sCurrentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
          //sCurrentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏：根据传感器横向切换

          bottomBannerView = new RelativeLayout(sCurrentActivity.getApplicationContext());

          LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
          sCurrentActivity.addContentView(bottomBannerView,lp);

          //RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
          //bannerLayoutParams.setMargins(5, 5, 5, 5);

          int width = toPixelUnits(BANNER_WIDTH);
          int height = toPixelUnits(BANNER_HEIGHT);
          RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(width, height);
          bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
          bannerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

          //bottomBannerView.addView( applovin_adView, new android.widget.FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER ) );
          bottomBannerView.addView( mKidozBannerView,bannerLayoutParams);


        }
      });
    }

    private void performInitialization(final String pid, final String token, final Context context, final Callback callback)
    {
      // Guard against running init logic multiple times
      if ( isPluginInitialized ) return;

      isPluginInitialized = true;


      // If SDK key passed in is empty
      if ( TextUtils.isEmpty( pid ) || TextUtils.isEmpty(token))
      {
        throw new IllegalStateException( "Unable to initialize kidoz Ads SDK - no SDK key provided!" );
      }

      // Initialize SDK
      mInitCallback = callback;
      KidozSDK.setSDKListener(new SDKEventListener()
      {
        @Override
        public void onInitSuccess()
        {
          isSdkInitialized = true;
          mInitCallback.invoke("success");
        }

        @Override
        public void onInitError(String error)
        {
          Log.e(TAG,  "Failed to load SDK ");
        }
      });

      // This are Kidoz's sample credentials - replace with your own publisher's credential before publishing the app
      KidozSDK.initialize(context, pid, token);


    }


    /**
     * Destroys IronSource Banner and removes it from the container
     *
     */
    private void destroyAndDetachBanner() {

      if (bottomBannerView != null && sCurrentActivity != null){
        sCurrentActivity.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            bottomBannerView.setVisibility(View.INVISIBLE);
            bottomBannerView.removeAllViews();
            bottomBannerView = null;

          }
        });
      }
    }


    // React Native Bridge
    private void sendReactNativeEvent(final String name, @Nullable final WritableMap params)
    {
      getReactApplicationContext()
        .getJSModule( RCTDeviceEventEmitter.class )
        .emit( name, params );
    }
}
