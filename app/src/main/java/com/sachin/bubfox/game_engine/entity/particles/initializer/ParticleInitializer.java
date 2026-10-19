package com.sachin.bubfox.game_engine.entity.particles.initializer;

import com.sachin.bubfox.game_engine.entity.particles.Particle;

import java.util.Random;



public interface ParticleInitializer {

    void initParticle(Particle particle, Random random);

}
