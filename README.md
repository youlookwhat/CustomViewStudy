# CustomViewStudy
> 主要参照Hongyang的CSDN博客所写，具体细节优化请见[这里](http://blog.csdn.net/jingbin_/article/category/6187475)。如有错误欢迎指正，如有侵权，请联系我删除。


## Blog Catalogue

 - 1.[ Android 自定义View (一)](http://blog.csdn.net/lmj623565791/article/details/24252901)

 - 2.[ Android 自定义View (二) 进阶](http://blog.csdn.net/lmj623565791/article/details/24300125)

 - 3.[ Android 自定义View (三) 圆环交替 等待效果](http://blog.csdn.net/lmj623565791/article/details/24500107)

 - 4.[ Android 自定义View (四) 视频音量调控](http://blog.csdn.net/lmj623565791/article/details/24529807)

 - 5.[ Android 手把手教您自定义ViewGroup（一）](http://blog.csdn.net/lmj623565791/article/details/38339817)

 - 6.[ Android 深入理解Android中的自定义属性](http://blog.csdn.net/lmj623565791/article/details/45022631)

 - 7.[ Android ViewDragHelper完全解析 自定义ViewGroup神器](http://blog.csdn.net/lmj623565791/article/details/46858663)



## Source Code

>- 1.[CustomTitleView.java](https://github.com/youlookwhat/CustomViewStudy/blob/master/app/src/main/java/com/example/jingbin/customview/view/CustomTitleView.java)

>- 2.[CustomImageView.java](https://github.com/youlookwhat/CustomViewStudy/blob/master/app/src/main/java/com/example/jingbin/customview/view/CustomImageView.java)

>- 3.[CustomProgressBar.java](https://github.com/youlookwhat/CustomViewStudy/blob/master/app/src/main/java/com/example/jingbin/customview/view/CustomProgressBar.java)

>- 4.[CustomVolumControlBar.java](https://github.com/youlookwhat/CustomViewStudy/blob/master/app/src/main/java/com/example/jingbin/customview/view/CustomVolumControlBar.java)

>- 5.[CustomImgContainer.java](https://github.com/youlookwhat/CustomViewStudy/blob/master/app/src/main/java/com/example/jingbin/customview/viewgroup/CustomImgContainer.java)

>- 6.[DeepUnderstandAttrActivity.java](https://github.com/youlookwhat/CustomViewStudy/blob/master/app/src/main/java/com/example/jingbin/customview/activity/DeepUnderstandAttrActivity.java)

>- 7.[VDHDeepLayout.java](https://github.com/youlookwhat/CustomViewStudy/blob/master/app/src/main/java/com/example/jingbin/customview/viewgroup/VDHDeepLayout.java)


## Picture
<img width="160" height=“274” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_00.png"></img>
<img width="160" height=“274” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_01.png"></img>
<img width="160" height=“274” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_02.png"></img>
<img width="160" height=“274” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_03.png"></img>
<img width="160" height=“274” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_04.png"></img>
<img width="160" height=“274” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_05.png"></img>
<img width="160" height=“274” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_06.png"></img>
<img width="160" height=“274” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_07.png"></img>



##  Code Optimization
###1. [CustomTitleView优化](https://github.com/youlookwhat/CustomViewStudy/blob/master/file/Android 自定义View (一)优化.md)

####1.1 关于文字显示优化：
``` java
//             int textWidth = mRect.width(); // 这样mRect.width()直接计算出来的会有误差
               float textWidth = mPaint.measureText(mTitleText);

//              int textHeight = mRect.height(); //直接计算出来的会有误差
                Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
                float textHeight = Math.abs((fontMetrics.bottom - fontMetrics.top));
```
####1.2 onDraw里画Text时起点坐标优化：
``` java
canvas.drawText(mTitleText, getWidth() / 2 - mRect.width() / 2 - mRect.left, getHeight() / 2 + mRect.height() / 2, mPaint);
``` 
>canvas.drawText(String text,float x,float y,Paint paint); x和y是绘制时的起点坐标(左下角);

>" - mRect.left":  就很标准，居中显示(csdn:yql_running解决)

###2. 圆环交替 等待效果优化

####2.1 新开线程画线，离开页面时线程未关闭优化

``` java
// 用来开关线程
    private boolean isContinue;
    
    // 绘图线程
        new Thread() {
            public void run() {
                while (isContinue) {
                    mProgress++;
                    if (mProgress == 360) {
                        mProgress = 0;
                        isNext = !isNext;
                    }
                    Log.e("--------", "在执行..");
                    postInvalidate();

                    try {
                        Thread.sleep(100 / mSpeed);// 这里优化了一下,值越大,速度越快
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
``` 
Activity相关代码：

``` java
@Override
    protected void onStop() {
        super.onStop();
        customProgressBar01.setContinue(false);
        customProgressBar02.setContinue(false);
        customProgressBar03.setContinue(false);
    }
``` 
####2.2 用户宽高若设置wrap_content时优化
``` java
@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (modeWidth == MeasureSpec.EXACTLY) {
            width = sizeWidth;
        } else {//默认宽度200dp
            width = (int) getContext().getResources().getDimension(R.dimen.width);
        }
        Log.e("------------->", "width:" + width);
        setMeasuredDimension(width, width);
    }
``` 

##Download
 - [CustomViewStudy.apk](http://download.csdn.net/detail/jingbin_/9678501)

## Reference
- [http://blog.csdn.net/lmj623565791/article/category/2680591](http://blog.csdn.net/lmj623565791/article/category/2680591)

##  Thanks
- [CSDN:张鸿洋](http://blog.csdn.net/lmj623565791)






