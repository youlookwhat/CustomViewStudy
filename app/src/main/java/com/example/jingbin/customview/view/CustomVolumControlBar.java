package com.example.jingbin.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.jingbin.customview.R;

/**
 * Created by jingbin on 2016/10/18.
 *
 * Android 自定义View (四) 视频音量调控:
 * 先分许需要的属性，两个小块的颜色、一张中间的图片、间隙大小、一共多少个块块。
 * <p>
 * 1、自定义View的属性
 * 2、在View的构造方法中获得我们自定义的属性
 * [ 3、重写onMesure ](这里不需要)
 * 4、重写onDraw
 */

public class CustomVolumControlBar extends View {

    // 第一个圆的颜色
    private int mFirstColor;
    // 第二个圆的颜色
    private int mSecondColor;
    // 圆弧的宽度
    private int mCircleWidth;
    // 个数
    private int mDotCount;
    // 每个块块之间的间隙
    private int mSplitSize;
    // 中间的图片
    private Bitmap mBg;

    // 当前进度
    private int mCurrentCount = 1;

    // 画笔
    private Paint mPaint;

    private Rect mRect;

    public CustomVolumControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 必要的初始化，获得一些自定义的值
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomVolumControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int arr = typedArray.getIndex(i);
            switch (arr) {
                case R.styleable.CustomVolumControlBar_vFirstColor:
                    mFirstColor = typedArray.getColor(arr, Color.BLACK);
                    break;
                case R.styleable.CustomVolumControlBar_vSecondColor:
                    mSecondColor = typedArray.getColor(arr, Color.WHITE);
                    break;
                case R.styleable.CustomVolumControlBar_vCircleWidth:
                    mCircleWidth = typedArray.getDimensionPixelSize(arr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomVolumControlBar_vDotCount:
                    mDotCount = typedArray.getInt(arr, 20);
                    break;
                case R.styleable.CustomVolumControlBar_vSplitSize:
                    mSplitSize = typedArray.getInt(arr, 20);
                    break;
                case R.styleable.CustomVolumControlBar_vBg:
                    mBg = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(arr, 0));
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
        mRect = new Rect();

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//定义线段断电形状为圆头
        mPaint.setStyle(Paint.Style.STROKE);// 设置空心
        int center = getWidth() / 2;// 获取圆心x坐标
        int radius = center - mCircleWidth / 2;// 半径(***圆边在圆环的中间***)
        /**
         * 椭圆
         */
        drawOval(canvas, center, radius);

        /**
         * 计算内切正方形的位置
         */
        int relRadius = radius - mCircleWidth / 2;// 获得内圆的半径

        /**
         * 内切正方形距离左边的距离(或顶部):
         * (内圆半径 -  (更2 / 2) * 内圆半径) + 圆弧的宽度
         */
        mRect.left = (int) (relRadius - Math.sqrt(2) / 2 * relRadius) + mCircleWidth;
        mRect.top = (int) (relRadius - Math.sqrt(2) / 2 * relRadius) + mCircleWidth;
        /**
         * 内切正方形距离左边的距离 + 正方形的边长(Math.sqrt(2) * relRadius)
         */
        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);
        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);


        /**
         * 如果图片比较小,那么根据图片的尺寸放置到正中心
         */
        if (mBg.getWidth() < Math.sqrt(2) * relRadius) {
            mRect.left = mCircleWidth + (relRadius - mBg.getWidth() / 2);
            mRect.top = mCircleWidth + (relRadius - mBg.getWidth() / 2);
            mRect.right = mCircleWidth + (relRadius + mBg.getWidth() / 2);
            mRect.bottom = mCircleWidth + (relRadius + mBg.getWidth() / 2);
        }
        canvas.drawBitmap(mBg, null, mRect, mPaint);

    }

    /**
     * 根据参数画出每个小块
     */
    private void drawOval(Canvas canvas, int center, int radius) {
        /**
         * 根据需要的个数以及间隙计算每个块块所占的比例 * 360   ---> 每个块的大小
         * ( 360 - 块个数 * 间距 ) / 块的个数
         */
        float itemSize = (360 * 1.0f - mDotCount * mSplitSize) / mDotCount;
        // 用于定义的圆的形状和大小的界限
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);

        /**
         * 画圆环的第一种颜色
         */
        mPaint.setColor(mFirstColor);// 设置圆环的颜色
        for (int i = 0; i < mDotCount; i++) {
            // 根据进度画圆弧
            canvas.drawArc(oval, (i * (itemSize + mSplitSize)), itemSize, false, mPaint);
        }

        /**
         * 画圆环的第二种颜色  mCurrentCount
         */
        mPaint.setColor(mSecondColor);// 设置圆环的颜色
        for (int i = 0; i < mCurrentCount; i++) {
            // canvas.drawArc(rectF, -90, mProgress, false, mPaint);// 根据进度画圆弧
            // 根据进度画圆弧
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }
    }

    /**
     * 添加触摸监听
     * 当前数量+1
     */
    public void up() {
        if (mCurrentCount < mDotCount) {
            mCurrentCount++;
        }
        postInvalidate();
    }

    public void down() {
        if (mCurrentCount > 0) {
            mCurrentCount--;
        }
        postInvalidate();
    }

    private int xDown, xUp;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xDown < xUp) {
                    down();
                } else {
                    up();
                }
                break;
        }
        return true;
    }
}


