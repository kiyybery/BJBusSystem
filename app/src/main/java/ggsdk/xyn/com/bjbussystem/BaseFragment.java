package ggsdk.xyn.com.bjbussystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import ggsdk.xyn.com.bjbussystem.library.PreferencesUtil;
import ggsdk.xyn.com.bjbussystem.library.StringUtil;


/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class BaseFragment extends Fragment {

    public static String ccc = "";
    public static final int COMMON_DIALOG_IDENTITY = 1;
    private Thread thread = null;
    private AsyncTask asyncTask = null;
    private ProgressDialog progressDialog;
    public Boolean hasProgressDialog = false;
    private Dialog dialog;
    protected String auth;

    public Context getApplicationContext() {
        return BBSApplication.context;
    }

    public void showToast(String message, int time) {// 提示框
        Toast.makeText(getApplicationContext(), message, time).show();
    }

    protected void showToast(int str, int time) {// 提示框
        String info = getResources().getString(str);
        Toast.makeText(getApplicationContext(), info, time).show();
    }

    public String getDeviceId() {
        String imei = "";
        Context ctx = getApplicationContext();
        if (ctx != null) {

            TelephonyManager telephonyManager = (TelephonyManager) ctx
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null)
                imei = telephonyManager.getDeviceId();

            if (TextUtils.isEmpty(imei))
                imei = "0";
        }
        return imei;
    }

    public String getAuth() {// 获取auth
        return PreferencesUtil.get(BBSApplication.KEY_AUTH, "");
    }

    protected boolean checkLogin() {
        auth = getAuth();
        if (StringUtil.isEmpty(auth)) {
            showToast("需要登录", Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    protected void forceLogin() {
        auth = getAuth();
        if (StringUtil.isEmpty(auth)) {
            showToast("需要登录", Toast.LENGTH_SHORT);
        } else {
            showToast("需要登录", Toast.LENGTH_SHORT);
        }
    }

    protected boolean checkNeedLogin() {

        auth = getAuth();
        if (StringUtil.isEmpty(auth)) {

            return false;
        } else {
            return true;
        }
    }

    // -------------进度条--------------//
    // 进度条 起
    public synchronized ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(
                getApplicationContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }

    public synchronized void startProgressBar(int message, Thread thread,
                                              boolean cancelable) {
        startProgressBar(getResources().getString(message), thread, cancelable);
    }

    public synchronized void startProgressBar(String message, Thread thread,
                                              boolean cancelable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        this.thread = thread;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        if (cancelable) {
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    closeProgressBar();
                }
            });
        }
        progressDialog.show();

    }

    public synchronized void startProgressBar(int message, AsyncTask asyncTask,
                                              boolean cancelable) {
        startProgressBar(getResources().getString(message), asyncTask,
                cancelable);
    }

    public synchronized void startProgressBar(String message,
                                              AsyncTask asyncTask, boolean cancelable) {
        this.asyncTask = asyncTask;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                closeProgressBar();
            }
        });
        progressDialog.show();
    }

    // 进度条 关
    public synchronized void closeProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
    }

    protected void finish(int resultCode, Intent data) {
        if (getActivity() != null) {
            getActivity().setResult(resultCode, data);
            getActivity().finish();
        }
    }
}
