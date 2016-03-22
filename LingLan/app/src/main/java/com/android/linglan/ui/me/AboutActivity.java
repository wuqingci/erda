package com.android.linglan.ui.me;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LeeMy on 2016/1/7 0007.
 * 关于我们页面
 */
public class AboutActivity extends BaseActivity {
    private TextView tv_about;
    private TextView version;

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String about = (String) msg.getData().getString("about");
            switch (msg.what) {
                case 1:
                    tv_about.setText(about);
                    break;
            }
        }
    };
    @Override
    protected void setView() {
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void initView() {
        tv_about = (TextView) findViewById(R.id.tv_about);
        version = (TextView) findViewById(R.id.tv_version);
    }

    @Override
    protected void initData() {
        setTitle("关于我们", "");
        try {
            version.setText("V" + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getAbout();
    }

    @Override
    protected void setListener() {

    }

    private void getAbout(){
        NetApi.getAbout(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,AboutActivity.this)){
                    return;
                }
                try {
                    JSONObject resultJson =new JSONObject(result);
                    JSONObject data = resultJson.getJSONObject("data");
                    String about = data.getString("about");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("about",about);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    message.what = 1;// 标志是哪个线程传数据
//                    ToastUtil.show(about);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private String getVersionName() throws Exception {
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }
}
