package com.sachin.bubfox.game_engine.event;

public interface GameEventListener {

    void onGameEvent(com.sachin.bubfox.game_engine.event.GameEvent event);

    void gameEvent(GameEvent event);

}
