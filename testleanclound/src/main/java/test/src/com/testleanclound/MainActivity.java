package test.src.com.testleanclound;

import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.RequestEmailVerifyCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.listener.SaveListener;


public class MainActivity extends ActionBarActivity {

    private RelativeLayout mll;
    private LinearLayout mlll;
    private int tempX;

    /**
     * javah -d jni -classpath D:\Andro
        idStudioSDK\platforms\android-18;D:\code_muji_android\testleanclound
     \  build\intermediates\classes\debug
     * @param savedInstanceState
     */

    static {
        System.loadLibrary("JniTest");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bmob();
       // leanCloud();
        init();

      //  String location= GetLocationByNumber.getCallerInfo("1357679",getApplicationContext());
    }


    public void init(){

        Scroller scroller=new Scroller(this);
        mll=(RelativeLayout) findViewById(R.id.ll_test);
        mlll= (LinearLayout) findViewById(R.id.ll_mian);
        mll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                       tempX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int deltaX = tempX - moveX;
                        tempX = moveX;
                        mlll.scrollBy(deltaX,0);
                        break;
                }
                return true;
            }
        });
    }

    public void leanCloud(){
        AVUser avUser=new AVUser();
        avUser.setUsername("445322416@qq.com");
        avUser.setPassword("123456");
        avUser.setEmail("445322416@qq.com");
        avUser.put("mujiphone","13058121800");
        avUser.setMobilePhoneNumber("13058121800");
        avUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {

            }
        });
//        AVUser.requestMobilePhoneVerifyInBackground("13058121800",new RequestMobileCodeCallback() {
//
//            @Override
//            public void done(AVException e) {
//                //发送了验证码以后做点什么呢
//            }
//        });

//        AVUser.requestEmailVerfiyInBackground("643639786@qq.com", new RequestEmailVerifyCallback() {
//
//            @Override
//            public void done(AVException e) {
//
//            }
//        });

//        final TextView tv= (TextView) findViewById(R.id.progress);
//        final ImageView img= (ImageView) findViewById(R.id.img);
//        AVObject gameScore = new AVObject("GameScore");
//        gameScore.put("score", 1200);
//        gameScore.put("playerName", "steve");
//        gameScore.put("level", 10);
//        gameScore.put("test","very good");
//        gameScore.saveInBackground();
        // AVQuery<AVObject> query = new AVQuery<AVObject>("GameScore");
        // query.whereContainsAll();
//        query.findInBackground(new FindCallback<AVObject>() {
//            public void done(List<AVObject> avObjects, AVException e) {
//                if (e == null) {
//                    AVFile avFile=avObjects.get(0).getAVFile("image");
//                    avFile.getDataInBackground(new GetDataCallback() {
//                        @Override
//                        public void done(byte[] bytes, AVException e) {
//                        img.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
//                        }
//                    },new ProgressCallback() {
//                        @Override
//                        public void done(Integer integer) {
//                            tv.setText(integer.intValue()+"%");
//                            Log.e("progressCallback-----","progress--"+integer.intValue());
//                        }
//                    });
//                    String url=avFile.getUrl();
//                    Log.e("Image---","---"+url);
//                    Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
//                } else {
//                    Log.d("失败", "查询错误: " + e.getMessage());
//                }
//            }
//        });

        //String url=avFile.getUrl();
        /// Log.e("Image---",url);
        AVCloud.setProductionMode(false);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("name","world");
        AVCloud.callFunctionInBackground("helloArray", parameters, new FunctionCallback() {
            public void done(Object object, AVException e) {
                if (e == null) {
                } else {

                }
            }
        });
    }


    public void Bmob(){
        final Person p2 = new Person();
        p2.setName("lucky");
        p2.setAddress("北京海淀");
        p2.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.e("添加数据成功，返回objectId为：",p2.getObjectId());
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Log.e("创建数据失败：", msg);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
