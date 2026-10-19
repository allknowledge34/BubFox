package com.example.bubfox.game.counter.target;

import android.widget.TextView;

import com.example.bubfox.R;
import com.example.bubfox.game.MyGameEvent;
import com.example.bubfox.game_engine.Game;
import com.example.bubfox.game_engine.entity.UIGameObject;
import com.example.bubfox.game_engine.event.GameEvent;
import com.example.bubfox.level.MyLevel;



public abstract class TargetCounter extends UIGameObject {

    protected final TextView mText;
    protected final int mTarget;

    protected int mPoints;
    protected boolean mTargetHaveReached = false;
    private boolean mMoveHaveChanged = false;

    protected TargetCounter(Game game) {
        super(game);
        mText = (TextView) game.getGameActivity().findViewById(R.id.txt_target);
        mTarget = ((MyLevel) game.getLevel()).mTarget;
    }

    protected abstract boolean isTargetReach();

    @Override
    public void onStart() {
        drawUI();
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        if (mTargetHaveReached) {
            // We notify the GameController and remove
            gameEvent(MyGameEvent.GAME_WIN);
            removeFromGame();
        }
        if (mMoveHaveChanged) {
            if (((MyLevel) mGame.getLevel()).mMove == 0 && !isTargetReach()) {
                // We notify the GameController
                gameEvent(MyGameEvent.GAME_OVER);
            }
            mMoveHaveChanged = false;
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvents) {
        if ((MyGameEvent) gameEvents == MyGameEvent.BUBBLE_CONSUMED) {
            mMoveHaveChanged = true;
        }
    }

}
