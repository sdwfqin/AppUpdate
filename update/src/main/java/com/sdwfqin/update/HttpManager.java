package com.sdwfqin.update;

import android.support.annotation.NonNull;

import com.sdwfqin.update.callback.FileDownloadCallback;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * app版本更新接口
 */
public interface HttpManager extends Serializable {

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileDownloadCallback callback);
}
