package com.example.bubfox.game_engine.event;

public interface GameEventListener {

    void onGameEvent(com.example.bubfox.game_engine.event.GameEvent event);

    void gameEvent(GameEvent event);

}
