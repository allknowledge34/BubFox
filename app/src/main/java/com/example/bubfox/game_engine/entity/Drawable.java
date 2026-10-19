package com.example.bubfox.game_engine.entity;

import android.graphics.Canvas;

public interface Drawable {

    void draw(Canvas canvas);

    int getLayer();

    boolean isVisible();

}
