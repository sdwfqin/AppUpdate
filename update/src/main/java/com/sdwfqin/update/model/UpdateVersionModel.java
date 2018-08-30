package com.sdwfqin.update.model;

import java.io.Serializable;

/**
 * 描述：版本更新信息
 *
 * @author 张钦
 * @date 2018/8/29
 */
public class UpdateVersionModel implements Serializable {

    /**
     * 版本号
     */
    private String apkVersionName;
    /**
     * 版本号
     */
    private long apkVersionCode;
    /**
     * apk地址
     */
    private String apkUrl;
    /**
     * 标题
     */
    private String updateTitle;
    /**
     * 描述
     */
    private String updateDes;
    /**
     * apkMd5
     */
    private String apkMd5;
    /**
     * 文件大小
     */
    private String apkSize;
    /**
     * 是否强制更新
     * <p>
     * true：开启强制更新
     */
    private boolean constraint;

    public UpdateVersionModel() {
    }

    public UpdateVersionModel(String apkVersionName, Integer apkVersionCode, String apkUrl, String updateTitle, String updateDes, String apkMd5, String apkSize, boolean constraint) {
        this.apkVersionName = apkVersionName;
        this.apkVersionCode = apkVersionCode;
        this.apkUrl = apkUrl;
        this.updateTitle = updateTitle;
        this.updateDes = updateDes;
        this.apkMd5 = apkMd5;
        this.apkSize = apkSize;
        this.constraint = constraint;
    }

    @Override
    public String toString() {
        return "UpdateVersionModel{" +
                "apkVersionName='" + apkVersionName + '\'' +
                "apkVersionCode='" + apkVersionCode + '\'' +
                ", apkUrl='" + apkUrl + '\'' +
                ", updateTitle='" + updateTitle + '\'' +
                ", updateDes='" + updateDes + '\'' +
                ", apkMd5='" + apkMd5 + '\'' +
                ", apkSize='" + apkSize + '\'' +
                ", constraint=" + constraint +
                '}';
    }

    public String getApkVersionName() {
        return apkVersionName;
    }

    public void setApkVersionName(String apkVersionName) {
        this.apkVersionName = apkVersionName;
    }

    public long getApkVersionCode() {
        return apkVersionCode;
    }

    public void setApkVersionCode(long apkVersionCode) {
        this.apkVersionCode = apkVersionCode;
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }

    public String getUpdateDes() {
        return updateDes;
    }

    public void setUpdateDes(String updateDes) {
        this.updateDes = updateDes;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    public boolean isConstraint() {
        return constraint;
    }

    public void setConstraint(boolean constraint) {
        this.constraint = constraint;
    }
}
