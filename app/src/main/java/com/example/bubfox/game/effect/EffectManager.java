package com.example.bubfox.game.effect;

import com.example.bubfox.R;
import com.example.bubfox.game.Layer;
import com.example.bubfox.game_engine.Game;
import com.example.bubfox.game_engine.entity.particles.ParticleSystem;

public class EffectManager {

    private final Game mGame;
    private final ParticleSystem mExplosionParticleSystem;
    private final ParticleSystem mLightParticleSystem;

    public EffectManager(Game game) {
        mGame = game;
        
        mExplosionParticleSystem = new ParticleSystem(game, R.drawable.sparkle, 60)
                .setDurationPerParticle(800)
                .setSpeedX(-600, 600)
                .setSpeedY(-600, 600)
                .setAccelerationY(1)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0, 400)
                .setScale(1, 0, 400)
                .setLayer(Layer.EFFECT_LAYER);

        mLightParticleSystem = new ParticleSystem(game, R.drawable.circle_light, 15)
                .setDurationPerParticle(400)
                .setAlpha(255, 0, 200)
                .setScale(0, 2)
                .setLayer(Layer.EFFECT_LAYER);
    }

    public void playExplosion(float x, float y, int particles) {
        mExplosionParticleSystem.oneShot(x, y, particles);
    }

    public void playLight(float x, float y, int particles) {
        mLightParticleSystem.oneShot(x, y, particles);
    }

    public void playScoreEffect(float x, float y, int drawableId) {
        new ScoreEffect(mGame, drawableId).activate(x, y);
    }

    public void playFloaterEffect(float x, float y, int drawableId) {
        new FloaterEffect(mGame, drawableId).activate(x, y);
    }

}
