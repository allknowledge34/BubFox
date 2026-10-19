package com.sachin.bubfox.ui.dialog;

import android.widget.ImageView;

import com.sachin.bubfox.MainActivity;
import com.sachin.bubfox.R;
import com.sachin.bubfox.game_engine.ui.GameActivity;
import com.sachin.bubfox.game_engine.ui.GameDialog;
import com.sachin.bubfox.sound.MySoundEvent;
import com.sachin.bubfox.ui.TransitionEffect;
import com.sachin.bubfox.ui.UIEffect;

public class LossDialog extends GameDialog implements TransitionEffect.OnTransitionListener {

    private final TransitionEffect mTransitionEffect;

    public LossDialog(GameActivity activity) {
        super(activity);
        setContentView(R.layout.dialog_loss);
        setRootLayoutId(R.layout.dialog_game_container);
        setEnterAnimationId(R.anim.enter_from_top);
        setExitAnimationId(R.anim.exit_to_bottom);
        mTransitionEffect = new TransitionEffect(activity);
        mTransitionEffect.setListener(this);
        init();
    }

    private void init() {
        // Init pop effect
        ImageView imageFox = (ImageView) findViewById(R.id.image_fox);
        UIEffect.createPopUpEffect(imageFox);

        // Reduce one live
        ((MainActivity) mParent).getLivesTimer().reduceLive();

        // Dismiss the dialog after 1500ms
        imageFox.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 1500);
    }

    @Override
    protected void onShow() {
        mParent.getSoundManager().playSound(MySoundEvent.PLAYER_LOSS);
    }

    @Override
    protected void onDismiss() {
        mParent.getSoundManager().playSound(MySoundEvent.SWEEP_OUT);
    }

    @Override
    protected void onHide() {
        mTransitionEffect.show();
    }

    @Override
    public void onTransition() {
        // Stop the game and navigate back to map
        stopGame();
        mParent.navigateBack();
    }

    public void stopGame() {
        // Override this method to stop the game
    }

}
