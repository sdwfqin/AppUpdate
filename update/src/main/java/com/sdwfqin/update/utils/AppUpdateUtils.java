package com.sdwfqin.update.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by Vector
 * on 2017/6/6 0006.
 */
public class AppUpdateUtils {

    public static void installApp(Context context, String appPath, String fileProvider) {
        File file = new File(appPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            Uri apkUri = FileProvider.getUriForFile(context, fileProvider, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
        }
        return "";
    }

    public static Drawable getAppIcon(Context context) {
        try {
            return context.getPackageManager().getApplicationIcon(context.getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

    }

    public static int dip2px(int dip, Context context) {
        return (int) (dip * getDensity(context) + 0.5f);
    }

    public static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 求百分比
     *
     * @return
     */
    public static double accuracy(double num, double total, int scale) {
        double accuracy_num = num / total * 100;
        return new BigDecimal(accuracy_num).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
