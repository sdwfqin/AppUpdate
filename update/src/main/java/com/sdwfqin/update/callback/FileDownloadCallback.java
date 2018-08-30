package com.sdwfqin.update.callback;

/**
 * 描述：下载回调
 *
 * @author 张钦
 * @date 2018/8/30
 */
public interface FileDownloadCallback {
    /**
     * 进度
     *
     * @param progress 进度0.00 - 0.50  - 1.00
     * @param total    文件总大小 单位字节
     */
    void onProgress(int progress, int total);

    /**
     * 错误回调
     *
     * @param error 错误提示
     */
    void onError(String error);

    /**
     * 结果回调
     */
    void onSuccess(String path);

    /**
     * 请求之前
     */
    void onBefore();
}