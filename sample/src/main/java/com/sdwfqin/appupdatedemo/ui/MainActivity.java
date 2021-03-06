package com.sdwfqin.appupdatedemo.ui;

import android.Manifest;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.PathUtils;
import com.sdwfqin.appupdatedemo.databinding.ActivityMainBinding;
import com.sdwfqin.appupdatedemo.http.UpdateAppHttpUtil;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.update.DefaultHttpManager;
import com.sdwfqin.update.UpdateAppManager;
import com.sdwfqin.update.model.UpdateVersionModel;
import com.sdwfqin.update.utils.AppUpdateUtils;
import com.sdwfqin.update.utils.DrawableUtil;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private UpdateVersionModel mUpdateVersionModel;

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {

        DrawableUtil.setTextSolidTheme(mBinding.btnDefault);
        DrawableUtil.setTextSolidTheme(mBinding.btnConstraint);
        mBinding.iv.setImageBitmap(AppUpdateUtils.drawableToBitmap(AppUpdateUtils.getAppIcon(this)));

        String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        initCheckPermissions(perms, new OnPermissionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "未授权", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mUpdateVersionModel = new UpdateVersionModel();
        mUpdateVersionModel.setApkMd5("b97bea014531123f94c3ba7b7afbaad2");
        mUpdateVersionModel.setApkVersionName("3.0.0");
        mUpdateVersionModel.setApkVersionCode(1024);
        mUpdateVersionModel.setApkUrl("http://acj5.pc6.com/pc6_soure/2020-3-28/bc72f3c1e3d1bf6zug4tizBrqbKrHX.apk");
        mUpdateVersionModel.setUpdateTitle("更新测试");
        mUpdateVersionModel.setUpdateDes("1，添加删除信用卡接口。\r\n2，添加vip认证。\r\n3，区分自定义消费，一个小时不限制。\r\n4，添加放弃任务接口，小时内不生成。\r\n5，消费任务手动生成。");
        mUpdateVersionModel.setConstraint(false);
        mUpdateVersionModel.setApkSize("5M");
    }

    /**
     * 普通更新
     */
    public void updateApp(View view) {
        mUpdateVersionModel.setConstraint(false);
        new UpdateAppManager
                .Builder()
                // 当前Activity
                .setActivity(this)
                // 更新地址
                .setUpdateModel(mUpdateVersionModel)
                // 实现httpManager接口的对象
                .setHttpManager(new DefaultHttpManager())
                .setFileProvider("com.sdwfqin.appupdatedemo.fileprovider")
                .setSavePath(PathUtils.getAppDataPathExternalFirst() + "/AppUpdate/")
                .build()
                .update();
    }

    /**
     * 强制更新
     */
    public void constraintUpdate(View view) {
        mUpdateVersionModel.setConstraint(true);
        new UpdateAppManager
                .Builder()
                // 当前Activity
                .setActivity(this)
                // 更新地址
                .setUpdateModel(mUpdateVersionModel)
                // 实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .setFileProvider("com.sdwfqin.appupdatedemo.fileprovider")
                .setSavePath(PathUtils.getAppDataPathExternalFirst() + "/AppUpdate/")
                .build()
                .update();
    }
}
