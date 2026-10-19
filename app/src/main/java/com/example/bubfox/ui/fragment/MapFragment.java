package com.example.bubfox.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.example.bubfox.MainActivity;
import com.example.bubfox.R;
import com.example.bubfox.database.DatabaseHelper;
import com.example.bubfox.game_engine.ui.GameFragment;
import com.example.bubfox.item.Item;
import com.example.bubfox.sound.MySoundEvent;
import com.example.bubfox.timer.LivesTimer;
import com.example.bubfox.timer.WheelTimer;
import com.example.bubfox.ui.TransitionEffect;
import com.example.bubfox.ui.UIEffect;
import com.example.bubfox.ui.dialog.AdLivesDialog;
import com.example.bubfox.ui.dialog.LevelDialog;
import com.example.bubfox.ui.dialog.SettingDialog;
import com.example.bubfox.ui.dialog.ShopDialog;
import com.example.bubfox.ui.dialog.WheelDialog;

import java.util.ArrayList;

public class MapFragment extends GameFragment implements View.OnClickListener,
        TransitionEffect.OnTransitionListener, MapLevelAdapter.OnLevelClickListener {

    private static final int TOTAL_LEVEL = 1000;
    private DatabaseHelper mDatabaseHelper;
    private LivesTimer mLivesTimer;
    private WheelTimer mWheelTimer;
    private TransitionEffect mTransitionEffect;
    private ArrayList<Integer> mLevelStar;
    private int mCurrentLevel;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabaseHelper = ((MainActivity) getGameActivity()).getDatabaseHelper();
        mLivesTimer = ((MainActivity) getGameActivity()).getLivesTimer();
        mWheelTimer = new WheelTimer(getGameActivity());
        mTransitionEffect = new TransitionEffect(getGameActivity());
        mTransitionEffect.setListener(this);
        // Load level data from db and init
        mLevelStar = mDatabaseHelper.getAllLevelStar();
        mCurrentLevel = mLevelStar.size() + 1;
        init();
    }

    @Override
    protected void onLayoutCreated() {
        if (mCurrentLevel > TOTAL_LEVEL) {
            return;
        }
        // Scroll to current level position using RecyclerView
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_map);
        if (recyclerView != null) {
            int position = (mCurrentLevel - 1) / 10;
            recyclerView.scrollToPosition(position);
        }
    }

    private void init() {
        // Init button
        ImageButton btnSetting = (ImageButton) getView().findViewById(R.id.btn_setting);
        ImageButton btnShop = (ImageButton) getView().findViewById(R.id.btn_shop);
        ImageButton btnCoin = (ImageButton) getView().findViewById(R.id.btn_coin);
        ImageButton btnWheel = (ImageButton) getView().findViewById(R.id.btn_wheel);
        btnSetting.setOnClickListener(this);
        btnShop.setOnClickListener(this);
        btnCoin.setOnClickListener(this);
        btnWheel.setOnClickListener(this);
        UIEffect.createButtonEffect(btnSetting);
        UIEffect.createButtonEffect(btnShop);
        UIEffect.createButtonEffect(btnCoin);
        UIEffect.createButtonEffect(btnWheel);
        // Init lives text
        TextView txtLives = (TextView) getView().findViewById(R.id.txt_lives);
        txtLives.setOnClickListener(this);
        UIEffect.createButtonEffect(txtLives);
        // Init layout
        initRecyclerView();
        initAd();
        loadCoin();

        // We only show one dialog at a time
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mWheelTimer.isWheelReady()) {
                    // Show the wheel dialog
                    showWheelDialogAndShowLevel();
                } else {
                    // Show the current level dialog
                    showLevelDialog(mCurrentLevel);
                }
            }
        }, 1200);
    }

    private View findViewByName(String name) {
        int id = getResources().getIdentifier(name, "id", getGameActivity().getPackageName());
        return getView().findViewById(id);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_map);
        
        // Reverse layout makes index 0 at the bottom, so level 1 is at the bottom
        LinearLayoutManager layoutManager = new LinearLayoutManager(getGameActivity(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        
        MapLevelAdapter adapter = new MapLevelAdapter(getGameActivity(), TOTAL_LEVEL, mCurrentLevel, mLevelStar, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLevelClick(int level) {
        showLevelDialog(level);
        getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
    }

    private void initAd() {
        AdView adView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void loadCoin() {
        TextView textCoin = (TextView) getView().findViewById(R.id.txt_coin);
        int coin = mDatabaseHelper.getItemNum(Item.COIN);
        textCoin.setText(String.valueOf(coin));
    }

    private void showLevelDialog(int level) {
        if (level > TOTAL_LEVEL) {
            return;
        }
        LevelDialog levelDialog = new LevelDialog(getGameActivity(), level) {
            @Override
            public void navigateToGame() {
                // Check player lives
                if (mLivesTimer.isEnoughLives()) {
                    super.navigateToGame();
                } else {
                    showLiveNotEnoughDialog();
                }
            }

            @Override
            public void startGame() {
                getGameActivity().navigateToFragment(MyGameFragment.newInstance(level));
                // Stop the bgm
                getGameActivity().getSoundManager().unloadMusic();
            }
        };
        showDialog(levelDialog);
    }

    private void showLiveNotEnoughDialog() {
        AdLivesDialog adLivesDialog = new AdLivesDialog(getGameActivity());
        showDialog(adLivesDialog);
    }

    private void showWheelDialog() {
        WheelDialog wheelDialog = new WheelDialog(getGameActivity(), mWheelTimer) {
            @Override
            public void updateCoin() {
                loadCoin();
            }
        };
        showDialog(wheelDialog);
    }

    private void showWheelDialogAndShowLevel() {
        WheelDialog wheelDialog = new WheelDialog(getGameActivity(), mWheelTimer) {
            @Override
            public void showLevel() {
                showLevelDialog(mCurrentLevel);
            }

            @Override
            public void updateCoin() {
                loadCoin();
            }
        };
        showDialog(wheelDialog);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_setting) {
            getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            SettingDialog settingDialog = new SettingDialog(getGameActivity());
            showDialog(settingDialog);
        } else if (id == R.id.btn_shop || id == R.id.btn_coin) {
            getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            ShopDialog shopDialog = new ShopDialog(getGameActivity()) {
                @Override
                public void updateMapCoin() {
                    loadCoin();
                }
            };
            showDialog(shopDialog);
        } else if (id == R.id.btn_wheel) {
            getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            showWheelDialog();
        } else if (id == R.id.txt_lives) {
            getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            if (!mLivesTimer.isLivesFull()) {
                showLiveNotEnoughDialog();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLivesTimer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLivesTimer.stop();
    }

    @Override
    public boolean onBackPressed() {
        mTransitionEffect.show();
        return true;
    }

    @Override
    public void onTransition() {
        getGameActivity().navigateToFragment(new MenuFragment());
    }

}
