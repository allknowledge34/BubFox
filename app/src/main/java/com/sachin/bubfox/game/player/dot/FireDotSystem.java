package com.sachin.bubfox.game.player.dot;


import com.sachin.bubfox.R;
import com.sachin.bubfox.game.player.booster.FireBubble;
import com.sachin.bubfox.game_engine.Game;

public class FireDotSystem extends DotSystem {

    public FireDotSystem(FireBubble fireBubble, Game game) {
        super(fireBubble, game);
        setDotBitmap(R.drawable.dot_fire);
    }

    @Override
    protected void setDotPosition(Dot dot, float x, float y) {
        if (x <= dot.mMinX || x >= dot.mMaxX) {
            // We do not need reflection in fire dot
            dot.setPosition(-dot.mWidth, -dot.mHeight);
        } else {
            dot.setPosition(x, y);
        }
    }

}
