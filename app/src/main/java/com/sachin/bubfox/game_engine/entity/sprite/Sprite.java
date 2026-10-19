package com.sachin.bubfox.game_engine.entity.sprite;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.sachin.bubfox.game_engine.Game;
import com.sachin.bubfox.game_engine.entity.Drawable;
import com.sachin.bubfox.game_engine.entity.GameObject;


public abstract class Sprite extends GameObject implements Drawable {

    protected final float mPixelFactor;

    public Bitmap mBitmap;
    public int mWidth;
    public int mHeight;
    public float mX;
    public float mY;
    public float mTargetWidth = -1;
    public float mTargetHeight = -1;
    public int mLayer;
    public float mRotation;
    public float mScale = 1;
    public float mAlpha = 255;
    public boolean mIsVisible = true;

    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint = new Paint();

    protected Sprite(Game game, int drawableId) {
        super(game);
        mPixelFactor = game.getPixelFactor();
        setSpriteBitmap(drawableId);
    }

    protected Bitmap getBitmapFromId(int drawableId) {
        Resources r = mGame.getGameActivity().getResources();
        return ((BitmapDrawable) r.getDrawable(drawableId)).getBitmap();
    }

    public void setSpriteBitmap(int drawableId) {
        mBitmap = getBitmapFromId(drawableId);
        updateSize();
    }

    public void setTargetSize(float width, float height) {
        mTargetWidth = width;
        mTargetHeight = height;
        updateSize();
    }

    private void updateSize() {
        if (mTargetWidth > 0 && mTargetHeight > 0) {
            mWidth = (int) (mTargetWidth * mPixelFactor);
            mHeight = (int) (mTargetHeight * mPixelFactor);
        } else if (mBitmap != null) {
            mWidth = (int) (mBitmap.getWidth() * mPixelFactor);
            mHeight = (int) (mBitmap.getHeight() * mPixelFactor);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mX > canvas.getWidth()
                || mY > canvas.getHeight()
                || mX < -mWidth
                || mY < -mHeight) {
            return;
        }

        float scaleX = ((float) mWidth / mBitmap.getWidth()) * mScale;
        float scaleY = ((float) mHeight / mBitmap.getHeight()) * mScale;
        float translateFactor = (1 - mScale) / 2;
        mMatrix.reset();
        mMatrix.postScale(scaleX, scaleY);
        mMatrix.postTranslate(mX + translateFactor * mWidth, mY + translateFactor * mHeight);
        mMatrix.postRotate(mRotation, mX + mWidth / 2f, mY + mHeight / 2f);
        mPaint.setAlpha((int) mAlpha);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }

    @Override
    public void addToGame() {
        mIsVisible = true;
        super.addToGame();
    }

    @Override
    public void removeFromGame() {
        mIsVisible = false;
        super.removeFromGame();
    }

    @Override
    public int getLayer() {
        return mLayer;
    }

    @Override
    public boolean isVisible() {
        return mIsVisible;
    }

}
