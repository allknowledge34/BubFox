package com.sachin.bubfox.game_engine.collision;

import com.sachin.bubfox.game_engine.collision.shape.ShapeCollidable;


public interface Collidable {

    CollisionType getCollisionType();

    void setCollisionType(CollisionType collisionType);

    ShapeCollidable getCollisionShape();

    void setCollisionShape(ShapeCollidable shapeCollidable);

    void onCollision(Collidable otherObject);

}
