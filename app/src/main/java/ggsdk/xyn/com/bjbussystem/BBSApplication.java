package ggsdk.xyn.com.bjbussystem;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2017/3/20 0020.
 */
public class BBSApplication extends Application {

    public static Context context;
    public static SharedPreferences preferences;
    public static final String KEY_AUTH = "key-auth";
    public static final String SHAREPREFERENCE_NAME = "BBSApplication";

    public static String IP_URL = "http://210.51.190.27:8086/";

    public static DisplayMetrics displayMetrics;
    // 原始UI界面设计图的宽度(px)，用于后期对控件做缩放
    public static final float UI_Design_Width = 720;
    public static final float UI_Design_Height = 1136;
    // 屏幕宽度缩放比（相对于原设计图）
    public static float screenWidthScale = 1f;
    public static float screenHeightScale = 1f;

    private static BBSApplication app;

    public static BBSApplication get() {

        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        //preferences = PreferenceManager.getDefaultSharedPreferences(context);
        //允许单双线程并行
        preferences = getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);

        displayMetrics = getResources().getDisplayMetrics();
        // 初始化屏幕宽度缩放比例
        screenWidthScale = displayMetrics.widthPixels / UI_Design_Width;
        screenHeightScale = displayMetrics.heightPixels / UI_Design_Height;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
