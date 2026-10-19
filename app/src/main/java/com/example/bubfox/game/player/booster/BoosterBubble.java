package com.example.bubfox.game.player.booster;

import com.example.bubfox.game.MyGameEvent;
import com.example.bubfox.game.bubble.BubbleSystem;
import com.example.bubfox.game.player.PlayerBubble;
import com.example.bubfox.game_engine.Game;
import com.example.bubfox.sound.MySoundEvent;

public abstract class BoosterBubble extends PlayerBubble {

    protected boolean mConsume;

    protected BoosterBubble(BubbleSystem bubbleSystem, Game game, int drawableId) {
        super(bubbleSystem, game, drawableId);
    }

    @Override
    public void onStart() {
        super.onStart();
        mConsume = false;
        gameEvent(MyGameEvent.BOOSTER_ADDED);
        mGame.getSoundManager().playSound(MySoundEvent.ADD_BOOSTER);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (mConsume) {
            // Update player state and notify the booster manager
            gameEvent(MyGameEvent.BOOSTER_CONSUMED);
        } else {
            // Update player state
            gameEvent(MyGameEvent.BOOSTER_REMOVED);
        }
    }

    @Override
    protected void onBubbleShoot() {
        gameEvent(MyGameEvent.BOOSTER_SHOT);
        mGame.getSoundManager().playSound(MySoundEvent.BUBBLE_SHOOT);
    }

    @Override
    protected void onBubbleSwitch() {
        // We do not switch booster here
    }

    @Override
    protected void onBubbleReset() {
        // Pop floater and shift
        mBubbleSystem.popFloater();
        mBubbleSystem.shiftBubble();
        mConsume = true;
        removeFromGame();
    }

}
