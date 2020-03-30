package com.sdwfqin.update;

import androidx.annotation.NonNull;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.sdwfqin.update.callback.FileDownloadCallback;

/**
 * 描述：文件下载网络管理器默认实现
 *
 * @author 张钦
 * @date 2018/8/29
 */
public class DefaultHttpManager implements HttpManager {

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileDownloadCallback callback) {
        FileDownloader
                .getImpl()
                .create(url)
                .setPath(path + fileName, false)
                .setListener(new FileDownloadListener() {
                    /**
                     * 等待，已经进入下载队列
                     */
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        callback.onBefore();
                    }

                    /**
                     * 已连接
                     */
                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    /**
                     * 下载进度回调
                     */
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        callback.onProgress(soFarBytes, totalBytes);
                    }

                    /**
                     * 在完成前同步调用该方法，此时已经下载完成
                     */
                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    /**
                     * 重试之前把将要重试是第几次回调回来
                     */
                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    /**
                     * 完成整个下载过程
                     */
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        callback.onSuccess(task.getPath());
                    }

                    /**
                     * 暂停下载
                     */
                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    /**
                     * 下载出现错误
                     */
                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    /**
                     * 在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
                     */
                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                })
                // 开始下载
                .start();
    }
}