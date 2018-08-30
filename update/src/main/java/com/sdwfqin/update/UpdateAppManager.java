package com.sdwfqin.update;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.sdwfqin.update.callback.UpdateCallback;
import com.sdwfqin.update.model.UpdateAppModel;
import com.sdwfqin.update.model.UpdateVersionModel;

/**
 * 描述：版本更新管理器
 *
 * @author 张钦
 * @date 2018/8/29
 */
public class UpdateAppManager {

    // Bundle Key
    final static String INTENT_KEY = "update_dialog_values";
    final static String THEME_KEY = "theme_color";
    final static String TOP_IMAGE_KEY = "top_resId";

    private static final String TAG = UpdateAppManager.class.getSimpleName();

    private Activity mActivity;
    private HttpManager mHttpManager;
    private UpdateVersionModel mVersionModel;
    private UpdateAppModel mUpdateApp;
    private int mThemeColor;
    private @DrawableRes
    int mTopPic;
    /**
     * Android N及以上版本需要设置FILE_PROVIDER
     */
    private String mFileProvider;
    /**
     * 文件保存路径
     */
    private String mSavaPath;

    private UpdateAppManager(Builder builder) {
        mActivity = builder.getActivity();
        mHttpManager = builder.getHttpManager();
        mVersionModel = builder.getVersionModel();

        mThemeColor = builder.getThemeColor();
        mTopPic = builder.getTopPic();
        mFileProvider = builder.getFileProvider();
        mSavaPath = builder.getSavaPath();

        //添加信息
        fillUpdateAppData();
    }

    /**
     * 设置更新页面配置
     */
    private void fillUpdateAppData() {
        mUpdateApp = new UpdateAppModel();
        mUpdateApp.setHttpManager(mHttpManager);
        mUpdateApp.setFileProvider(mFileProvider);
        mUpdateApp.setSavaPath(mSavaPath);
    }

    /**
     * 跳转到更新页面
     */
    public void showDialogFragment() {
        if (mActivity != null && !mActivity.isFinishing()) {
            Bundle bundle = new Bundle();
            mUpdateApp.setVersionModel(mVersionModel);
            bundle.putSerializable(INTENT_KEY, mUpdateApp);
            if (mThemeColor != 0) {
                bundle.putInt(THEME_KEY, mThemeColor);
            }

            if (mTopPic != 0) {
                bundle.putInt(TOP_IMAGE_KEY, mTopPic);
            }

            UpdateDialogFragment
                    .newInstance(bundle)
                    .show(((FragmentActivity) mActivity).getSupportFragmentManager(), "dialog");
        }

    }

    /**
     * 最简方式
     */
    public void update() {
        checkNewApp(new UpdateCallback());
    }

    /**
     * 检测是否有新版本
     *
     * @param callback 更新回调
     */
    private void checkNewApp(UpdateCallback callback) {
        if (callback == null) {
            return;
        }

        long versionCode = 0;
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            versionCode = packageInfo.getLongVersionCode();
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "checkNewApp: " + e.getMessage());
        }
        if (mVersionModel.getApkVersionCode() > versionCode) {
            callback.hasNewApp(mUpdateApp, this);
        }
    }

    public static class Builder {

        private Activity mActivity;
        // 网络工具
        private HttpManager mHttpManager;
        // 更新版本信息
        private UpdateVersionModel mVersionModel;
        // 设置按钮，进度条的颜色
        private int mThemeColor = 0;
        // 顶部的图片
        private
        @DrawableRes
        int mTopPic = 0;
        /**
         * Android N及以上版本需要设置FILE_PROVIDER
         */
        private String mFileProvider;
        /**
         * 文件保存路径
         */
        private String mSavaPath;

        private Activity getActivity() {
            return mActivity;
        }

        /**
         * 设置当前Activity
         *
         * @param activity 当前提示的Activity
         * @return Builder
         */
        public Builder setActivity(Activity activity) {
            mActivity = activity;
            return this;
        }

        private HttpManager getHttpManager() {
            return mHttpManager;
        }

        /**
         * 设置网络工具
         *
         * @param httpManager 自己实现的网络对象
         * @return Builder
         */
        public Builder setHttpManager(HttpManager httpManager) {
            mHttpManager = httpManager;
            return this;
        }

        private UpdateVersionModel getVersionModel() {
            return mVersionModel;
        }

        /**
         * 更新地址
         *
         * @param updateModel 版本信息
         * @return Builder
         */
        public Builder setUpdateModel(UpdateVersionModel updateModel) {
            mVersionModel = updateModel;
            return this;
        }

        private int getThemeColor() {
            return mThemeColor;
        }

        /**
         * 设置按钮，进度条的颜色
         *
         * @param themeColor 设置按钮，进度条的颜色
         * @return Builder
         */
        public Builder setThemeColor(int themeColor) {
            mThemeColor = themeColor;
            return this;
        }

        private int getTopPic() {
            return mTopPic;
        }

        /**
         * 顶部的图片
         *
         * @param topPic 顶部的图片
         * @return Builder
         */
        public Builder setTopPic(int topPic) {
            mTopPic = topPic;
            return this;
        }

        private String getFileProvider() {
            return mFileProvider;
        }

        /**
         * 文件共享
         *
         * @return Builder
         */
        public Builder setFileProvider(String fileProvider) {
            mFileProvider = fileProvider;
            return this;
        }

        private String getSavaPath() {
            return mSavaPath;
        }

        /**
         * 文件保存路径
         *
         * 请务必用/结尾！！！！
         *
         * @return Builder
         */
        public Builder setSavaPath(String savaPath) {
            mSavaPath = savaPath;
            return this;
        }

        /**
         * @return 生成app管理器
         */
        public UpdateAppManager build() {
            //校验
            if (getActivity() == null || getHttpManager() == null || getVersionModel() == null) {
                throw new NullPointerException("必要参数不能为空");
            }
            return new UpdateAppManager(this);
        }
    }

}

