package com.example.bubfox.game_engine.entity.particles.initializer;

import com.example.bubfox.game_engine.entity.particles.Particle;

import java.util.Random;

public class SpeedAngleInitializer implements ParticleInitializer {

    private final float mMinSpeed;
    private final float mMaxSpeed;
    private final int mMinAngle;
    private final int mMaxAngle;

    public SpeedAngleInitializer(float minSpeed, float maxSpeed, int minAngle, int maxAngle) {
        mMinSpeed = minSpeed;
        mMaxSpeed = maxSpeed;
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    @Override
    public void initParticle(Particle particle, Random random) {
        float speed = (random.nextFloat() * (mMaxSpeed - mMinSpeed) + mMinSpeed) / 1000f;
        int angle = mMinAngle;
        if (mMaxAngle > mMinAngle) {
            angle = random.nextInt(mMaxAngle - mMinAngle + 1) + mMinAngle;
        } else if (mMaxAngle < mMinAngle) {
            angle = random.nextInt(mMinAngle - mMaxAngle + 1) + mMaxAngle;
        }
        double angleInRads = Math.toRadians(angle);
        particle.mSpeedX = (float) (speed * Math.cos(angleInRads));
        particle.mSpeedY = (float) (speed * Math.sin(angleInRads));
    }
}
