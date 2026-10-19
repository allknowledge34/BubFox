package com.example.bubfox.sound;

import com.example.bubfox.game_engine.sound.SoundEvent;

public enum MySoundEvent implements SoundEvent {
    // Bubble sound
    BUBBLE_POP,
    BUBBLE_HIT,
    BUBBLE_SHOOT,
    BUBBLE_SWITCH,
    COLLECT_ITEM,
    WOOD_EXPLODE,

    // Booster sound
    ADD_BOOSTER,
    FIRE_BUBBLE_SHOOT,
    BOMB_EXPLODE,

    // Game lifecycle sound
    START_GAME,
    COMBO,
    PLAYER_WIN,
    PLAYER_LOSS,
    GAME_COMPLETE,
    CALCULATE_SCORE,
    ADD_STAR,

    // UI sound
    SWEEP_IN,
    SWEEP_OUT,
    BUTTON_CLICK,
    WHEEL_SPIN
}
