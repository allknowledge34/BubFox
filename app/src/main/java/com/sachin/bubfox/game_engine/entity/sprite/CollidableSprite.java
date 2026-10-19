package com.sachin.bubfox.game_engine.entity.sprite;

import android.graphics.Canvas;

import com.sachin.bubfox.game_engine.Game;
import com.sachin.bubfox.game_engine.collision.Collidable;
import com.sachin.bubfox.game_engine.collision.CollisionType;
import com.sachin.bubfox.game_engine.collision.shape.ShapeCollidable;


public abstract class CollidableSprite extends Sprite implements Collidable {

    private CollisionType mCollisionType = CollisionType.ACTIVE;
    private ShapeCollidable mCollisionShape;

    protected CollidableSprite(Game game, int drawableId) {
        super(game, drawableId);
    }

    @Override
    public CollisionType getCollisionType() {
        return mCollisionType;
    }

    @Override
    public void setCollisionType(CollisionType type) {
        mCollisionType = type;
    }

    @Override
    public ShapeCollidable getCollisionShape() {
        return mCollisionShape;
    }

    @Override
    public void setCollisionShape(ShapeCollidable shapeCollidable) {
        mCollisionShape = shapeCollidable;
    }

    @Override
    public void onCollision(Collidable otherObject) {
    }

    @Override
    public void onPostUpdate(long elapsedMillis) {
        // We align the collision shape to center of sprite
        mCollisionShape.setCollisionBoundsPosition((int) mX + mWidth / 2, (int) mY + mHeight / 2);
    }

    @Override
    public void draw(Canvas canvas) {
        if (Game.getDebugMode()) {
            if (getCollisionType() != CollisionType.NONE) {
                // Draw collision shape on screen
                canvas.drawBitmap(mCollisionShape.getCollisionBitmap(), null,
                        mCollisionShape.getCollisionBounds(), null);
            }
        }
        super.draw(canvas);
    }

}
