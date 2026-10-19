package com.example.bubfox.game_engine.engine;


public class UpdateLoop extends Loop {

    public UpdateLoop(GameEngine gameEngine) {
        super(gameEngine);
    }

    private static final int DELTA_TIME = 16;   // This is 60 fps

    private long mLastTime = System.currentTimeMillis();

    @Override
    public void onLoopUpdate(long elapsedMillis) {
        long currentTime = System.currentTimeMillis();
        long realElapsed = currentTime - mLastTime;
        
        if (realElapsed < DELTA_TIME) {
            try {
                Thread.sleep(DELTA_TIME - realElapsed);
            } catch (InterruptedException e) {
            }
        }
        
        currentTime = System.currentTimeMillis();
        realElapsed = currentTime - mLastTime;
        mLastTime = currentTime;
        
        mGameEngine.update(realElapsed);
    }

}
