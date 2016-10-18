package com.example.jingbin.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.jingbin.customview.R;

/**
 * Created by jingbin on 2016/10/16.
 *
 *  Android 自定义View (二) 进阶:
 * 1、自定义View的属性
 * 2、在View的构造方法中获得我们自定义的属性
 * [ 3、重写onMesure ]
 * 4、重写onDraw
 */

public class CustomImageView extends View {


    // 图片
    private Bitmap mImage;
    // 图片缩放形式
    private int mImageScaleType;
    // 文字
    private String mTitleText;
    // 文字大小
    private int mTitleTextSize;
    // 文字颜色
    private int mTitleTextColor;
    // 用来画图片使用
    private Rect mRect;
    // 用来画文字使用
    private Rect mTextBound;
    // 画笔
    private Paint mPaint;

    // 宽
    private int mWidth;
    // 高
    private int mHeight;
    // 缩放形式
    private int IMAGE_SCALE_FITXY = 0;


    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 初始化所特有自定义类型
     * 2、在View的构造方法中获得我们自定义的属性
     */
    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int arr = a.getIndex(i);
            switch (arr) {
                case R.styleable.CustomImageView_image:// 得到设置的图片
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(arr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:// 图片显示形式
                    mImageScaleType = a.getInt(a.getIndex(arr), 0);
                    break;
                case R.styleable.CustomImageView_imageTitleText:// 文字
                    mTitleText = a.getString(arr);
                    break;
                case R.styleable.CustomImageView_imageTitleTextColor:// 文字颜色
                    mTitleTextColor = a.getColor(a.getIndex(arr), 0);
                    break;
                case R.styleable.CustomImageView_imageTitleTextSize:// 文字字号
                    mTitleTextSize = a.getDimensionPixelSize(arr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        // 注意这在for(){}外
        a.recycle();

        mRect = new Rect();
        mPaint = new Paint();
        mTextBound = new Rect();
        mPaint.setTextSize(mTitleTextSize);
        // 计算了描绘字体需要的范围
        if (!TextUtils.isEmpty(mTitleText)) {
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBound);
        }
    }

    /**
     * [ 3、重写onMesure ]
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else {
            // 图片宽
            int mImageWidth = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            // 文字宽
            int mTextWidth = getPaddingLeft() + getPaddingRight() + mTextBound.width();
            if (specMode == MeasureSpec.AT_MOST) {
                int des = Math.max(mImageWidth, mTextWidth);
                // 有可能的情况是占满全屏,为保险去最小的
                mWidth = Math.min(des, specSize);
            }
        }
        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else {
            int des = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mTextBound.height();
            // 有可能是全屏的,取最小的高度
            if (specMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(des, specSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 4、重写onDraw
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        /**
         * 边框
         */
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.top = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTitleTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        /**
         * 当前设置的宽度小于字体的需要的宽度,将字体改为xxx...
         */
        if (mTextBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitleText, paint, (float) mWidth - getPaddingLeft() - getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        } else {
            // 正常情况下将字体居中
//            canvas.drawText(mTitleText, mWidth / 2 - mTextBound.width() / 2, mHeight / 2 + mTextBound.height() / 2, mPaint);//不行?
//            canvas.drawText(mTitleText, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);  //不行?
            canvas.drawText(mTitleText, mWidth / 2 - mTextBound.width() / 2, mHeight - getPaddingBottom(), mPaint);
        }
        // 取消使用掉的块(图片)
        mRect.bottom -= mTextBound.height();
        if (mImageScaleType == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        } else { //居中
            // 计算居中的矩形范围
            mRect.left = mWidth / 2 - mImage.getWidth() / 2;
            mRect.right = mWidth / 2 + mImage.getWidth() / 2;
            mRect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight() / 2;
            mRect.bottom = (mHeight - mTextBound.height()) / 2 + mImage.getHeight() / 2;
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }

    }
}
