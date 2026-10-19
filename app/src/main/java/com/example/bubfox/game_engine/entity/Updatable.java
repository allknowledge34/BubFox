package com.example.bubfox.game_engine.entity;


public interface Updatable {

    void update(long elapsedMillis);

    void addToGame();

    void removeFromGame();

    boolean isActive();

}
