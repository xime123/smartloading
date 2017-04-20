package com.xm.smartloading.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.xm.smartloading.R;

/**
 * Created by admin on 2017/4/20.
 */

public class LoadingDialog {
    private static LoadingDialog mLoading;
    private static Dialog loadingDialog;



    public static void loadingDialog(Context ct) {
        cancleDialog();

        View view = LayoutInflater.from(ct).inflate(R.layout.loading_dialog2, null);
        loadingDialog = new Dialog(ct, R.style.loading_dialog2);
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(false);//不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(true);
        loadingDialog.show();

    }

    public static void cancleDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {

            loadingDialog.dismiss();

        }
        loadingDialog = null;

    }
}
