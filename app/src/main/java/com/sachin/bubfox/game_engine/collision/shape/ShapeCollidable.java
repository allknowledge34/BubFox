package com.sachin.bubfox.game_engine.collision.shape;

import android.graphics.Bitmap;
import android.graphics.Rect;


public interface ShapeCollidable {

    Bitmap getCollisionBitmap();

    Rect getCollisionBounds();

    void setCollisionBoundsPosition(int x, int y);

}
