package com.xm.smartloading.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by xumin on 2017/4/20.
 */

public class LoadingImageView extends ImageView {
    private LoadingDrawable mDrawable;
    public LoadingImageView(Context context) {
        super(context);
    }

    public LoadingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDrawable = new LoadingDrawable(getContext(), this);
        setImageDrawable(mDrawable);
        mDrawable.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getVisibility() == VISIBLE) {
            if (mDrawable != null)
                mDrawable.start();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView,int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (getVisibility() == GONE || getVisibility() == INVISIBLE) {
            if (mDrawable != null)
                mDrawable.stop();
        } else if (getVisibility() == VISIBLE) {
            if (mDrawable != null)
                mDrawable.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (getVisibility() == VISIBLE) {
            if (mDrawable != null)
                mDrawable.stop();
        }
        super.onDetachedFromWindow();
    }
}
