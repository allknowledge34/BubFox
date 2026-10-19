package com.sachin.bubfox.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sachin.bubfox.R;
import com.sachin.bubfox.ui.UIEffect;

import java.util.ArrayList;

public class MapLevelAdapter extends RecyclerView.Adapter<MapLevelAdapter.SegmentViewHolder> {

    private final Context context;
    private final int totalLevels;
    private final int currentLevel;
    private final ArrayList<Integer> levelStars;
    private final OnLevelClickListener listener;

    private static final int LEVELS_PER_SEGMENT = 10;
    private static final int[] MAP_BACKGROUNDS = {
            R.drawable.map_01,
            R.drawable.map_02,
            R.drawable.map_03,
            R.drawable.map_04
    };

    private static final float[][] MAP_BIASES = {
            // map_01 path
            {0.60f, 0.58f, 0.50f, 0.36f, 0.40f, 0.55f, 0.56f, 0.40f, 0.35f, 0.50f},
            // map_02 path
            {0.58f, 0.48f, 0.35f, 0.58f, 0.60f, 0.40f, 0.50f, 0.55f, 0.44f, 0.35f},
            // map_03 path
            {0.50f, 0.60f, 0.52f, 0.38f, 0.38f, 0.45f, 0.60f, 0.68f, 0.50f, 0.30f},
            // map_04 path
            {0.55f, 0.72f, 0.60f, 0.35f, 0.35f, 0.45f, 0.55f, 0.58f, 0.50f, 0.48f}
    };

    public interface OnLevelClickListener {
        void onLevelClick(int level);
    }

    public MapLevelAdapter(Context context, int totalLevels, int currentLevel, ArrayList<Integer> levelStars, OnLevelClickListener listener) {
        this.context = context;
        this.totalLevels = totalLevels;
        this.currentLevel = currentLevel;
        this.levelStars = levelStars;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SegmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_map_segment, parent, false);
        return new SegmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SegmentViewHolder holder, int position) {
        // position 0 is at the bottom (levels 1-10) due to reverseLayout
        int bgIndex = position % MAP_BACKGROUNDS.length;
        holder.imageBackground.setImageResource(MAP_BACKGROUNDS[bgIndex]);
        float[] biases = MAP_BIASES[bgIndex];

        int startLevel = (position * LEVELS_PER_SEGMENT) + 1;

        // Ensure the views have been measured to calculate layout mathematically
        holder.container.post(() -> {
            int width = holder.container.getWidth();
            int height = holder.container.getHeight();
            if (width == 0 || height == 0) return;

            for (int i = 0; i < LEVELS_PER_SEGMENT; i++) {
                int level = startLevel + i;
                View buttonView = holder.buttonViews[i];

                if (level > totalLevels) {
                    buttonView.setVisibility(View.GONE);
                    continue;
                }
                buttonView.setVisibility(View.VISIBLE);

                TextView txtLevel = buttonView.findViewById(R.id.btn_level);
                ImageView imageStar = buttonView.findViewById(R.id.image_level_star);

                txtLevel.setText(String.valueOf(level));

                // State logic
                if (level <= currentLevel) {
                    txtLevel.setBackgroundResource(R.drawable.btn_level);
                    txtLevel.setTextColor(context.getResources().getColor(R.color.brown));
                    UIEffect.createButtonEffect(txtLevel);
                    txtLevel.setOnClickListener(v -> listener.onLevelClick(level));

                    if (level < currentLevel && (level - 1) < levelStars.size()) {
                        int star = levelStars.get(level - 1);
                        if (star == 1) imageStar.setImageResource(R.drawable.star_set_01);
                        else if (star == 2) imageStar.setImageResource(R.drawable.star_set_02);
                        else if (star == 3) imageStar.setImageResource(R.drawable.star_set_03);
                        imageStar.setVisibility(View.VISIBLE);
                    } else {
                        imageStar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    txtLevel.setBackgroundResource(R.drawable.btn_level_lock);
                    txtLevel.setTextColor(context.getResources().getColor(R.color.white));
                    txtLevel.setOnClickListener(null);
                    imageStar.setVisibility(View.INVISIBLE);
                }

                // Mathematical placement: winding sine wave path from bottom to top
                // Local Y progresses from height down to 0
                float progressY = 1.0f - ((float) i / (float) LEVELS_PER_SEGMENT);
                // Shift down slightly to stay within bounds
                float yPos = (progressY * height) - (height * 0.05f); 

                // X calculates exact position using the hardcoded horizontal biases for this map background
                float xOffsetPercent = biases[i];
                float xPos = (width * xOffsetPercent) - (buttonView.getWidth() / 2.0f);

                buttonView.setTranslationX(xPos);
                buttonView.setTranslationY(yPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) totalLevels / LEVELS_PER_SEGMENT);
    }

    public class SegmentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBackground;
        FrameLayout container;
        View[] buttonViews;

        public SegmentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBackground = itemView.findViewById(R.id.image_background);
            container = itemView.findViewById(R.id.layout_buttons_container);
            buttonViews = new View[LEVELS_PER_SEGMENT];

            // Pre-inflate the 10 buttons per segment
            for (int i = 0; i < LEVELS_PER_SEGMENT; i++) {
                View btnView = LayoutInflater.from(context).inflate(R.layout.item_map_button, container, false);
                container.addView(btnView);
                buttonViews[i] = btnView;
            }
        }
    }
}
