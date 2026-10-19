package com.sachin.bubfox.game;

import android.widget.ImageView;

import com.sachin.bubfox.MainActivity;
import com.sachin.bubfox.R;
import com.sachin.bubfox.game.animal.Animal;
import com.sachin.bubfox.game.counter.MoveCounter;
import com.sachin.bubfox.game.counter.ProgressBarCounter;
import com.sachin.bubfox.game.counter.ScoreCounter;
import com.sachin.bubfox.game.counter.combo.ComboCounter;
import com.sachin.bubfox.game.counter.target.CollectTargetCounter;
import com.sachin.bubfox.game.counter.target.PopTargetCounter;
import com.sachin.bubfox.game.input.InputController;
import com.sachin.bubfox.game_engine.Game;
import com.sachin.bubfox.game_engine.GameView;
import com.sachin.bubfox.game_engine.ui.GameActivity;
import com.sachin.bubfox.game.effect.EffectManager;
import com.sachin.bubfox.level.LevelType;
import com.sachin.bubfox.level.MyLevel;
import com.sachin.bubfox.ui.dialog.PauseDialog;

public class MyGame extends Game {

    private final EffectManager mEffectManager;

    public MyGame(GameActivity activity, GameView gameView, int level) {
        super(activity, gameView);
        setPixelFactor(3300);
        setTouchController(new InputController(this));
        setSoundManager(getGameActivity().getSoundManager());
        setLevel(getGameActivity().getLevelManager().getLevel(level));
        // setDebugMode(true);

        // Init all the object and add to game
        mEffectManager = new EffectManager(this);
        new GameController(this).addToGame();
        new MoveCounter(this).addToGame();
        new ComboCounter(this).addToGame();
        new ScoreCounter(this).addToGame();
        new ProgressBarCounter(this).addToGame();
        setTargetCounter(((MyLevel) getLevel()).mLevelType);
        new Animal(this).addToGame();
    }

    public EffectManager getEffectManager() {
        return mEffectManager;
    }

    private void setTargetCounter(LevelType levelType) {
        // Init target image
        ImageView imageTarget = (ImageView) getGameActivity().findViewById(R.id.image_target);
        imageTarget.setImageResource(levelType.getTargetDrawableId());
        switch (levelType) {
            case POP_BUBBLE:
                new PopTargetCounter(this).addToGame();
                break;
            case COLLECT_ITEM:
                new CollectTargetCounter(this).addToGame();
                break;
        }
    }

    @Override
    protected void onStart() {
        // Start bgm
        getGameActivity().getSoundManager().loadMusic(R.raw.village);
    }

    @Override
    protected void onPause() {
        showPauseDialog();
    }

    @Override
    protected void onStop() {
        // Stop the game bgm
        getGameActivity().getSoundManager().unloadMusic();
        // Start the menu bgm
        getGameActivity().getSoundManager().loadMusic(R.raw.happy_and_joyful_children);
    }

    private void showPauseDialog() {
        PauseDialog dialog = new PauseDialog(getGameActivity(), getLevel().mLevel) {
            @Override
            public void resumeGame() {
                resume();
            }

            @Override
            public void quitGame() {
                // Reduce one live
                ((MainActivity) getGameActivity()).getLivesTimer().reduceLive();
                // Navigate back to map
                getGameActivity().navigateBack();
                // The GameEngine will stop automatically when fragment destroy
            }
        };
        getGameActivity().showDialog(dialog);
    }

}
