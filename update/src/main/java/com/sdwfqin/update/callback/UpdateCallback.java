package com.sdwfqin.update.callback;

import com.sdwfqin.update.UpdateAppManager;
import com.sdwfqin.update.model.UpdateAppModel;

/**
 * 新版本版本检测回调
 */
public class UpdateCallback {

    /**
     * 有新版本
     *
     * @param updateAppManager app更新管理器
     */
    public void hasNewApp(UpdateAppModel updateAppModel, UpdateAppManager updateAppManager) {
        updateAppManager.showDialogFragment();
    }
}
