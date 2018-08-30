package com.sdwfqin.update.callback;

/**
 * 描述：进度条回调接口
 *
 * @author 张钦
 * @date 2018/8/30
 */
public interface DownloadCallback {

    /**
     * 开始
     */
    void onStart();

    /**
     * 进度
     *
     * @param progress  进度 0.00 -1.00 ，总大小
     * @param totalSize 总大小 单位B
     */
    void onProgress(int progress, int totalSize);

    /**
     * 下载完了
     */
    void onSuccess(String path);

    /**
     * 下载异常
     *
     * @param msg 异常信息
     */
    void onError(String msg);
}