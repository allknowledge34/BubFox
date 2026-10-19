package com.sachin.bubfox.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sachin.bubfox.R;
import com.sachin.bubfox.game.MyGame;
import com.sachin.bubfox.game_engine.Game;
import com.sachin.bubfox.game_engine.GameView;
import com.sachin.bubfox.game_engine.ui.GameFragment;
import com.sachin.bubfox.sound.MySoundEvent;
import com.sachin.bubfox.ui.UIEffect;

public class MyGameFragment extends GameFragment implements View.OnClickListener {

    private static final String LEVEL = "level";

    private Game mGame;
    private int mLevel;

    public MyGameFragment() {
        // Required empty public constructor
    }

    public static MyGameFragment newInstance(int level) {
        MyGameFragment fragment = new MyGameFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL, level);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLevel = getArguments().getInt(LEVEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    protected void onLayoutCreated() {
        // Init pause button
        ImageButton btnPause = (ImageButton) getGameActivity().findViewById(R.id.btn_pause);
        UIEffect.createButtonEffect(btnPause);
        btnPause.setOnClickListener(this);

        // Start game
        mGame = new MyGame(getGameActivity(), (GameView) getView().findViewById(R.id.game_view), mLevel);
        mGame.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGame != null) {
            mGame.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGame != null) {
            mGame.stop();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mGame != null) {
            mGame.pause();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
        if (mGame != null) {
            mGame.pause();
        }
    }

}
