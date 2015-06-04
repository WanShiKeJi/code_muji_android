package test.src.com.testleanclound;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

import cn.bmob.v3.Bmob;

/**
 * Created by wanfei on 2015/5/14.
 */
public class MyApplication extends Application{

    String AppID="85m0pvb0vv1iluti5sk0xsou1mkftzn06a3f1ompvza9xc7z";
    String AppKey="orluh89ufnpvl773b68w5gcdk4dxfrahzwaahz7c46ettn44";
    String BmobAppID="0deed1e30c25364cf2bc0fef451a2fd1";
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,AppID,AppKey);
        Bmob.initialize(this,BmobAppID);
    }
}
