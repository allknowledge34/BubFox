package com.sachin.bubfox.game.counter;

import android.widget.TextView;

import com.sachin.bubfox.R;
import com.sachin.bubfox.game.MyGameEvent;
import com.sachin.bubfox.game_engine.Game;
import com.sachin.bubfox.game_engine.entity.UIGameObject;
import com.sachin.bubfox.game_engine.event.GameEvent;
import com.sachin.bubfox.level.MyLevel;

public class MoveCounter extends UIGameObject {

    private static final int EXTRA_MOVES = 5;

    private final MyLevel mLevel;
    private final TextView mText;

    private int mMoves;

    public MoveCounter(Game game) {
        super(game);
        mLevel = (MyLevel) game.getLevel();
        mText = (TextView) game.getGameActivity().findViewById(R.id.txt_move);
    }

    @Override
    public void onStart() {
        mMoves = mLevel.mMove;
        drawUI();
    }

    @Override
    public void onUpdate(long elapsedMillis) {
    }

    @Override
    protected void onDrawUI() {
        mText.setText(String.valueOf(mMoves));
    }

    @Override
    public void onGameEvent(GameEvent gameEvents) {
        switch ((MyGameEvent) gameEvents) {
            case BUBBLE_SHOT:
                mMoves--;
                mLevel.mMove = mMoves;
                drawUI();
                break;
            case ADD_EXTRA_MOVE:
                mMoves += EXTRA_MOVES;
                mLevel.mMove = mMoves;
                drawUI();
                break;
        }
    }

}
