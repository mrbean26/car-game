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
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class ClassicMiniAdverts {
    // Test Ad ID's
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

    private static void _setRewardedAdListener(RewardedVideoAdListener used){
        rewardedVideoAd.setRewardedVideoAdListener(used);
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

    public static void setRewardedAdListener(final RewardedVideoAdListener used){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _setRewardedAdListener(used);
            }
        });
    }

    // interstitial
    private static InterstitialAd interstitialAd;
    private static void _beginInterstitialAd(){
        interstitialAd = new InterstitialAd(MainActivity.getAppContext());
        interstitialAd.setAdUnitId(INTERSTITIAL_AD_ID);
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

    private static void _setInterstitialAdListener(AdListener used){
        interstitialAd.setAdListener(used);
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

    public static void setInterstitialAdListener(final AdListener used){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _setInterstitialAdListener(used);
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
        bannerAd.setAdUnitId(BANNER_AD_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAd.loadAd(adRequest);
    }

    private static void _setBannerAdListener(AdListener used){
        bannerAd.setAdListener(used);
    }

    public static void loadBannerAd(){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _loadBannerAd();
            }
        });
    }

    public static void setBannerAdListener(final AdListener used){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _setBannerAdListener(used);
            }
        });
    }
}
