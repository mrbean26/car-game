package com.bean.classicmini.utilities;

import com.bean.classicmini.MainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class ClassicMiniAdverts {
    private static boolean initialized = false;
    public static void begin(){
        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MobileAds.initialize(MainActivity.getAppContext(), new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                        initialized = true;
                    }
                });
            }
        });
    }

    // rewarded
    private static RewardedVideoAd rewardedVideoAd;
    private static void _loadRewardedAd(){
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(MainActivity.getAppContext());
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                rewardedVideoAd.show();
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
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    private static void _showRewardedAd(){
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
    private static void _loadInterstitialAd(){
        interstitialAd = new InterstitialAd(MainActivity.getAppContext());
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        interstitialAd.setAdListener(new AdListener(){
            // event callbacks
        });

        interstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private static void _showInterstitialAd(){
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
}
