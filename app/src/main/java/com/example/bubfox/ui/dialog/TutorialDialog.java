package com.example.bubfox.ui.dialog;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bubfox.MainActivity;
import com.example.bubfox.R;
import com.example.bubfox.database.DatabaseHelper;
import com.example.bubfox.game_engine.ui.GameActivity;
import com.example.bubfox.game_engine.ui.GameDialog;
import com.example.bubfox.item.Item;
import com.example.bubfox.level.LevelTutorial;
import com.example.bubfox.level.MyLevel;
import com.example.bubfox.sound.MySoundEvent;
import com.example.bubfox.ui.UIEffect;

public class TutorialDialog extends GameDialog implements View.OnClickListener {

    private final LevelTutorial mTutorial;

    public TutorialDialog(GameActivity activity, MyLevel level) {
        super(activity);
        setContentView(R.layout.dialog_tutorial);
        setRootLayoutId(R.layout.dialog_game_container);
        setEnterAnimationId(R.anim.enter_from_center);
        setExitAnimationId(R.anim.exit_to_center);
        mTutorial = level.mLevelTutorial;
        init();
    }

    private void init() {
        // Init tutorial text
        TextView txtTutorial = (TextView) findViewById(R.id.txt_tutorial);
        txtTutorial.setText(mTutorial.getStringId());

        // Init tutorial image
        ImageView imageTutorial = (ImageView) findViewById(R.id.image_tutorial);
        imageTutorial.setImageResource(mTutorial.getDrawableId());

        // Init button
        ImageButton btnPlay = (ImageButton) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        UIEffect.createButtonEffect(btnPlay);

        // Init pop up
        UIEffect.createPopUpEffect(btnPlay);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_play) {
            mParent.getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            dismiss();
        }
    }

    @Override
    protected void onShow() {
        mParent.getSoundManager().playSound(MySoundEvent.SWEEP_IN);
    }

    @Override
    protected void onDismiss() {
        mParent.getSoundManager().playSound(MySoundEvent.SWEEP_OUT);
    }

    @Override
    protected void onHide() {
        switch (mTutorial) {
            case COLOR_BUBBLE:
                // Add one color bubble to db
                addBooster(Item.COLOR_BALL);
                updateBooster();
                // Add color bubble through button
                mParent.findViewById(R.id.btn_color_bubble).performClick();
                break;
            case FIRE_BUBBLE:
                // Add one fire bubble to db
                addBooster(Item.FIREBALL);
                updateBooster();
                // Add fire bubble through button
                mParent.findViewById(R.id.btn_fire_bubble).performClick();
                break;
            case BOMB_BUBBLE:
                // Add one bomb bubble to db
                addBooster(Item.BOMB);
                updateBooster();
                // Add bomb bubble through button
                mParent.findViewById(R.id.btn_bomb_bubble).performClick();
                break;
        }
    }

    private void addBooster(String name) {
        DatabaseHelper databaseHelper = ((MainActivity) mParent).getDatabaseHelper();
        int numBooster = databaseHelper.getItemNum(name);
        databaseHelper.updateItemNum(name, numBooster + 1);
    }

    public void updateBooster() {
        // Override this method to update booster ui
    }

}
