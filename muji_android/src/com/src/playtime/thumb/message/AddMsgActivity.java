package com.src.playtime.thumb.message;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ab.util.AbViewUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.BaseActivity;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.PinyinSearch;
import com.src.playtime.thumb.widget.swipeback.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

public class AddMsgActivity extends SwipeBackActivity implements TextWatcher{

//    /**取消按钮*/
//    @ViewInject(R.id.tv_addmsg_cancel)
//    private TextView mCancel;
//    /**发送按钮*/
//    @ViewInject(R.id.tv_addmsg_send)
//    private TextView mSend;

    private final int RESULT_ADDMSG=0;

    @ViewInject(R.id.et_addmsg_address)
    private EditText mEdAddress;
    @ViewInject(R.id.et_addmsg_msg)
    private  EditText mEdMsg;
    @ViewInject(R.id.lv_addmsg_list)
    private ListView mListView;
    @ViewInject(R.id.ll_addmsg)
    private LinearLayout mLinear;

    /**短信数据容器*/
    private List<SmsModel> mData;
    /**适配器*/
    private ChatMsgAdapter mAdapter;
    /**联系人数据容器*/
    private List<ContactModel> mContactData;
    /**popupwindow的适配器*/
    private AddMsgAdapter mPopAdapter;

    /**查询到的临时数据*/
    private List<ContactModel> mTempData;

    private PopupWindow mPopup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmsg);
		ViewUtils.inject(mAct);
        init();
        initpopupView();
	}

    public void init(){
        mData=new ArrayList<SmsModel>();
        mContactData=new ArrayList<ContactModel>();
        mTempData=new ArrayList<ContactModel>();
        mAdapter = new ChatMsgAdapter(mAct, mData, mMultiItemTypeSupport);
        mListView.setAdapter(mAdapter);
        mEdAddress.addTextChangedListener(this);
    }

    public void initpopupView(){
        View mPopView= LayoutInflater.from(mAct).inflate(R.layout.contact_list_popup,null);
        mPopup=new PopupWindow(mPopView);
        mPopup.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopup.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //再设置模式，和Activity的一样，覆盖，调整大小。
        mPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ListView mPopList= (ListView) mPopView.findViewById(R.id.lv_contact_popup);
        mPopAdapter=new AddMsgAdapter(mAct,mContactData,R.layout.contact_item_popup,null);
        mPopList.setAdapter(mPopAdapter);
        mPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               mEdAddress.setText(mTempData.get(position).getName());
                mPopup.dismiss();
            }
        });
    }

    @Override
    @OnClick({R.id.tv_addmsg_cancel,R.id.tv_addmsg_send,R.id.tv_addmsg_add})
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.tv_addmsg_cancel:
                finish();
                break;
            case R.id.tv_addmsg_send:
                if (mEdMsg.getText().toString().equals("")) {
                    return;
                }
                SmsModel temp = new SmsModel();
                temp.setBody(mEdMsg.getText().toString());
//                temp.setName(mTvName.getText().toString());
//                temp.setAddress(mTvName.getText().toString());
                temp.setDate(System.currentTimeMillis()+"");
                temp.setType("0");
                mData.add(temp);
                mAdapter.refresh(mData, "");
                mEdMsg.setText("");
                mListView.setSelection(mAdapter.getCount());
                break;
            case R.id.tv_addmsg_add:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("vnd.android.cursor.dir/phone");
                startActivityForResult(intent,RESULT_ADDMSG);
                break;
        }
    }

    MultiItemTypeSupport<SmsModel> mMultiItemTypeSupport = new MultiItemTypeSupport<SmsModel>() {

        @Override
        public int getLayoutId(int position, SmsModel t) {
            if (t.getType().equals("1")) {
                return R.layout.chatmsg_left;
            }
            return R.layout.chatmsg_right;
        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return 2;
        }

        @Override
        public int getItemViewType(int postion, SmsModel t) {

            return 0;
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().equals("")){
            mPopup.dismiss();
            return;
        }
        mPopup.showAsDropDown(mLinear);
        mTempData = PinyinSearch.FindPinyin(s.toString(),
                mApp.mContactDatas, true);
        mPopAdapter.refresh(mTempData,"");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_ADDMSG:
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                mEdAddress.setText(name);
                mPopup.dismiss();
              //  mContactText.setSelection(number.length());
                break;
        }
    }
}
