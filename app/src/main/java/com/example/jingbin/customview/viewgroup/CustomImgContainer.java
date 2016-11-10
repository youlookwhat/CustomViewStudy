package com.example.jingbin.customview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jingbin on 2016/10/19.
 * 手把手教您自定义ViewGroup（一）:
 * 1、决定该ViewGroup的LayoutParams
 * 2、onMeasure
 * 3、onLayout对其所有childView进行定位（设置childView的绘制区域）
 * <p>
 * E/----->: CustomImgContainer  2
 * E/----->: generateLayoutParams
 * E/----->: generateLayoutParams
 * E/----->: generateLayoutParams
 * E/----->: generateLayoutParams
 * E/----->: onMeasure
 * E/----->: onLayout
 * E/----->: onMeasure
 * E/----->: onLayout
 */

public class CustomImgContainer extends ViewGroup {

    public CustomImgContainer(Context context) {
        super(context);
        Log.e("----->", "CustomImgContainer  1");
    }

    public CustomImgContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("----->", "CustomImgContainer  2");
    }

    /**
     * 1、决定该ViewGroup的LayoutParams
     * 对于我们这个例子，我们只需要ViewGroup能够支持margin即可，那么我们直接使用系统的MarginLayoutParams
     * <p>
     * 重写父类的该方法，返回MarginLayoutParams的实例，这样就为我们的ViewGroup指定了其LayoutParams为MarginLayoutParams。
     * <p>
     * 遍历所有的childView，根据childView的宽和高以及margin，然后分别将0，1，2，3位置的childView依次设置到左上、右上、左下、右下的位置。
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        Log.e("----->", "generateLayoutParams");
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 2、onMeasure
     * 在onMeasure中计算childView的测量值以及模式，以及设置自己的宽和高：
     * <p>
     * 计算所有childview 的宽度和高度 然后根据childview的计算结果,设置自己的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("----->", "onMeasure");
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高,以及计算模式
         */
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childview的宽和高  *************
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        /**
         * 记录如果是warp_content是设置的宽和高
         */
        // viewgroup的宽高
        int width = 0;
        int height = 0;

        // childview的宽高
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;

        int childCount = getChildCount();

        //用于计算左边两个childview的高度和
        int lHeight = 0;
        //用于计算右边两个childview的高度和,最终取最大值
        int rHeight = 0;
        //用于计算上边两个childview的宽度和
        int tWidth = 0;
        //用于计算下面两个childview的宽度和,最终取最大值
        int bWidth = 0;

        /**
         * 根据childview计算出的宽和高,以及设置的margin计算容器的宽和高,主要用于容器是warp_content时
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
//            cWidth = childView.getWidth();// 为什么不是这个????
            cWidth = childView.getMeasuredWidth();// childview的宽
            cHeight = childView.getMeasuredHeight();// childview的高
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            // 上面两个childview
            if (i == 0 || i == 1) {// 上面的宽度
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 2 || i == 3) {// 下面的宽度
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 0 || i == 2) {//左边的高度
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
            if (i == 1 || i == 3) {//右边的高度
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
        }
        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);
        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        // 是精确的类型就直接取得到的值; 不是则用计算的值
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width, modeHight == MeasureSpec.EXACTLY ? sizeHeight : height);
    }

    /**
     * 3、onLayout对其所有childView进行定位（设置childView的绘制区域）
     * abstract method in viewgroup
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("----->", "onLayout");
        int childCount = getChildCount();
        int childWidth = 0;
        int childHeight = 0;
        MarginLayoutParams childParams = null;
        /**
         * 遍历所有childview根据其宽和高,以及margin进行布局
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            childParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;
            switch (i) {
                case 0:
                    cl = childParams.leftMargin;
                    ct = childParams.topMargin;
                    break;
                case 1:
//                    cl = getMeasuredWidth() - childWidth - childParams.rightMargin;
                    // getMeasuredWidth()也是可以的,得到的是在ViewGroup里的宽
                    cl = getWidth() - childWidth - childParams.rightMargin;
                    ct = childParams.topMargin;
                    break;
                case 2:
                    cl = childParams.leftMargin;
//                    ct = getMeasuredHeight() - childHeight - childParams.bottomMargin;
                    ct = getHeight() - childHeight - childParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - childWidth - childParams.rightMargin;
                    ct = getHeight() - childHeight - childParams.bottomMargin;
                    break;
            }
            cr = childWidth + cl;
            cb = childHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }

    }
}
