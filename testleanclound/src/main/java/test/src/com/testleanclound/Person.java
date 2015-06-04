package test.src.com.testleanclound;

import cn.bmob.v3.BmobObject;

/**
 * Created by wanfei on 2015/5/15.
 */
public class Person extends BmobObject{

    private String name;
    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
