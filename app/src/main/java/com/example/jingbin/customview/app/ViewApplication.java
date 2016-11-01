package com.example.jingbin.customview.app;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by jingbin on 2016/10/20.
 */

public class ViewApplication extends Application {

    private static ViewApplication viewApplication;
    // 存放Activity的list
    private ArrayList<Activity> activities;

    public static ViewApplication getInstance() {
        if (viewApplication == null) {
            synchronized (ViewApplication.class) {
                if (viewApplication == null) {
                    viewApplication = new ViewApplication();
                }
            }
        }
        return viewApplication;
    }

    private static int applicationThreadId;
    private static Handler uiHnadler;

    @SuppressWarnings({"unused"})
    @Override
    public void onCreate() {
        super.onCreate();
        viewApplication = this;
    }


    public static int getApplicationThread() {
        return applicationThreadId;
    }

    public static Handler getUiHnadler() {
        return uiHnadler;
    }


    /**
     * 最多保留四个Activity实例对象
     */
    public void deleteOtherActivity(Activity activity) {
        if (activities == null) {
            activities = new ArrayList<Activity>();
        }
        activities.add(activity);
        if (activities.size() > 4) {
            activities.get(0).finish();
            activities.remove(0);
        }
    }

    /**
     * 用于清除activities
     */
    public void cleanActivities() {
        if (activities != null) {
            activities.clear();
            activities = null;
        }
    }

    /**
     * 把当前Activity从activities中移除
     */
    public void removeActivity() {
        if (activities != null && activities.size() > 0) {
            activities.remove(activities.size() - 1);
        }
    }
}
