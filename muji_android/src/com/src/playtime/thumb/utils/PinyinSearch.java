package com.src.playtime.thumb.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.util.Log;

import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.bean.SmsModel;

/**
 * @author wanfei
 */
public class PinyinSearch {

    //存储上一次正则
    public static List<Map<String,List<String>>> mRegularArray=new ArrayList<Map<String,List<String>>>();
    private static List<String> mRegularStr=new ArrayList<String>();
    private String oldRegular,nowRegular;
    /**
     * 根据输入的字符串返回搜索到的短信
     *
     * @param str
     * @param mData
     * @return
     */
    public static List<SmsModel> FindSms(String str, List<SmsModel> mData) {
        List<SmsModel> temp = new ArrayList<SmsModel>();
        for (SmsModel smsModel : mData) {
            if (smsModel.getName() == null) {
                continue;
            }
            if (smsModel.getName().contains(str)
                    || smsModel.getBody().contains(str)) {
                temp.add(smsModel);
            }
        }
        //去重
        for (int i = 0; i < temp.size(); i++)  //外循环是循环的次数
        {
            for (int j = temp.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
            {
                if (temp.get(i).getName().equals(temp.get(j).getName()))
                {
                    temp.remove(i);
                }

            }
        }
        Collections.reverse(temp);
        return temp;
    }

    /**
     * 根据输入的字符串搜索联系人
     *
     * @param str
     * @param mData
     * @param isNumber 是否根据数字去搜索
     * @return
     */
    public static List<ContactModel> FindPinyin(String str,
                                                List<ContactModel> mData, boolean isNumber) {
        /*
         * 判断是否是以字母来搜索
		 */
        if (str.charAt(0) >= 'a' && str.charAt(0) <= 'z'
                || str.charAt(0) >= 'A' && str.charAt(0) <= 'Z'||str.charAt(0)>='0'&& str.charAt(0)<='9'&&!isNumber) {
            return contains(str, mData);
        }

        List<ContactModel> temp = new ArrayList<ContactModel>();
            for (ContactModel contactModel : mData) {
                if(contactModel.getName().contains(str)){
                    temp.add(contactModel);
                }
            }

            return temp;
    }

    /**
     * 根据正则去检索
     * @param search
     * @param mData
     * @param isAddRegular 是否增加正则表达式
     * @return
     */
    public synchronized static List<ContactModel> FindRegularPinYin(String search,List<ContactModel> mData,boolean isAddRegular){
        //正则表达式
        String Regular="";
        Map<String,List<String>> mMapArray=new HashMap<String,List<String>>();
        List<ContactModel> mTemp=new ArrayList<ContactModel>();
        List<String> TempRglList=new ArrayList<String>();
        search=search.replace("*","A").replace("#","B").replace("+","C");
        String sub=search.substring(search.length()-1,search.length());
        String MapKey=search.substring(0,search.length()-1);

        if(isAddRegular){
        if(search.length()==1){
            Regular="-("+search+"\\d*)";
            TempRglList.add(Regular);
        }else if(search.length()>1){
           // TempRglList.clear();
             //取出所有表达式的值
           // for (int n = 0; n <mRegularArray.size() ; n++) {
             List<String> mStrRgl=mRegularArray.get(mRegularArray.size()-1).get(MapKey);
                    if(mStrRgl==null){
                        return mTemp;
                    }
                    for (int k = 0; k <mStrRgl.size() ; k++) {
                        /**
                         * 每一个表达式都可以分裂成两种新的表达式
                         */
                    /**把每个表达式分成字符串数组*/
                    String subones[]=mStrRgl.get(k).split("-");
                    String subone;
                    /**如果该字符串最后一个数组长度大于6*/
                    if(subones[subones.length-1].length()>6){
                        /**则要把该字符数组最后的//d*去掉*/
                        String tempsubone=subones[subones.length-1].replace("\\d*","");
                        tempsubone=mStrRgl.get(k).replace(subones[subones.length-1],tempsubone);
                        subone=tempsubone+"-("+sub+"\\d*)";
                    }else{
                        subone=mStrRgl.get(k)+"-("+sub+"\\d*)";
                    }
                        String subtwo=mStrRgl.get(k).substring(0, mStrRgl.get(k).lastIndexOf("\\"))+sub+"\\d*)";
                        TempRglList.add(subone);
                        TempRglList.add(subtwo);
                    }
                //    }
        }
        //减少表达式
        }else{
            //减少表达式只需要把上一次生成的表达式取出，减少运算量
            mRegularArray.remove(mRegularArray.size()-1);
            mMapArray=mRegularArray.get(mRegularArray.size()-1);
        }
        //isAddRegular为假则表示当前搜索字符是在减少的,就不用新添加表达式到mMapArray里
        if(isAddRegular){
            mMapArray.put(search,TempRglList);
        }else{
            //如果mMapArray数组是空的,说明当前没有正则表达式匹配,直接去匹配号码
            if(mMapArray.get(search).size()==0){
                for (int i = 0; i <mData.size() ; i++) {
                    if (mData.get(i).getTelnum().contains(search)) {
                        mData.get(i).group = search;
                        mTemp.add(mData.get(i));
                    }
                }
            }
        }

        //查找所有符合条件的名字
        for (int i = 0; i <mMapArray.get(search).size() ;) {
            //把当前所有的表达式取出
            String reguler=mMapArray.get(search).get(i);
            //标示表达式是否匹配到名字
            boolean isReguler=false;
            Pattern p = Pattern.compile(reguler,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            for (int k = 0; k <mData.size() ; k++) {
                for (int j = 0; j <mData.get(k).getPynameList().size() ; j++) {
                Matcher m = p.matcher(mData.get(k).getPynameList().get(j));
                //如果找到了就添加到临时数组里
                if (m.find()) {
                    mTemp.add(mData.get(k));
                    mData.get(k).group = m.group();
                    isReguler=true;
                } else {//如果没找到就按电话号码去查找
                    if (mData.get(k).getTelnum().contains(search)) {
                        mData.get(k).group = search;
                        mTemp.add(mData.get(k));
                    }

                }
            }
        }
            //如果该表达式在所有名字里都没有匹配到,则这个表达式就没有必要存在Map数组里
            if(!isReguler){
                mMapArray.get(search).remove(i);
                continue;
            }
            i++;
    }
        //如果isAddRegular为真则增加表达式
        if(isAddRegular){
            mRegularArray.add(mMapArray);
        }

        //去重
        for (int i = 0; i < mTemp.size(); i++)  //外循环是循环的次数
        {
            for (int j = mTemp.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
            {
                //名字相同并且号码也相同则去重
                if (mTemp.get(i).getName().equals(mTemp.get(j).getName())&&mTemp.get(i).getTelnum().equals(mTemp.get(j).getTelnum()))
                {
                    mTemp.remove(j);
                }

            }
        }
        return mTemp;
    }



    /**
     * 判断字符串里是否有中文
     *
     * @param s
     * @return
     */
    public static boolean containsChinese(String s) {
        if (null == s || "".equals(s.trim()))
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (isChinese(s.charAt(i)))
                return true;
        }
        return false;
    }

    public static boolean isChinese(char a) {
        int v = (int) a;
        return (v >= 19968 && v <= 171941);
    }

    /**
     * 根据字母去检索
     *
     * @param str
     * @param mData
     * @return
     */
    public static List<ContactModel> contains(String str,
                                              List<ContactModel> mData) {
        boolean flag;
        List<ContactModel> temp = new ArrayList<ContactModel>();
		/**
		 * 根据首字母去检索
		 */
        for (ContactModel model : mData) {
            Pattern pattern = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
            String TempName=BaseUtil.converterToSpell(model.getName());
            String[] TempNames=TempName.split(",");
            for (int i = 0; i <TempNames.length; i++) {
            Matcher matcher = pattern.matcher(TempNames[i].replaceAll(
                    "[a-z]", ""));
            flag = matcher.find();
            if (flag) {
                temp.add(model);
            } else {
				/**
				 * 根据全拼去检索
				 */
                Pattern pattern1 = Pattern.compile(str,
                        Pattern.CASE_INSENSITIVE);
                Matcher matcher1 = pattern1.matcher(TempNames[i].replace(
                        " ", ""));
                flag = matcher1.find();
                if (flag) {
                    /**
                     * 如果名字转拼音后检索出来的第一个字母不是大写字母 则不添加
                     *
                     * 如果名字本身是字母组合就不用判断首字母是否大写
                     */
                    if (matcher1.group().toString().charAt(0) >= 'A'
                            && matcher1.group().toString().charAt(0) <= 'Z') {
                        temp.add(model);
                    } else if (model.getName().charAt(0) >= 'A'
                            && model.getName().charAt(0) <= 'Z'
                            || model.getName().charAt(0) >= 'a'
                            && model.getName().charAt(0) <= 'z') {
                        temp.add(model);
                    }
                }

            }
        }
        }
        //去重
        for (int i = 0; i < temp.size(); i++)  //外循环是循环的次数
        {
            for (int j = temp.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
            {
                //名字相同并且号码也相同则去重
                if (temp.get(i).getName().equals(temp.get(j).getName())&&temp.get(i).getTelnum().equals(temp.get(j).getTelnum()))
                {
                    temp.remove(j);
                }

            }
        }
        return temp;

    }
}
