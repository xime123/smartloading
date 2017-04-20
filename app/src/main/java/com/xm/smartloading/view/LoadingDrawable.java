package com.xm.smartloading.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

import com.xm.smartloading.R;

/**
 * Created by xumin on 2017/4/20.
 */

public class LoadingDrawable extends Drawable {
    private static final int TRANSLATE_MAX_COUNT=6;
    private static final int ANIMATION_DRUATION=500;
    private static final Interpolator UP_INTERPOLATOR=new AccelerateDecelerateInterpolator();
    private static final Interpolator DOWN_INTERPOLATOR=new AccelerateInterpolator();
    private int mAnimationCount=0;
    /***
     * 当前动画执行到达的百分比
     */
    private float percent=0;

    private Context mContext;
    private View mParent;
    private Paint mPaint=new Paint();
    private Bitmap loading1;
    private Bitmap loading2;
    private Bitmap loading3;
    private Bitmap shadow;

    private int height=120;
    private int width=100;
    private float animArea=75;

    private Animation mAnimation;
    private Matrix mMatrix;
    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;

    public LoadingDrawable (Context context,View parent){
        this.mContext=context;
        this.mParent=parent;

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, dm);
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, dm);
        animArea = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, animArea, dm);

        loading1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.loading_1);
        loading2 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.loading_2);
        loading3 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.loading_3);
        shadow = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.loading_shadow);

        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        mPaint.setAntiAlias(true);
        mMatrix = new Matrix();
        setupAnimations();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.setDrawFilter(mPaintFlagsDrawFilter);
        drawShadow(canvas);
        canvas.drawBitmap(getCurrentBitmap(), getLoadingX(), getLoadingY(), mPaint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }
    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private Bitmap getCurrentBitmap() {
        switch (mAnimationCount / 2) {
            case 0:
                return loading1;
            case 1:
                return loading2;
            case 2:
                return loading3;
            default:
                return loading1;
        }
    }
    private void drawShadow(Canvas canvas){
        Matrix matrix=mMatrix;
        matrix.reset();
        matrix.postTranslate(getShadowX(),getShadowY());
        //float sx, float sy, float px, float py
        matrix.postScale(1 - (0.6f - 0.6f * getLoadingYPercent()),
                1,
                getShadowX() + shadow.getWidth() / 2,
                getShadowY() + shadow.getHeight() / 2);
        canvas.drawBitmap(shadow, matrix, mPaint);
    }

    private int getLoadingX() {
        return (getMinimumWidth() - getCurrentBitmap().getWidth()) / 2;
    }

    private int getLoadingY() {
        return (int) (getLoadingYPercent() * animArea);
    }
    private int getShadowX(){
       return  (getMinimumWidth()-shadow.getWidth())/2;
    }

    private int getShadowY(){
        return getIntrinsicHeight()-shadow.getHeight();
    }

    private float getLoadingYPercent() {
        return (getMoveOrientation() == 0 ? (1 - percent) : percent);
    }

    private void postDraw(float interpolatedTime){
        percent=interpolatedTime;
        if(percent==1.0f){
            percent=0;
            mAnimationCount++;
            if(mAnimationCount>=TRANSLATE_MAX_COUNT){
                mAnimationCount=0;
            }
            mAnimation.setInterpolator(getMoveOrientation()==0?UP_INTERPOLATOR:DOWN_INTERPOLATOR);
        }
        invalidateSelf();
    }

    //0:上 1：下
    private int getMoveOrientation(){
        return  mAnimationCount%2;
    }
    private void setupAnimations() {
        mAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                postDraw(interpolatedTime);
            }
        };
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(UP_INTERPOLATOR);
        mAnimation.setDuration(ANIMATION_DRUATION);
    }


    public void start() {
        reset();
        mParent.startAnimation(mAnimation);
    }

    public void stop() {
        mParent.clearAnimation();
    }
    private void reset() {
        mAnimation.reset();
        percent = 0;
        mAnimationCount = 0;
    }
}
