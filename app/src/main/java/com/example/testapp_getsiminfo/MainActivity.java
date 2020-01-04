package com.example.testapp_getsiminfo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {

    private static String TAG = "GetPhoneInfo";
    private TelephonyManager telephonyManager;
    private String []statusName=new String[]{};
    private ListView listShow;
    private ArrayList<String> statusValue=new ArrayList<String>();
    private int soltId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listShow = (ListView)findViewById(R.id.listShow);



        statusName = getResources().getStringArray(R.array.statusName);
        String simStatus[]=getResources().getStringArray(R.array.simStatus);
        String phoneType[]=getResources().getStringArray(R.array.phoneType);

        for (int i = 0; i < 2; i++)
        {
            SubscriptionInfo Subinfo = getSIMInfo(this, i);
            if (Subinfo != null) {
                statusValue.add(String.valueOf(Subinfo.getSimSlotIndex()));
                statusValue.add(String.valueOf(Subinfo.getDataRoaming()));
                statusValue.add(String.valueOf(Subinfo.getDisplayName()));
                statusValue.add(String.valueOf(Subinfo.getIccId()));
                statusValue.add(String.valueOf(Subinfo.getMcc()));
                statusValue.add(String.valueOf(Subinfo.getMnc()));
                statusValue.add(String.valueOf(Subinfo.getNumber()));
                statusValue.add(String.valueOf(Subinfo.getSubscriptionId()));
            }
        }

        List<Map<String, Object>> listItems=new ArrayList<Map<String,Object>>();
        // 遍历statusValues集合。将statusNames、statusValues
        // 的数据封装到List<Map<String , String>>集合中
        for (int i = 0; i < statusValue.size(); i++) {
            Map<String, Object>listItem = new HashMap<String, Object>();
            listItem.put("name",statusName[i%statusName.length]);
            listItem.put("value",statusValue.get(i));
            listItems.add(listItem);
        }
        SimpleAdapter adapter=new SimpleAdapter(this, listItems, R.layout.item,
                new String[]{"name","value"},new int[]{R.id.textname,R.id.textvalue});
        //为listShow设置Adapter
        listShow.setAdapter(adapter);
    }

    private SubscriptionInfo getSIMInfo(Context cxt, int SlotIndex){
        SubscriptionManager subscriptionManager = (SubscriptionManager)cxt.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE ) == PackageManager.PERMISSION_GRANTED){
        }

        List<SubscriptionInfo> infos = subscriptionManager.getActiveSubscriptionInfoList();
        SubscriptionInfo infoRet = null;

        for (SubscriptionInfo info:infos){
            Log.e(TAG, info.toString());
            if (info.getSimSlotIndex() ==  SlotIndex){
                infoRet = info;
                Toast.makeText(getBaseContext(),"SIM subid = " + SlotIndex,Toast.LENGTH_LONG).show();
                return infoRet;
            }
        }
        Toast.makeText(getBaseContext(),"SIM nothing",Toast.LENGTH_LONG).show();
        return null;
    }

}
