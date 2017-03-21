package ggsdk.xyn.com.bjbussystem;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

import ggsdk.xyn.com.bjbussystem.library.TabEntity;
import ggsdk.xyn.com.bjbussystem.library.ViewFindUtils;

public class MainActivity extends AppCompatActivity {

    private int[] iconResid =
            {R.drawable.footer_home_off,
                    R.drawable.footer_discover_off,
                    R.drawable.footer_msg_off,
                    R.drawable.footer_me_off};

    private int[] iconResidClick =
            {R.drawable.footer_home_on,
                    R.drawable.footer_discover_on,
                    R.drawable.footer_msg_on,
                    R.drawable.footer_me_on};

    private String[] titles = {"首页", "发现", "消息", "我的"};
    private ArrayList<Fragment> fragments2 = new ArrayList<>();
    private ArrayList<CustomTabEntity> tabs = new ArrayList<>();
    private CommonTabLayout tl;
    private View decorView;
    private Context context;
    Message m = null;
    String DEVICE_ID;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        DEVICE_ID = tm.getDeviceId();

        if (savedInstanceState == null) {
            //initFragment();

            for (int i = 0; i < titles.length; i++) {
                tabs.add(new TabEntity(titles[i], iconResidClick[i], iconResid[i]));
            }
            //fragments2.add(homeMain.newInstance());
            //fragments2.add(discoverNew.newInstance());
            //fragments2.add(messageParentFrag.newInstance());
            //fragments2.add(myProfileFragment.newInstance());

            decorView = getWindow().getDecorView();
            tl = ViewFindUtils.find(decorView, R.id.tl_2);
            tl.setTabData(tabs, this, R.id.home_fragment_container, fragments2);
        }
    }
}
