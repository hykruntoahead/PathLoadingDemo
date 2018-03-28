package com.example.administrator.pathloadingdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/3/26.
 */

public class LoadingView extends View {
    private float mWidth;
    private Paint mOutCirclePaint, mInnerCirclePaint;
    ;
    private ValueAnimator mValueAnim;
    private RectF mOutRect, mInnerRect;
    private float mProgress;
    private boolean mGrowing = true;
    private int mRate = 200;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        mOutCirclePaint = new Paint();
        mOutCirclePaint.setAntiAlias(true);
        mOutCirclePaint.setColor(Color.parseColor("#4DAFFF"));
        mOutCirclePaint.setStrokeWidth(16);
        mOutCirclePaint.setStyle(Paint.Style.STROKE);
        mOutCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setAntiAlias(true);
        mInnerCirclePaint.setColor(Color.parseColor("#4dafff"));
        mInnerCirclePaint.setStyle(Paint.Style.STROKE);
        mInnerCirclePaint.setStrokeWidth(8);
        mInnerCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mValueAnim = ValueAnimator.ofFloat(0, 1, 0);
        mValueAnim.setDuration(3000);
        mValueAnim.setRepeatCount(ValueAnimator.INFINITE);
        //先加速后减速
//        mValueAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (float) valueAnimator.getAnimatedValue();
                if (valueAnimator.getAnimatedFraction() <= 0.5) {
                    mGrowing = true;
                } else {
                    mGrowing = false;
                }
                invalidate();
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int min = Math.min(w, h);

        mWidth = min < 64 ? 64 : min;

        initRectF();
    }

    private void initRectF() {

        mOutRect = new RectF(-mWidth / 2 + 8, -mWidth / 2 + 8, mWidth / 2 - 8, mWidth / 2 - 8);
        mInnerRect = new RectF(-mWidth / 2 + 24, -mWidth / 2 + 24, mWidth / 2 - 24, mWidth / 2 - 24);

        mValueAnim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mWidth / 2, mWidth / 2);
        canvas.rotate(-90);


        if (mGrowing) {
            canvas.drawArc(mOutRect, 360 * mProgress, mRate * mProgress, false, mOutCirclePaint);
            canvas.drawArc(mInnerRect, 180 + 360 * mProgress, mRate * mProgress, false, mInnerCirclePaint);
        } else {
            canvas.drawArc(mOutRect, 360 * (1 - mProgress),  mRate * mProgress, false, mOutCirclePaint);
            canvas.drawArc(mInnerRect, 180 + 360 * (1 - mProgress),  mRate * mProgress, false, mInnerCirclePaint);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnim != null) {
            if (mValueAnim.isRunning()) {
                mValueAnim.end();
            }
            mValueAnim.cancel();
            mValueAnim = null;
        }
    }
}
