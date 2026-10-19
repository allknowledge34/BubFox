package com.sachin.bubfox.game.bubble.type;

import com.sachin.bubfox.R;
import com.sachin.bubfox.game.MyGameEvent;
import com.sachin.bubfox.game.bubble.Bubble;
import com.sachin.bubfox.game.bubble.BubbleColor;
import com.sachin.bubfox.game.effect.BubblePopEffect;
import com.sachin.bubfox.game.effect.ItemEffect;
import com.sachin.bubfox.game_engine.Game;
import com.sachin.bubfox.sound.MySoundEvent;

public class ItemBubble extends Bubble {

    private static final float ITEM_SCALE = 1.3f;

    private final ItemEffect mItemEffect;
    private final BubblePopEffect mBubblePopEffect;

    private boolean mIsItem = true;

    public ItemBubble(Game game) {
        super(game, BubbleColor.ITEM);
        mItemEffect = new ItemEffect(game, R.drawable.nut);
        mBubblePopEffect = new BubblePopEffect(game);
        mLayer++;   // We want the item float on top of bubbles
    }

    @Override
    public void onStart() {
        super.onStart();
        // Init scale
        mScale = ITEM_SCALE;
        mItemEffect.mScale = ITEM_SCALE;
        mBubblePopEffect.mScale = ITEM_SCALE;
    }

    @Override
    public void popBubble() {
        if (mIsItem) {
            // We pop the item if it hasn't been collected
            popItem();
        } else {
            // Otherwise, we pop the bubble
            super.popBubble();
        }
    }

    @Override
    public void popFloater() {
        if (mIsItem) {
            // We pop the item if it hasn't been collected
            popItem();
        } else {
            // Otherwise, we pop the floater
            super.popFloater();
        }
    }

    private void popItem() {
        mItemEffect.activate(mX + mWidth / 2f, mY + mHeight / 2f);
        mBubblePopEffect.activate(mX + mWidth / 2f, mY + mHeight / 2f);
        setBubbleColor(BubbleColor.BLANK);
        gameEvent(MyGameEvent.COLLECT_ITEM);
        mGame.getSoundManager().playSound(MySoundEvent.COLLECT_ITEM);
        mScale = 1;
        mIsItem = false;
    }

}
