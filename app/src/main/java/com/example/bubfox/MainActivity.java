package com.example.bubfox;

import android.os.Bundle;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.example.bubfox.database.DatabaseHelper;
import com.example.bubfox.level.MyLevelManager;
import com.example.bubfox.sound.MySoundManager;
import com.example.bubfox.timer.LivesTimer;
import com.example.bubfox.ui.fragment.MenuFragment;
import com.example.bubfox.game_engine.ui.GameActivity;

public class MainActivity extends GameActivity {

    private DatabaseHelper mDatabaseHelper;
    private AdManager mAdManager;
    private LivesTimer mLivesTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BubFox);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_main);
        setContainerId(R.id.container);
        setLevelManager(new MyLevelManager(this));
        setSoundManager(new MySoundManager(this));
        mDatabaseHelper = new DatabaseHelper(this);
        mAdManager = new AdManager(this);
        mLivesTimer = new LivesTimer(this);

        // Init the ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Show the menu fragment
        if (savedInstanceState == null) {
            navigateToFragment(new MenuFragment());
            // Start the menu bgm
            getSoundManager().loadMusic(R.raw.happy_and_joyful_children);
        }
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