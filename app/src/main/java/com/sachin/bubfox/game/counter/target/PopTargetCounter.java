package com.sachin.bubfox.game.counter.target;

import com.sachin.bubfox.game.MyGameEvent;
import com.sachin.bubfox.game_engine.Game;
import com.sachin.bubfox.game_engine.event.GameEvent;
import com.sachin.bubfox.level.MyLevel;



public class PopTargetCounter extends TargetCounter {

    public PopTargetCounter(Game game) {
        super(game);
    }

    @Override
    protected boolean isTargetReach() {
        return mPoints == 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Compute exact number of bubbles on the grid initially, because 
        // the hardcoded XML target is sometimes inaccurate and causes the game 
        // to not end even when all bubbles are cleared.
        String bubbles = ((MyLevel) mGame.getLevel()).mBubble;
        int actualBubbleCount = 0;
        if (bubbles != null) {
            bubbles = bubbles.replaceAll("\\s+", "");
            for (int i = 0; i < bubbles.length(); i++) {
                char c = bubbles.charAt(i);
                if (c == 'b' || c == 'r' || c == 'y' || c == 'g' ||
                    c == 'B' || c == 'R' || c == 'Y' || c == 'G') {
                    actualBubbleCount++;
                }
            }
        }
        mPoints = actualBubbleCount;
    }

    @Override
    protected void onDrawUI() {
        mText.setText(String.valueOf(mPoints));
    }

    @Override
    public void onGameEvent(GameEvent gameEvents) {
        super.onGameEvent(gameEvents);
        switch ((MyGameEvent) gameEvents) {
            case BUBBLE_POP:
                mPoints--;
                drawUI();

                if (isTargetReach()) {
                    mTargetHaveReached = true;
                }
                break;
            case BUBBLE_HIT:
                mPoints++;
                drawUI();
                break;
        }
    }

}
