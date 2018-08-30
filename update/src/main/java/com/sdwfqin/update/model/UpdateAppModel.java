package com.sdwfqin.update.model;

import com.sdwfqin.update.HttpManager;

import java.io.Serializable;

/**
 * 版本信息
 */
public class UpdateAppModel implements Serializable {

    /**
     * 网络工具
     */
    private HttpManager httpManager;
    /**
     * 文件保存路径
     */
    private String savaPath;
    /**
     * Android N及以上版本需要设置FILE_PROVIDER
     */
    public String fileProvider;
    /**
     * 更新的文件信息
     */
    private UpdateVersionModel mVersionModel;

    public UpdateVersionModel getVersionModel() {
        return mVersionModel;
    }

    public void setVersionModel(UpdateVersionModel versionModel) {
        mVersionModel = versionModel;
    }

    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    public String getSavaPath() {
        return savaPath;
    }

    public void setSavaPath(String savaPath) {
        this.savaPath = savaPath;
    }

    public String getFileProvider() {
        return fileProvider;
    }

    public void setFileProvider(String fileprovider) {
        this.fileProvider = fileprovider;
    }
}
