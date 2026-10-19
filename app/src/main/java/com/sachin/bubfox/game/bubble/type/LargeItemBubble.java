package com.sachin.bubfox.game.bubble.type;

import com.sachin.bubfox.R;
import com.sachin.bubfox.game.MyGameEvent;
import com.sachin.bubfox.game.bubble.Bubble;
import com.sachin.bubfox.game.bubble.BubbleColor;
import com.sachin.bubfox.game.effect.BubblePopEffect;
import com.sachin.bubfox.game.effect.ItemEffect;
import com.sachin.bubfox.game_engine.Game;
import com.sachin.bubfox.game_engine.collision.shape.CircleCollisionShape;
import com.sachin.bubfox.game_engine.entity.sprite.CollidableSprite;
import com.sachin.bubfox.sound.MySoundEvent;

import java.util.ArrayList;
import java.util.List;

public class LargeItemBubble extends CompositeBubble {

    private static final int ITEMS_PER_BUBBLE = 5;

    private final BubblePopEffect mBubblePopEffect;
    private final List<ItemEffect> mItemEffectsPool = new ArrayList<>(ITEMS_PER_BUBBLE);

    private long mTotalTime;
    private boolean mIsItem = true;
    private boolean mCollected = false;

    public LargeItemBubble(Game game) {
        super(game, BubbleColor.LARGE_ITEM);
        setTargetSize(-1, -1); // Revert the 300x300 restriction from Bubble constructor
        mBubblePopEffect = new BubblePopEffect(game);
        // We add item to the pool now
        for (int i = 0; i < ITEMS_PER_BUBBLE; i++) {
            mItemEffectsPool.add(new ItemEffect(game, R.drawable.nut));
        }
        mLayer++;   // We want the item float on top of bubbles
    }

    @Override
    public void onStart() {
        super.onStart();
        mX -= mWidth / 3f;
        mY -= mHeight / 3f;
        mBubblePopEffect.mScale = 3;
        // Init the DummyBubble
        for (Bubble b : mEdges) {
            if (b instanceof DummyBubble) {
                addDummyBubble((DummyBubble) b);
            }
        }
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        super.onUpdate(elapsedMillis);
        if (mCollected) {
            mTotalTime += elapsedMillis;
            if (mTotalTime >= 150) {
                if (!mItemEffectsPool.isEmpty()) {
                    activateItemEffect();
                } else {
                    mCollected = false;
                }
                mTotalTime = 0;
            }
        }
    }

    @Override
    public void popBubble() {
        if (mIsItem) {
            // We pop the item if it hasn't been collected
            popLargeItem();
        } else {
            // Otherwise, we pop the bubble
            super.popBubble();
        }
    }

    @Override
    public void popFloater() {
        if (mIsItem) {
            // We pop the item if it hasn't been collected
            popLargeItem();
        } else {
            // Otherwise, we pop the floater
            super.popFloater();
        }
    }

    private void popLargeItem() {
        mBubblePopEffect.activate(mX + mWidth / 2f, mY + mHeight / 2f);
        activateItemEffect();
        setBubbleColor(BubbleColor.BLANK);
        setCollisionShape(new CircleCollisionShape(mWidth, mHeight));   // Update collision box
        clearDummyBubble();
        // We notify the target counter now, so it will sync
        for (int i = 0; i < ITEMS_PER_BUBBLE; i++) {
            gameEvent(MyGameEvent.COLLECT_ITEM);   // We have 5 items
        }
        mGame.getSoundManager().playSound(MySoundEvent.COLLECT_ITEM);
        mX += mWidth;
        mY += mHeight;
        mLayer--;
        mCollected = true;
        mIsItem = false;
    }

    private void activateItemEffect() {
        mItemEffectsPool.remove(0)
                .activate(mX + mWidth / 2f, mY + mHeight / 2f);
    }

    @Override
    public Bubble getCollidedBubble(CollidableSprite collidableSprite) {
        if (mIsItem) {
            // Check player collide bubble at bottom, side or middle
            if (collidableSprite.mY > mY + mHeight * 3 / 4f) {
                // Check player collide bubble at right or left bottom
                if (collidableSprite.mX > mX + mWidth * 2 / 3f) {
                    return mEdges[5].mEdges[5];
                } else if (collidableSprite.mX > mX + mWidth / 3f) {
                    return mEdges[5].mEdges[4];
                } else {
                    return mEdges[4].mEdges[4];
                }
                // [==|=====|==]
                //  0.33   0.66
            } else if (collidableSprite.mY > mY + mHeight / 2f) {
                // Check player collide bubble at right or left middle
                if (collidableSprite.mX > mX + mWidth / 2f) {
                    return mEdges[3].mEdges[5];
                } else {
                    return mEdges[2].mEdges[4];
                }
            } else {
                // Check player collide bubble at right or left side
                if (collidableSprite.mX > mX + mWidth / 2f) {
                    return mEdges[3].mEdges[3];
                } else {
                    return mEdges[2].mEdges[2];
                }
            }
        } else {
            return super.getCollidedBubble(collidableSprite);
        }
    }

}
