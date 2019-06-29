package com.sunzm.isv.lib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.InputStream;

/**
 * Created by szm on 2018/7/4.
 */

public class ImageSwitcherView extends FrameLayout {

    private static final long ANIMATOR_DURATION = 200;

    private ValueAnimator showValueAnim;

    /**动画持续时间*/
    private long animatorDuration;
    /**当前将要显示的是childOne*/
    private boolean isChildOne;
    /**正在执行动画*/
    private boolean isShowAniming;

    private Bitmap mBitmap;

    private ImageView childOne, childTwo;

    public ImageSwitcherView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init(){
        animatorDuration = ANIMATOR_DURATION;
        isChildOne = true;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        childOne = new ImageView(getContext());
        childOne.setScaleType(ImageView.ScaleType.FIT_XY);
        childOne.setLayoutParams(params);

        childTwo = new ImageView(getContext());
        childTwo.setScaleType(ImageView.ScaleType.FIT_XY);
        childTwo.setLayoutParams(params);

        addView(childOne);
        addView(childTwo);

        childOne.setAlpha(0f);
        childTwo.setAlpha(0f);
        childOne.setVisibility(GONE);
        childTwo.setVisibility(GONE);

        showValueAnim = ValueAnimator.ofFloat(0f, 1f);
        showValueAnim.setDuration(animatorDuration);
        showValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float)valueAnimator.getAnimatedValue();
                if (isChildOne){
                    childOne.setAlpha(alpha);
                }else{
                    childTwo.setAlpha(alpha);
                }
            }
        });
        showValueAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                if (mOnAnimatorRunning != null){
                    mOnAnimatorRunning.onAnimatorEnd();
                }
                if (isChildOne){
                    isShowAniming = false;
                    childTwo.setVisibility(GONE);
                }else{
                    isShowAniming = false;
                    childOne.setVisibility(GONE);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {
                if (mOnAnimatorRunning != null){
                    mOnAnimatorRunning.onAnimatorEnd();
                }
                if (isChildOne){
                    isShowAniming = false;
                    childTwo.setVisibility(GONE);
                    childOne.setVisibility(VISIBLE);
                    childOne.setAlpha(1f);
                }else{
                    isShowAniming = false;
                    childOne.setVisibility(GONE);
                    childTwo.setVisibility(VISIBLE);
                    childTwo.setAlpha(1f);
                }
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        childOne.layout(0,0,getMeasuredWidth(),getMeasuredHeight());
        childTwo.layout(0,0,getMeasuredWidth(),getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setImageResource(int id){
        mBitmap = readBitmapFromResource(getResources(), id);
        setImageBitmap(mBitmap, true);
    }

    public void setImageResource(int id, boolean needAnim){
        mBitmap = readBitmapFromResource(getResources(), id);
        setImageBitmap(mBitmap, needAnim);
    }

    public void setImageBitmap(Bitmap bitmap, boolean needAnim){
        if (needAnim) {
            if (isShowAniming) {
                showValueAnim.cancel();
            }
            if (childOne.getVisibility() == VISIBLE && childTwo.getVisibility() == GONE) {
                removeAllViews();
                addView(childOne);
                addView(childTwo);
                childTwo.setAlpha(0f);
                childTwo.setImageBitmap(bitmap);
                childTwo.setVisibility(VISIBLE);
                isChildOne = false;
                isShowAniming = true;
                if (mOnAnimatorRunning != null) {
                    mOnAnimatorRunning.onAnimatorStart();
                }
                showValueAnim.start();
            } else if (childTwo.getVisibility() == VISIBLE && childOne.getVisibility() == GONE) {
                removeAllViews();
                addView(childTwo);
                addView(childOne);
                childOne.setAlpha(0f);
                childOne.setImageBitmap(bitmap);
                childOne.setVisibility(VISIBLE);
                isChildOne = true;
                isShowAniming = true;
                if (mOnAnimatorRunning != null) {
                    mOnAnimatorRunning.onAnimatorStart();
                }
                showValueAnim.start();
            } else {
                childOne.setImageBitmap(bitmap);
                childOne.setVisibility(VISIBLE);
                childOne.setAlpha(1f);
            }
        }else {
            if (childOne.getVisibility() == VISIBLE && childTwo.getVisibility() == GONE) {
                childOne.setImageBitmap(bitmap);
                childOne.setAlpha(1f);
            } else if (childTwo.getVisibility() == VISIBLE && childOne.getVisibility() == GONE) {
                childTwo.setImageBitmap(bitmap);
                childTwo.setAlpha(1f);
            }else {
                childOne.setImageBitmap(bitmap);
                childOne.setVisibility(VISIBLE);
                childOne.setAlpha(1f);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBitmap != null){
            mBitmap.recycle();
        }
    }

    public void setAnimatorDuration(long animatorDuration) {
        this.animatorDuration = animatorDuration;
    }

    public Bitmap readBitmapFromResource(Resources resources, int resourcesId) {
        InputStream ins = resources.openRawResource(resourcesId);
        return BitmapFactory.decodeStream(ins, null, null);
    }

    public boolean getIsShowAniming(){
        return isShowAniming;
    }

    public interface OnAnimatorRunning{
        public void onAnimatorStart();
        public void onAnimatorEnd();
    }

    private OnAnimatorRunning mOnAnimatorRunning;

    public void setOnAnimatorRunning(OnAnimatorRunning onAnimatorRunning){
        mOnAnimatorRunning = onAnimatorRunning;
    }
}
