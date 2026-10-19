package com.sachin.bubfox;

import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.sachin.bubfox.database.DatabaseHelper;
import com.sachin.bubfox.game_engine.ui.GameActivity;
import com.sachin.bubfox.level.MyLevelManager;
import com.sachin.bubfox.sound.MySoundManager;
import com.sachin.bubfox.timer.LivesTimer;
import com.sachin.bubfox.ui.fragment.MenuFragment;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends GameActivity {

    private DatabaseHelper mDatabaseHelper;
    private AdManager mAdManager;
    private LivesTimer mLivesTimer;
    private final AtomicBoolean mIsMobileAdsInitialized = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BubFox);
        setContentView(R.layout.activity_main);
        setContainerId(R.id.container);
        setLevelManager(new MyLevelManager(this));
        setSoundManager(new MySoundManager(this));
        mDatabaseHelper = new DatabaseHelper(this);
        mAdManager = new AdManager(this);
        mLivesTimer = new LivesTimer(this);

        // Request GDPR / UMP Consent and initialize AdMob
        requestConsentAndInitAds();

        // Show the menu fragment
        if (savedInstanceState == null) {
            navigateToFragment(new MenuFragment());
            // Start the menu bgm
            getSoundManager().loadMusic(R.raw.happy_and_joyful_children);
        }
    }

    private void requestConsentAndInitAds() {
        ConsentRequestParameters params = new ConsentRequestParameters.Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                () -> UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                        MainActivity.this,
                        formError -> {
                            if (consentInformation.canRequestAds()) {
                                initializeMobileAds();
                            }
                        }
                ),
                requestConsentError -> {
                    // Offline or request error: initialize if consent was previously gathered or permitted
                    if (consentInformation.canRequestAds()) {
                        initializeMobileAds();
                    }
                }
        );

        if (consentInformation.canRequestAds()) {
            initializeMobileAds();
        }
    }

    private void initializeMobileAds() {
        if (mIsMobileAdsInitialized.getAndSet(true)) {
            return;
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                if (mAdManager != null) {
                    mAdManager.requestAd();
                }
            }
        });
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    public AdManager getAdManager() {
        return mAdManager;
    }

    public LivesTimer getLivesTimer() {
        return mLivesTimer;
    }

}