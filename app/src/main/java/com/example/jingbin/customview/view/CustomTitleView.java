package com.example.jingbin.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.jingbin.customview.R;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by jingbin on 16/9/29.
 * <p> Android 自定义View (一)
 * <p>
 * -----onMeasure:: 111
 * -----onMeasure:: 111
 * -----onDraw:: 111
 * -----onMeasure:: 111
 * -----onMeasure:: 111
 * -----onDraw:: 111
 */

public class CustomTitleView extends View {

    // 文字
    private String mTitleText;
    // 文字颜色
    private int mTitleTextColor;
    // 字号
    private int mTitleTextSize;
    // 画笔
    private Paint mPaint;
    // 矩形
    private Rect mRect;

    public CustomTitleView(Context context) {
        this(context, null);
        Log.e("-----CustomTitleView:", "111");
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.e("-----CustomTitleView:", "222");
    }

    /**
     * 获得我自定义的样式属性
     */
    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("-----CustomTitleView:", "333");
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
        int n = typedArray.getIndexCount();

        for (int i = 0; i < n; i++) {
            int arr = typedArray.getIndex(i);//获得单个属性值
            switch (arr) {
                case R.styleable.CustomTitleView_titleText://文字
                    mTitleText = typedArray.getString(arr);
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    //默认文字为黑色
                    mTitleTextColor = typedArray.getColor(arr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    // 默认设置为16sp,TypedValue也可以把sp转化为px
                    mTitleTextSize = typedArray.getDimensionPixelSize(arr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mPaint.setColor(mTitleTextColor);
        mRect = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleText = randomText();
                postInvalidate();
            }
        });

    }

    private String randomText() {
        Random random = new Random();
        HashSet<Integer> integers = new HashSet<>();
        while (integers.size() < 4) {
            int randomInt = random.nextInt(10);
            integers.add(randomInt);
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (Integer i : integers) {
            stringBuffer.append("").append(i);
        }
        return stringBuffer.toString();
    }

    /**
     * 此时如果我们把布局文件的宽和高写成wrap_content，会发现效果并不是我们的预期：
     * 系统帮我们测量的高度和宽度都是MATCH_PARENT，当我们设置明确的宽度和高度时，系统帮我们测量的结果就是我们设置的结果，
     * 当我们设置为WRAP_CONTENT,或者MATCH_PARENT系统帮我们测量的结果就是MATCH_PARENT的长度。
     * 所以，当设置了WRAP_CONTENT时，我们需要自己进行测量，即重写onMeasure方法”：
     * <p>
     * 重写之前先了解MeasureSpec的specMode,一共三种类型：
     * EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
     * AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
     * UNSPECIFIED：表示子布局想要多大就多大，很少使用
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("-----onMeasure:", "111");

        //之前自己参照博客写的:
//        myOnMeasure(widthMeasureSpec, heightMeasureSpec);
        // 源码
        sourceOnMeasure(widthMeasureSpec, heightMeasureSpec);

//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 源码
     */
    private void sourceOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY://明确指定了
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
            case MeasureSpec.UNSPECIFIED:
//                int textWidth = mRect.width(); // 这样mRect.width()直接计算出来的会有误差
                float textWidth = mPaint.measureText(mTitleText);
                width = (int) (getPaddingLeft() + getPaddingRight() + textWidth);
                break;
        }
        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY://明确指定了
                height = getPaddingTop() + getPaddingBottom() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
            case MeasureSpec.UNSPECIFIED:
//                int textHeight = mRect.height(); //直接计算出来的会有误差
                Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
//                float textHeight = Math.abs((fontMetrics.descent - fontMetrics.ascent));
                float textHeight = Math.abs((fontMetrics.bottom - fontMetrics.top));
                height = (int) (getPaddingTop() + getPaddingBottom() + textHeight);
                break;
        }
        setMeasuredDimension(width, height);
    }


    /**
     * 之前自己参照博客写的:
     */
    private void myOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
//            float textWidth = mRect.width();
            float textWidth = mPaint.measureText(mTitleText);
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
//            float textHeight = mRect.height();
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float textHeight = Math.abs((fontMetrics.descent - fontMetrics.ascent));
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("-----onDraw:", "111");
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.LEFT);
        // 画布,左上右下
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint); // getMeasuredWidth()也可以。
        // 画笔
        mPaint.setColor(mTitleTextColor);
        // 画布,画Text  *****
        /**
         * 用的是其中一个drawText重载方法:canvas.drawText(String text,float x,float y,Paint paint);
         * x和y是绘制时的起点坐标, getWidth() / 2 - mRect.width() / 2 其实是为了居中绘制文本,
         * getWidth()是获取自定义View的宽度,
         * mRect.width()是获取文本的宽度,
         * 你可能是想问为什么不是直接getWidth() / 2吧?
         * 这样的话文本就是从水平方向中间位置向右绘制,绘制的文本当然就不是居中了,要减去mRect.width() / 2才是水平居中.垂直方向同理.
         *
         * "0 + getPaddingLeft()": 绘制文本的起点X
         *   "0": 直接从"0"开始就可以(文字会自带一点默认间距)
         *
         * */
        Log.e("---->", "getWidth():" + getWidth());
        Log.e("---->", "mRect.width():" + mRect.width());
        // 这样写会自动居中(但会有一点误差,以为文字会自带一点默认间距," - mRect.left"就好,csdn:[Zohar_zou]解决)
        canvas.drawText(mTitleText, getWidth() / 2 - mRect.width() / 2 - mRect.left, getHeight() / 2 + mRect.height() / 2, mPaint);
        // 不会居中
//         canvas.drawText(mTitleText, 0 + getPaddingLeft(), getHeight() / 2 + mRect.height() / 2, mPaint);
//        canvas.drawText(mTitleText, getPaddingLeft(), getHeight() / 2 + mRect.height() / 2, mPaint);
    }
}
