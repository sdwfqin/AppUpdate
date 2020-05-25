
## Android 版本更新

## 目录

* [功能介绍](#功能介绍)
* [导入指南](#导入指南)
* [使用介绍](#使用介绍)
* [效果图](#效果图)
* [TODO](#todo)
* [License](#license)

## 功能介绍

- 实现android版本更新
- 强制更新在前台下载，普通更新在后台下载
- 支持Android M、N、O、P、Q

## 导入指南

1. Gradle

    ``` gradle
    implementation 'com.sdwfqin.quicklib:update:1.1.0'
    ```
2. eclipse

    ![goHome](/images/go_home_you_are_drunk.png)

## 使用介绍

1. 简单调用(需要自行处理运行时权限)

    ``` java
    mUpdateVersionModel = new UpdateVersionModel();
    // 暂时用不到MD5
    mUpdateVersionModel.setApkMd5("b97bea014531123f94c3ba7b7afbaad2");
    // 暂时用不到VersionName
    mUpdateVersionModel.setApkVersionName("3.0.0");
    // code用来判断是否是新版本
    mUpdateVersionModel.setApkVersionCode(1024);
    // 新版本下载地址
    mUpdateVersionModel.setApkUrl("http://7xvtvi.com1.z0.glb.clouddn.com/app-release.apk");
    // 更新标题
    mUpdateVersionModel.setUpdateTitle("更新测试");
    // 更新内容
    mUpdateVersionModel.setUpdateDes("1，添加删除信用卡接口。\r\n2，添加vip认证。\r\n3，区分自定义消费，一个小时不限制。\r\n4，添加放弃任务接口，小时内不生成。\r\n5，消费任务手动生成。");
    // 是否强制更新
    mUpdateVersionModel.setConstraint(false);
    // 安装包大小（仅用于展示）
    mUpdateVersionModel.setApkSize("5M");
    new UpdateAppManager
            .Builder()
            // 当前Activity
            .setActivity(this)
            // 更新地址
            .setUpdateModel(mUpdateVersionModel)
            // 实现httpManager接口的对象
            .setHttpManager(new DefaultHttpManager())
            // FileProvider
            .setFileProvider("com.sdwfqin.appupdatedemo.fileprovider")
            // 文件下载路径（必须“/”结尾）
            .setSavaPath(PathUtils.getExternalStoragePath() + "/AppUpdate/")
            .build()
            .update();
    ```

2. Android N及以上版本需要自行处理`FileProvider`（避免不必要的冲突）

    > 此处仅供参考，可查看Demo

    1. 在`Manifest`文件的`application`标签中添加

            ``` xml
            <provider
                android:name="android.support.v4.content.FileProvider"
                android:authorities="${applicationId}.fileprovider"
                android:exported="false"
                android:grantUriPermissions="true">
                <meta-data
                    android:name="android.support.FILE_PROVIDER_PATHS"
                    android:resource="@xml/file_paths_public" />
            </provider>
            ```
    2. 在`res/xml`目录下创建对应的`resource`文件(设置对应的文件下载目录)

        ``` xml
        <paths>
            <external-files-path name="name" path="AppUpdate" />
            <external-path name="name" path="AppUpdate" />
        </paths>
        ```

3. 关于默认的Apk下载

    默认的Apk下载`DefaultHttpManager`为可选，**如需使用需添加下面的依赖,并且在`Application`中初始化下载`FileDownloader.setupOnApplicationOnCreate(this);`**。如果自行实现下载接口，可参考`DefaultHttpManager`，新建一个类实现`HttpManager`中的方法即可，**注意，请务必确保回调接口在主线程中掉用**。

    ``` gradle
    implementation 'com.liulishuo.filedownloader:library:1.7.7'
    ```
    
4. 如果在Android P中出现`cleartext traffic permitted`错误

    可参考Demo与[stackoverflow](https://stackoverflow.com/questions/51770323/how-to-solve-android-p-downloadmanager-stopping-with-cleartext-http-traffic-to)中的解决方案。

## 效果图

![](/images/i_01.png)

## TODO

- 安装包MD5校验
- 忽略升级

## License

   	Copyright 2018 zhangqin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
