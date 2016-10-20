# CustomViewStudy
自定义控件练习
---
###参照Hongyang的CSDN博客所写：
>[http://blog.csdn.net/lmj623565791/article/category/2680591](http://blog.csdn.net/lmj623565791/article/category/2680591)

###一. 目录：

####1. [Android 自定义View (一)](http://blog.csdn.net/lmj623565791/article/details/24252901)

####2. [Android 自定义View (二) 进阶](http://blog.csdn.net/lmj623565791/article/details/24300125)

####3. [Android 自定义View (三) 圆环交替 等待效果](http://blog.csdn.net/lmj623565791/article/details/24500107)

####4. [Android 自定义View (四) 视频音量调控](http://blog.csdn.net/lmj623565791/article/details/24529807)

####5. [Android 手把手教您自定义ViewGroup（一）](http://blog.csdn.net/lmj623565791/article/details/38339817)

####6. [Android 深入理解Android中的自定义属性](http://blog.csdn.net/lmj623565791/article/details/45022631)

####7. [Android ViewDragHelper完全解析 自定义ViewGroup神器](http://blog.csdn.net/lmj623565791/article/details/46858663)
###二. 疑难及优化
####1. [Android 自定义View (一)优化](https://github.com/youlookwhat/CustomViewStudy/blob/master/file/Android 自定义View (一)优化.md)
#####1.1 关于文字显示优化：
``` java
//                int textWidth = mRect.width(); // 这样mRect.width()直接计算出来的会有误差
                float textWidth = mPaint.measureText(mTitleText);

//                int textHeight = mRect.height(); //直接计算出来的会有误差
                Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
                float textHeight = Math.abs((fontMetrics.bottom - fontMetrics.top));
```
#####1.2 onDraw里画Text时起点坐标优化：
``` java
canvas.drawText(mTitleText, 0 + getPaddingLeft(), getHeight() / 2 + mRect.height() / 2, mPaint);
``` 
>canvas.drawText(String text,float x,float y,Paint paint); x和y是绘制时的起点坐标(左下角);

>"0":  直接从"0"开始就可以(文字会自带一点默认间距)

###三. 对应图示：
<img width="150" width=“330” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_01.png"></img>
<img width="150" width=“330” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_02.png"></img>
<img width="150" width=“330” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_03.png"></img>
<img width="150" width=“330” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_04.png"></img>
<img width="150" width=“330” src="https://github.com/youlookwhat/CustomViewStudy/blob/master/file/view_05.png"></img>






