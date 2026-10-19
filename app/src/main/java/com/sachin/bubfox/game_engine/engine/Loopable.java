package com.sachin.bubfox.game_engine.engine;


public interface Loopable {

    void startLoop();

    void stopLoop();

    void pauseLoop();

    void resumeLoop();

    boolean isRunning();

    boolean isPaused();

    void onLoopUpdate(long elapsedMillis);

}
