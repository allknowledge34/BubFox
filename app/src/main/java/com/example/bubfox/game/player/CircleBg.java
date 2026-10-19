package com.example.bubfox.game.player;


import com.example.bubfox.R;
import com.example.bubfox.game.Layer;
import com.example.bubfox.game.MyGameEvent;
import com.example.bubfox.game_engine.Game;
import com.example.bubfox.game_engine.entity.sprite.Sprite;
import com.example.bubfox.game_engine.event.GameEvent;

public class CircleBg extends Sprite {

    private final float mRotationSpeed;

    public CircleBg(Game game) {
        super(game, R.drawable.player_circle);
        mRotationSpeed = -50 / 1000f;   // We want to rotate at 50 degree per second
        mLayer = Layer.BACKGROUND_LAYER;
    }

    @Override
    public void onStart() {
        mX = mGame.getScreenWidth() / 2f - mWidth / 2f;
        mY = mGame.getScreenHeight() * 4 / 5f - mHeight / 2f;
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        mRotation += mRotationSpeed * elapsedMillis;
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == MyGameEvent.SHOW_WIN_DIALOG) {
            removeFromGame();
        }
    }

}
