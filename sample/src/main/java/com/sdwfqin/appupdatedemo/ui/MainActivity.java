package com.sdwfqin.appupdatedemo.ui;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.SDCardUtils;
import com.sdwfqin.appupdatedemo.R;
import com.sdwfqin.appupdatedemo.http.UpdateAppHttpUtil;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.update.DefaultHttpManager;
import com.sdwfqin.update.UpdateAppManager;
import com.sdwfqin.update.model.UpdateVersionModel;
import com.sdwfqin.update.utils.AppUpdateUtils;
import com.sdwfqin.update.utils.DrawableUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity {

    private UpdateVersionModel mUpdateVersionModel;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {

        DrawableUtil.setTextSolidTheme((Button) findViewById(R.id.btn_default));
        DrawableUtil.setTextSolidTheme((Button) findViewById(R.id.btn_constraint));
        ImageView im = (ImageView) findViewById(R.id.iv);
        im.setImageBitmap(AppUpdateUtils.drawableToBitmap(AppUpdateUtils.getAppIcon(this)));

        getPermission();

        mUpdateVersionModel = new UpdateVersionModel();
        mUpdateVersionModel.setApkMd5("b97bea014531123f94c3ba7b7afbaad2");
        mUpdateVersionModel.setApkVersionName("3.0.0");
        mUpdateVersionModel.setApkVersionCode(1024);
        mUpdateVersionModel.setApkUrl("http://7xvtvi.com1.z0.glb.clouddn.com/app-release.apk");
        mUpdateVersionModel.setUpdateTitle("更新测试");
        mUpdateVersionModel.setUpdateDes("1，添加删除信用卡接口。\r\n2，添加vip认证。\r\n3，区分自定义消费，一个小时不限制。\r\n4，添加放弃任务接口，小时内不生成。\r\n5，消费任务手动生成。");
        mUpdateVersionModel.setConstraint(false);
        mUpdateVersionModel.setApkSize("5M");
    }

    public void getPermission() {
        addSubscribe(new RxPermissions(this)
                .request(WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        Toast.makeText(MainActivity.this, "已授权", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "未授权", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }));
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
                .setSavaPath(SDCardUtils.getSDCardPaths().get(0) + "/AppUpdate/")
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
                .setSavaPath(SDCardUtils.getSDCardPaths().get(0) + "/AppUpdate/")
                .build()
                .update();
    }
}
