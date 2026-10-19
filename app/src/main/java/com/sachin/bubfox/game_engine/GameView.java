package com.sachin.bubfox.game_engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;

import com.sachin.bubfox.game_engine.entity.Drawable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameView extends TextureView implements TextureView.SurfaceTextureListener {
    private List<Drawable> mDrawables;
    private boolean mIsSurfaceAvailable = false;
    private boolean mNeedsSorting = true;

    private final Comparator<Drawable> mLayerComparator = new Comparator<Drawable>() {
        @Override
        public int compare(Drawable d1, Drawable d2) {
            return Integer.compare(d1.getLayer(), d2.getLayer());
        }
    };

    public GameView(Context context) {
        super(context);
        init();
    }
    
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOpaque(false);
        setSurfaceTextureListener(this);
    }

    public void setDrawables(List<Drawable> drawables) {
        mDrawables = drawables;
    }

    public void setNeedsSorting() {
        mNeedsSorting = true;
    }

    public void draw() {
        if (!mIsSurfaceAvailable) {
            return;
        }
        Canvas canvas = lockCanvas();
        if (canvas != null) {
            try {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                synchronized (mDrawables) {
                    if (mNeedsSorting) {
                        Collections.sort(mDrawables, mLayerComparator);
                        mNeedsSorting = false;
                    }
                    int size = mDrawables.size();
                    for (int i = 0; i < size; i++) {
                        Drawable d = mDrawables.get(i);
                        if (d.isVisible()) {
                            d.draw(canvas);
                        }
                    }
                }
            } finally {
                unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mIsSurfaceAvailable = true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mIsSurfaceAvailable = false;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
}
