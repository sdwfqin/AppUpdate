package com.sdwfqin.update.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.sdwfqin.update.R;
import com.sdwfqin.update.callback.DownloadCallback;
import com.sdwfqin.update.callback.FileDownloadCallback;
import com.sdwfqin.update.model.UpdateAppModel;
import com.sdwfqin.update.model.UpdateVersionModel;
import com.sdwfqin.update.utils.AppUpdateUtils;

/**
 * 后台下载
 */
public class DownloadService extends Service {

    private static final int NOTIFY_ID = 0x01;
    private static final String CHANNEL_ID = "app_update_id";
    private static final CharSequence CHANNEL_NAME = "app_update_channel";

    public static boolean isRunning = false;
    private NotificationManager mNotificationManager;
    private DownloadBinder binder = new DownloadBinder();
    private NotificationCompat.Builder mBuilder;
    private String mFileProvider;

    public static void bindService(Context context, ServiceConnection connection) {
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        isRunning = true;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isRunning = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 返回自定义的DownloadBinder实例
        return binder;
    }

    @Override
    public void onDestroy() {
        mNotificationManager = null;
        super.onDestroy();
    }

    /**
     * 创建通知
     */
    private void setUpNotification(String title) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(false);
            channel.enableLights(false);

            mNotificationManager.createNotificationChannel(channel);
        }

        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.lib_update_app_update_icon)
                .setLargeIcon(AppUpdateUtils.drawableToBitmap(AppUpdateUtils.getAppIcon(DownloadService.this)))
                .setOngoing(true)
                .setAutoCancel(true)
                .setProgress(100, 0, false);
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
    }

    /**
     * 更新进度条
     *
     * @param progress
     */
    public void updateProgress(double progress) {
        // 修改进度条
        mBuilder.setProgress(100, (int) progress, false)
                .setContentText(progress + "%")
                .setWhen(System.currentTimeMillis());
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
        mNotificationManager.notify(NOTIFY_ID, notification);
    }

    /**
     * 下载模块
     */
    private void startDownload(UpdateAppModel updateApp, final DownloadCallback callback) {
        UpdateVersionModel versionModel = updateApp.getVersionModel();

        mFileProvider = updateApp.getFileProvider();

        String apkUrl = versionModel.getApkUrl();
        if (TextUtils.isEmpty(apkUrl)) {
            String contentText = "新版本下载路径错误";
            stop(contentText);
            return;
        }
        String fileName = AppUpdateUtils.getAppName(getApplicationContext()) + "_" + versionModel.getApkVersionCode() + ".apk";

        updateApp.getHttpManager().download(apkUrl, updateApp.getSavaPath(), fileName, new FileDownloadCallBack(updateApp.getVersionModel(), callback));
    }

    private void stop(String contentText) {
        if (mBuilder != null) {
            mBuilder.setContentTitle(AppUpdateUtils.getAppName(DownloadService.this))
                    .setContentText(contentText)
                    .setProgress(0, 0, false);
            Notification notification = mBuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(NOTIFY_ID, notification);
        }
        close();
    }

    private void close() {
        stopSelf();
        isRunning = false;
    }

    /**
     * DownloadBinder中定义了一些实用的方法
     *
     * @author user
     */
    public class DownloadBinder extends Binder {
        /**
         * 开始下载
         *
         * @param updateApp 新app信息
         * @param callback  下载回调
         */
        public void start(UpdateAppModel updateApp, DownloadCallback callback) {
            //下载
            startDownload(updateApp, callback);
        }

        public void stop(String msg) {
            DownloadService.this.stop(msg);
        }
    }

    /**
     * 下载进度回调
     */
    class FileDownloadCallBack implements FileDownloadCallback {
        private DownloadCallback mCallBack;
        private UpdateVersionModel mVersionModel;

        public FileDownloadCallBack(UpdateVersionModel versionModel, @Nullable DownloadCallback callback) {
            super();
            this.mCallBack = callback;
            mVersionModel = versionModel;
        }

        @Override
        public void onBefore() {
            if (mVersionModel.isConstraint()) {
                if (mCallBack != null) {
                    mCallBack.onStart();
                }
            } else {
                //初始化通知栏
                setUpNotification(mVersionModel.getUpdateTitle());
            }
        }

        @Override
        public void onProgress(int progress, int total) {
            if (mVersionModel.isConstraint()) {
                if (mCallBack != null) {
                    mCallBack.onProgress(progress, total);
                }
            } else {
                updateProgress(AppUpdateUtils.accuracy(progress, total, 2));
            }
        }

        @Override
        public void onError(String error) {
            if (mVersionModel.isConstraint()) {
                if (mCallBack != null) {
                    mCallBack.onError(error);
                }
            }
            stop(error);
        }

        @Override
        public void onSuccess(String path) {
            if (mVersionModel.isConstraint()) {
                mCallBack.onSuccess(path);
            }
            //App前台运行
            mNotificationManager.cancel(NOTIFY_ID);
            AppUpdateUtils.installApp(DownloadService.this, path, mFileProvider);

            close();
        }
    }
}
