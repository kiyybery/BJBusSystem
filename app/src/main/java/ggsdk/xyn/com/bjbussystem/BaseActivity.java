package ggsdk.xyn.com.bjbussystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ggsdk.xyn.com.bjbussystem.library.NetWorkUtil;
import ggsdk.xyn.com.bjbussystem.library.PreferencesUtil;
import ggsdk.xyn.com.bjbussystem.library.StringUtil;


/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int COMMON_DIALOG_IDENTITY = 1022;

    protected String phone_message_baseActivity;
    protected String auth;
    private boolean showProgressBar = false;
    private Thread thread = null;
    private AsyncTask asyncTask = null;
    private boolean progressDialogCancelable = true;


    public String getDeviceId() {// 获取设备id
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public String getAuth() {// 获取auth
        return PreferencesUtil.get(BBSApplication.KEY_AUTH, "");
    }

    public void showToast(String message, int time) {// 提示框
        Toast.makeText(getApplicationContext(), message, time).show();
    }

    protected void showToast(int str, int time) {// 提示框
        String info = getResources().getString(str);
        Toast.makeText(getApplicationContext(), info, time).show();
    }

    protected boolean checkNetOK() {// 检测网络是否异常
        if (!NetWorkUtil.networkCanUse(getApplicationContext())) {
            Toast.makeText(BaseActivity.this, "请确保您处于联网状态!", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    public Activity getTopActivity() {
        Activity top = this;
        while (top.getParent() != null) {
            top = top.getParent();
        }
        return top;
    }

    /**
     * 所有子类对应的Dialog使用当前方法，方便统一控制
     *
     * @param title     dialog Title
     * @param message   dialog message
     * @param pTitle    positive Title
     * @param nTitle    Negative Title
     * @param pListener positive OnClickListener
     * @param nListener Negative OnClickListener
     */
    protected void showDialog(String title, String message, String pTitle,
                              String nTitle, DialogInterface.OnClickListener pListener,
                              DialogInterface.OnClickListener nListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getTopActivity());
        // 需要title居中，使用自定义控件
        TextView titleView = new TextView(getApplicationContext());
        titleView.setText(message);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX);
        titleView.setGravity(Gravity.CENTER);
        builder.setCustomTitle(titleView);
        builder.setCancelable(false);
        if (pListener != null) {
            pTitle = StringUtil.isEmpty(pTitle) ? "确定" : pTitle;
            builder.setPositiveButton(pTitle, pListener);
        }
        if (nListener != null) {
            nTitle = StringUtil.isEmpty(nTitle) ? "取消" : nTitle;
            builder.setNegativeButton(nTitle, nListener);
        }
        builder.create().show();

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
        this.phone_message_baseActivity = message;
        this.progressDialogCancelable = cancelable;
        this.thread = thread;
        showProgressBar = true;
        showDialog(COMMON_DIALOG_IDENTITY);
    }

    public synchronized void startProgressBar(int message, AsyncTask asyncTask,
                                              boolean cancelable) {
        startProgressBar(getResources().getString(message), asyncTask,
                cancelable);
    }

    public synchronized void startProgressBar(String message,
                                              AsyncTask asyncTask, boolean cancelable) {
        this.phone_message_baseActivity = message;
        this.asyncTask = asyncTask;
        this.progressDialogCancelable = cancelable;
        showProgressBar = true;
        showDialog(COMMON_DIALOG_IDENTITY);
    }

    // 进度条 关
    public synchronized boolean closeProgressBar() {
        try {
            removeDialog(COMMON_DIALOG_IDENTITY);
        } catch (Exception e) {
            // Dialog is not present
        }
        if (showProgressBar) {
            showProgressBar = false;
            return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case COMMON_DIALOG_IDENTITY:
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                if (phone_message_baseActivity != null) {
                    progressDialog.setMessage(phone_message_baseActivity);
                } else {
                    // progressDialog
                    // .setMessage(getString(R.string.common_waiting_please));
                }

                progressDialog.setCancelable(true);
                progressDialog
                        .setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialogInterface,
                                                 int i, KeyEvent keyEvent) {
                                if (i == KeyEvent.KEYCODE_BACK
                                        && progressDialogCancelable) {
                                    closeProgressBar();

                                    if (thread != null && thread.isAlive()) {
                                        thread.stop();
                                    }

                                    if (asyncTask != null) {
                                        asyncTask.cancel(true);
                                    }
                                    progressDialogCancelable = true;
                                    return true;
                                }
                                return false;
                            }
                        });
                return progressDialog;
            default:
                return super.onCreateDialog(id);
        }
    }

    protected boolean checkLogin() {
        auth = getAuth();
        if (StringUtil.isEmpty(auth)) {
            showToast("需要登录", Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {


    }
}
