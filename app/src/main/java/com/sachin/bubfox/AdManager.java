package com.sachin.bubfox;

import android.app.Activity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdManager {

    private final Activity mActivity;
    private RewardedAd mRewardedAd;
    private AdRewardListener mListener;
    private boolean mRewardEarned = false;
    private boolean mIsLoading = false;

    public AdManager(Activity activity) {
        mActivity = activity;
    }

    public static void configureAudiencePolicy(boolean tagForChildDirectedTreatment) {
        RequestConfiguration.Builder builder = MobileAds.getRequestConfiguration().toBuilder();
        if (tagForChildDirectedTreatment) {
            builder.setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                   .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G);
        }
        MobileAds.setRequestConfiguration(builder.build());
    }

    public boolean isAdLoaded() {
        return mRewardedAd != null;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void requestAd() {
        requestAd(null);
    }

    public void requestAd(final AdLoadCallback callback) {
        if (mRewardedAd != null) {
            if (callback != null) {
                callback.onAdLoaded();
            }
            return;
        }

        if (mIsLoading) {
            return;
        }

        mIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(mActivity, mActivity.getString(R.string.txt_admob_reward),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        mIsLoading = false;
                        mRewardedAd = null;
                        if (callback != null) {
                            callback.onAdFailedToLoad();
                        }
                    }

                    @Override
                    public void onAdLoaded(RewardedAd rewardedAd) {
                        mIsLoading = false;
                        mRewardedAd = rewardedAd;
                        if (callback != null) {
                            callback.onAdLoaded();
                        }
                    }
                });
    }

    public boolean showRewardAd() {
        if (mRewardedAd == null) {
            requestAd();
            return false;
        }

        mRewardEarned = false;

        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                mRewardedAd = null;
                if (mListener != null) {
                    mListener.onLossReward();
                }
                requestAd();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                mRewardedAd = null;
                if (!mRewardEarned && mListener != null) {
                    mListener.onLossReward();
                }
                requestAd();
            }
        });

        mRewardedAd.show(mActivity, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(RewardItem rewardItem) {
                if (!mRewardEarned) {
                    mRewardEarned = true;
                    if (mListener != null) {
                        mListener.onEarnReward();
                    }
                }
            }
        });

        return true;
    }

    public void setListener(AdRewardListener listener) {
        mListener = listener;
    }

    public interface AdRewardListener {
        void onEarnReward();

        void onLossReward();
    }

    public interface AdLoadCallback {
        void onAdLoaded();

        void onAdFailedToLoad();
    }

}
