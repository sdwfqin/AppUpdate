package com.sdwfqin.appupdatedemo;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.liulishuo.filedownloader.FileDownloader;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

/**
 * Created by Vector
 * on 2017/7/17 0017.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化工具类
        Utils.init(this);
        // 初始化下载模块
        FileDownloader.setupOnApplicationOnCreate(this);
        QMUISwipeBackActivityManager.init(this);
    }
}
