package com.bean.classicmini.utilities;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bean.classicmini.MainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class ClassicMiniAdverts {
    public static String REWARDED_AD_ID = "ca-app-pub-3940256099942544/5224354917";
    public static String INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712";
    public static String BANNER_AD_ID = "ca-app-pub-3940256099942544/6300978111";

    // begins
    private static boolean initialized = false;
    private static void _begin(){
        MobileAds.initialize(MainActivity.getAppContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                initialized = true;
            }
        });

        _beginBannerAd();
        _beginInterstitialAd();
        _beginRewardedAd();
    }
    
    public static void begin(){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _begin();
            }
        });
    }

    // rewarded
    private static RewardedVideoAd rewardedVideoAd;
    private static void _beginRewardedAd(){
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(MainActivity.getAppContext());
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
    }

    private static void _loadRewardedAd(){
        if(rewardedVideoAd == null){
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        rewardedVideoAd.loadAd(REWARDED_AD_ID, adRequest);
    }

    private static void _showRewardedAd(){
        if(rewardedVideoAd == null){
            return;
        }

        if(rewardedVideoAd.isLoaded()){
            rewardedVideoAd.show();
        }
    }

    public static void loadRewardedAd(){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _loadRewardedAd();
            }
        });
    }

    public static void showRewardedAd(){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _showRewardedAd();
            }
        });
    }

    // interstitial
    private static InterstitialAd interstitialAd;
    private static void _beginInterstitialAd(){
        interstitialAd = new InterstitialAd(MainActivity.getAppContext());
        interstitialAd.setAdUnitId(INTERSTITIAL_AD_ID);
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdFailedToLoad(int i) {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdImpression() {

            }
        });
    }

    private static void _loadInterstitialAd(){
        if(interstitialAd == null){
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    private static void _showInterstitialAd(){
        if(interstitialAd == null){
            return;
        }

        if(interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }

    public static void loadInterstitialAd(){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _loadInterstitialAd();
            }
        });
    }

    public static void showInterstitialAd(){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _showInterstitialAd();
            }
        });
    }
    
    // banner
    private static AdView bannerAd;
    private static RelativeLayout.LayoutParams adParams;
    private static RelativeLayout mainRelativeLayout;
    
    private static void _beginBannerAd(){
        bannerAd = new AdView(MainActivity.getAppContext());
        bannerAd.setAdSize(AdSize.BANNER);
        bannerAd.setBackgroundColor(Color.TRANSPARENT);

        mainRelativeLayout = new RelativeLayout(MainActivity.getAppContext());
        mainRelativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }
    
    public static AdView getBannerAd(){ // used to add over surfaceView
        return bannerAd;
    }

    public static RelativeLayout.LayoutParams getAdParams(){ // used to add over surfaceView
        return adParams;
    }
    
    public static RelativeLayout getMainRelativeLayout(){ // used to add over surfaceView
        return mainRelativeLayout;
    }

    private static void _loadBannerAd(){
        bannerAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(int i) {

            }

            @Override
            public void onAdImpression() {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdOpened() {

            }
        });

        bannerAd.setAdUnitId(BANNER_AD_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAd.loadAd(adRequest);
    }

    public static void loadBannerAd(){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _loadBannerAd();
            }
        });
    }
}
