package com.example.bubfox.game.bubble.type;

import com.example.bubfox.game.bubble.Bubble;
import com.example.bubfox.game.bubble.BubbleColor;
import com.example.bubfox.game_engine.Game;

import java.util.ArrayList;
import java.util.List;

public class CompositeBubble extends Bubble {

    private final List<DummyBubble> mDummyBubbles = new ArrayList<>();

    public CompositeBubble(Game game, BubbleColor bubbleColor) {
        super(game, bubbleColor);
    }

    protected void addDummyBubble(DummyBubble dummyBubble) {
        dummyBubble.mTargetBubble = this;
        mDummyBubbles.add(dummyBubble);
    }

    protected void clearDummyBubble() {
        for (DummyBubble d : mDummyBubbles) {
            d.setBubbleColor(BubbleColor.BLANK);
            d.mTargetBubble = null;
        }
    }

}
