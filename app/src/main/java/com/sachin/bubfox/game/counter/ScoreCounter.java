package com.sachin.bubfox.game.counter;

import android.widget.TextView;

import com.sachin.bubfox.R;
import com.sachin.bubfox.game.MyGameEvent;
import com.sachin.bubfox.game_engine.Game;
import com.sachin.bubfox.game_engine.entity.UIGameObject;
import com.sachin.bubfox.game_engine.event.GameEvent;
import com.sachin.bubfox.level.MyLevel;

public class ScoreCounter extends UIGameObject {

    private static final int POINTS_GAINED_PER_BUBBLE = 10;

    private final MyLevel mLevel;
    private final TextView mText;
    private int mPoints;

    public ScoreCounter(Game game) {
        super(game);
        mLevel = (MyLevel) game.getLevel();
        mText = (TextView) game.getGameActivity().findViewById(R.id.txt_score);
    }

    @Override
    public void onStart() {
        mPoints = 0;
        drawUI();
    }

    @Override
    public void onUpdate(long elapsedMillis) {
    }

    @Override
    protected void onDrawUI() {
        mText.setText(String.valueOf(mPoints));
    }

    @Override
    public void onGameEvent(GameEvent gameEvents) {
        if (gameEvents == MyGameEvent.BUBBLE_POP) {
            mPoints += POINTS_GAINED_PER_BUBBLE;
            mLevel.mScore = mPoints;
            drawUI();
        }
    }

}
