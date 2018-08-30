package com.sdwfqin.update;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdwfqin.update.callback.DownloadCallback;
import com.sdwfqin.update.model.UpdateAppModel;
import com.sdwfqin.update.model.UpdateVersionModel;
import com.sdwfqin.update.service.DownloadService;
import com.sdwfqin.update.utils.AppUpdateUtils;
import com.sdwfqin.update.utils.ColorUtil;
import com.sdwfqin.update.utils.DrawableUtil;
import com.sdwfqin.update.view.NumberProgressBar;

/**
 * 描述：更新弹窗
 *
 * @author 张钦
 * @date 2018/8/29
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TIPS = "请授权访问存储空间权限，否则App无法更新";
    public static boolean isShow = false;
    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private UpdateAppModel mUpdateApp;
    private UpdateVersionModel mVersionModel;
    private NumberProgressBar mNumberProgressBar;
    private ImageView mIvClose;
    private TextView mTitleTextView;
    /**
     * 回调
     */
    private ServiceConnection conn = new DownloadServiceConnection();
    private LinearLayout mLlClose;
    // 默认色
    private int mDefaultColor = 0xffe94339;
    private int mDefaultPicResId = R.mipmap.lib_update_app_top_bg;
    private ImageView mTopIv;
    private DownloadService.DownloadBinder mDownloadBinder;
    private Activity mActivity;
    private String mFileProvider;

    public static UpdateDialogFragment newInstance(Bundle args) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);

        mActivity = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(false);

        // 强制更新点击返回直接退到桌面，普通更新关闭弹窗
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // 禁用
                if (mUpdateApp != null && mVersionModel.isConstraint()) {
                    // 返回桌面
                    startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        });

        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        lp.height = (int) (displayMetrics.heightPixels * 0.8f);
        dialogWindow.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lib_update_app_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        // 提示内容
        mContentTextView = view.findViewById(R.id.tv_update_info);
        // 标题
        mTitleTextView = view.findViewById(R.id.tv_title);
        // 更新按钮
        mUpdateOkButton = view.findViewById(R.id.btn_ok);
        // 进度条
        mNumberProgressBar = view.findViewById(R.id.npb);
        // 关闭按钮
        mIvClose = view.findViewById(R.id.iv_close);
        // 关闭按钮+线 的整个布局
        mLlClose = view.findViewById(R.id.ll_close);
        // 顶部图片
        mTopIv = view.findViewById(R.id.iv_top);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        mUpdateApp = (UpdateAppModel) getArguments().getSerializable(UpdateAppManager.INTENT_KEY);
        mVersionModel = mUpdateApp.getVersionModel();
        mFileProvider = mUpdateApp.getFileProvider();
        // 设置主题色
        initTheme();

        if (mUpdateApp != null) {
            // 弹出对话框
            final String dialogTitle = mVersionModel.getUpdateTitle();
            final String newVersion = mVersionModel.getApkVersionName();
            final String targetSize = mVersionModel.getApkSize();
            final String updateLog = mVersionModel.getUpdateDes();

            String msg = "";

            if (!TextUtils.isEmpty(targetSize)) {
                msg = "新版本大小：" + targetSize + "\n\n";
            }

            if (!TextUtils.isEmpty(updateLog)) {
                msg += updateLog;
            }

            // 更新内容
            mContentTextView.setText(msg);
            // 标题
            mTitleTextView.setText(TextUtils.isEmpty(dialogTitle) ? String.format("是否升级到%s版本？", newVersion) : dialogTitle);
            // 强制更新隐藏关闭按钮
            if (mVersionModel.isConstraint()) {
                mLlClose.setVisibility(View.GONE);
            }

            initEvents();
        }
    }

    /**
     * 初始化主题色
     */
    private void initTheme() {

        final int color = getArguments().getInt(UpdateAppManager.THEME_KEY, -1);
        final int topResId = getArguments().getInt(UpdateAppManager.TOP_IMAGE_KEY, -1);

        if (-1 == topResId) {
            if (-1 == color) {
                // 默认红色
                setDialogTheme(mDefaultColor, mDefaultPicResId);
            } else {
                setDialogTheme(color, mDefaultPicResId);
            }

        } else {
            if (-1 == color) {
                setDialogTheme(mDefaultColor, topResId);
            } else {
                // 更加指定的上色
                setDialogTheme(color, topResId);
            }
        }
    }

    /**
     * 设置颜色
     *
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mTopIv.setImageResource(topResId);
        mUpdateOkButton.setBackground(DrawableUtil.getDrawable(AppUpdateUtils.dip2px(4, getActivity()), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mUpdateOkButton.setTextColor(ColorUtil.isTextColorDark(color) ? Color.BLACK : Color.WHITE);
    }

    /**
     * 初始化点击事件
     */
    private void initEvents() {
        mUpdateOkButton.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        // TODO：开始下载
        if (i == R.id.btn_ok) {
            // 权限判断是否有访问外部存储空间权限
            int flag = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (flag != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                    Toast.makeText(getActivity(), TIPS, Toast.LENGTH_LONG).show();
                } else {
                    // 申请授权。
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            } else {
                installApp();
            }

        } else if (i == R.id.iv_close) {
            // 关闭
            cancelDownloadService();
            dismiss();
        }
    }

    public void cancelDownloadService() {
        if (mDownloadBinder != null) {
            // 标识用户已经点击了更新，之后点击取消
            mDownloadBinder.stop("取消下载");
        }
    }

    /**
     * 开始下载
     */
    private void installApp() {
        if (mVersionModel.isConstraint()) {
            // 强制更新（仅限前台下载）
            downloadApp();
        } else {
            // 普通更新（后台下载）
            downloadApp();
            dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //升级
                installApp();
            } else {
                //提示，并且关闭
                Toast.makeText(getActivity(), TIPS, Toast.LENGTH_LONG).show();
                dismiss();
            }
        }

    }

    /**
     * 开启后台服务下载
     */
    private void downloadApp() {
        //使用ApplicationContext延长他的生命周期
        DownloadService.bindService(getActivity().getApplicationContext(), conn);
    }

    /**
     * 回调监听下载
     */
    private void startDownloadApp(DownloadService.DownloadBinder binder) {
        // 开始下载，监听下载进度，可以用对话框显示
        if (mUpdateApp != null) {
            this.mDownloadBinder = binder;
            binder.start(mUpdateApp, new DownloadCallback() {
                @Override
                public void onStart() {
                    if (!UpdateDialogFragment.this.isRemoving()) {
                        mNumberProgressBar.setVisibility(View.VISIBLE);
                        mUpdateOkButton.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onProgress(int progress, int totalSize) {
                    if (!UpdateDialogFragment.this.isRemoving()) {
                        mNumberProgressBar.setProgress((int) AppUpdateUtils.accuracy(progress, totalSize, 2));
                        mNumberProgressBar.setMax(100);
                    }
                }

                @Override
                public void onSuccess(String path) {
                    showInstallBtn(path);
                }

                @Override
                public void onError(String msg) {
                    if (!UpdateDialogFragment.this.isRemoving()) {
                        dismissAllowingStateLoss();
                    }
                }
            });
        }
    }

    private void showInstallBtn(String path) {
        mNumberProgressBar.setVisibility(View.GONE);
        mUpdateOkButton.setVisibility(View.VISIBLE);
        mUpdateOkButton.setText("安装");
//        mUpdateOkButton.setOnClickListener(v ->
//                AppUpdateUtils.installApp(mActivity, file, mFileProvider)
//        );
    }

    @Override
    public void onDestroyView() {
        isShow = false;
        super.onDestroyView();
    }

    class DownloadServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            startDownloadApp((DownloadService.DownloadBinder) iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }
}

