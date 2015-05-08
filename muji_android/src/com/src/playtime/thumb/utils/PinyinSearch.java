package com.src.playtime.thumb.utils;

import java.util.ArrayList;
import java.util.List;
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
                || str.charAt(0) >= 'A' && str.charAt(0) <= 'Z') {
            return contains(str, mData);
        }

        List<ContactModel> temp = new ArrayList<ContactModel>();
        /**
         * 如果是以0,1,+开头就以数字检索
         */
        if (str.toString().startsWith("0") || str.toString().startsWith("1")
                || str.toString().startsWith("+")) {
            if (isNumber) {
                for (ContactModel model : mData) {
                    if (model.getTelnum().contains(str)
                            || model.getName().contains(str)) {
                        model.group = str;
                        temp.add(model);
                    }
                }
                return temp;
            }
        }
        /**
         * 判断字符里是否有汉字
         */
        if (containsChinese(str)) {
            for (ContactModel contactModel : mData) {
                Pattern pattern = Pattern
                        .compile(str, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(contactModel.getName());
                if (matcher.find()) {
                    temp.add(contactModel);
                }
            }
            return temp;
        }
        if (isNumber) {
            StringBuffer sb = new StringBuffer();
            // 获取每一个数字对应的字母列表并以'-'隔开
            for (int i = 0; i < str.length(); i++) {
                sb.append((str.charAt(i) <= '9' && str.charAt(i) >= '0') ? BaseUtil.STRS[str
                        .charAt(i) - '0'] : str.charAt(i));
                if (i != str.length() - 1) {
                    sb.append("-");
                }
            }

            // 循环遍历根据拼音去检索
            for (ContactModel model : mData) {
                if (contains(sb.toString(), model, str)) {
                    temp.add(model);
                } else if (model.getTelnum().contains(str)) {
                    model.group = str;
                    temp.add(model);
                }
            }
            return temp;
        }
        return temp;
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
		/*
		 * 根据首字母去检索
		 */
        for (ContactModel model : mData) {
            Pattern pattern = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(model.getPyname().replaceAll(
                    "[a-z]", ""));
            flag = matcher.find();
            if (flag) {
                temp.add(model);
            } else {
				/*
				 * 根据全拼去检索
				 */
                Pattern pattern1 = Pattern.compile(str,
                        Pattern.CASE_INSENSITIVE);
                Matcher matcher1 = pattern1.matcher(model.getPyname().replace(
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
        return temp;

    }

    /**
     * 根据拼音搜索
     *
     * @param str    正则表达式
     * @return
     */
    public static boolean contains(String str, ContactModel model, String search) {
        if (TextUtils.isEmpty(model.getPyname())) {
            return false;
        }
        model.group = "";
        //把字符串里的*号前面全部加上\\，否则匹配不了
        str=str.replaceAll("\\*","\\\\\\*");
        String Pyname="";
        //把名字的首字母抽出(例如ee王二(eewe) 阿洁(aj))
        for (int i = 0; i <model.getName().length() ; i++) {
            if(PinyinSearch.isChinese(model.getName().charAt(i))){
                Pyname+=BaseUtil.getPingYin(model.getName().charAt(i)+"").charAt(0);
            }else{
                Pyname+=model.getName().charAt(i);
            }
        }
        // 搜索条件大于6个字符将不按拼音首字母查询
        if (search.length() < 12) {
            /**
             * 根据首字母进行模糊查询（把对应的按键字母格式转化为[adc]） 例如：2-->[adc] 3-->[def]
             */
//            Pattern pattern = Pattern.compile("^"
//                    + str.replaceAll("\\*","\\\\\\\\\\*").toUpperCase().replace("-", "[*+#a-z]*"));
            Pattern pattern = Pattern.compile(str.replace("-","").toUpperCase(),Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(Pyname);
            if (matcher.find()) {
                //String tempStr = matcher.group();
//                for (int i = 0; i < tempStr.length(); i++) {
//                    if (tempStr.charAt(i) >= 'A' && tempStr.charAt(i) <= 'Z'||tempStr.charAt(i)== '*'||tempStr.charAt(i)== '#') {
//                        model.group += tempStr.charAt(i);
//                    }
//                }
                model.group=matcher.group();
                return true;
            }

        }
       // String s="";
         String s = searchPinYin(str, model.getPyname(), model);
        /**
         * 如果返回的有值则添加
         *
         */
      // return searchPinYin(str, model.getPyname(), model);
        if (!s.equals(""))
            return true;
        else
            return false;
    }

    /**
     * 根据全拼搜索
     *
     * @param str
     * @param pyname
     * @param model
     * @return
     */
    public static String searchPinYin(String str, String pyname,
                                      ContactModel model) {
        // 把对应的数字的字母分成数组
        String s[] = str.replace("-", " ").split(" ");
        // 把第一组的字母转化成大写（为了更好的匹配第一个字母是否大写）
        s[0] = s[0].toUpperCase();
        String comstr = "";
        StringBuffer sb = new StringBuffer();
        int idx = -1;
        for (int i = 0; i < s.length; i++) {
            comstr += s[i];
        }
        // 匹配每个汉字的首字母
        Pattern pattern;
        Matcher matcher;
        //
        if (model.getName().charAt(0) >= 'a'
                && model.getName().charAt(0) <= 'z'
                || model.getName().charAt(0) >= 'A'
                && model.getName().charAt(0) <= 'Z') {
            pattern = Pattern.compile(str.replace("-", ""),
                    Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(model.getPyname().replaceAll(" ", ""));
        } else {
            //匹配汉字的首字母
            Log.e("str=====>",str);
           // String b=str.replace("-","");
           // Log.e("b=====>",b);
            //
            pattern = Pattern.compile(str.replace("-", "").toUpperCase());
            //
            matcher = pattern.matcher(pyname.replaceAll("[a-z]", ""));
        }
        boolean flag = matcher.find();
        if (flag) {
            model.group += matcher.group();
            return model.group;
            // 如果数组的长度大于1则表明输入的数字为一个以上 这个时候就要去匹配全拼（因为flag为假说明首字母没匹配到）
        } else if (s.length > 1) {
            comstr = "";
            // 除了第一个数组以外（第一个数组要匹配大写字母），其它数组的字母都变成小写
            for (int i = 1; i < s.length; i++) {
                s[i] = s[i].toLowerCase();
            }
            // 在每个数组中间添加[*+#a-z]*以匹配中间断开的情况（如李旭婷l(i)xut）
            for (int j = 0; j < s.length; j++) {
                if (j > 0)
                    comstr += "[*+#a-z]*" + s[j];
                else
                    comstr += s[j];
            }
            // Pattern.CASE_INSENSITIVE 不区分大小写匹配
            Pattern pattern1 = Pattern
                    .compile(comstr, Pattern.CASE_INSENSITIVE);
            Matcher matcher1 = pattern1.matcher(pyname);
            flag = matcher1.find();
            if (flag) {
                model.group += matcher1.group();
                // 如果检索出来的第一个字符不是大写的就返回出去
                if (!(model.group.charAt(0) >= 'A' && model.group.charAt(0) <= 'Z')) {
                    model.group = "";
                    return model.group;
                } else {
                    // 拿字符串数组里的元素逐个去检索
                    for (int i = 0; i < s.length; i++) {
                        Pattern pattern2 = Pattern.compile(s[i],
                                Pattern.CASE_INSENSITIVE);
                        Matcher matcher2 = pattern2.matcher(model.group);
                        // 如果idx等于-1则从0开始 如果idx不等于-1就说明之前已经找到匹配字母并记录了下标
                        flag = matcher2.find(idx == -1 ? idx + 1 : idx + 1);
                        // 如果idx等于-1就把匹配到的字母下标给idx
                        if (idx == -1) {
                            idx = matcher2.start();
                        }
                        if (flag) {
                            String d = matcher2.group();
                            // 新匹配到的字符和前一个匹配到的字符拼凑的新字符
                            String tempstr = "";
                            // 如果新匹配到的字符和前一个字符是相邻的就添加进字符串里
                            if (matcher2.start() - idx == 1
                                    || matcher2.start() - idx == 0) {
                                sb.append(d);
                                // 如果新匹配到的字符是大写则要根据前一个匹配到的字符是大写还是小写做判断
                            } else if (d.charAt(0) >= 'A' && d.charAt(0) <= 'Z') {
                                // 截取出前一个匹配到的字符
                                String tempa = sb.substring(sb.length() - 1,
                                        sb.length());
                                // 判断匹配到的前一个字符是否为小写
                                if (sb.length() >= 1 && tempa.charAt(0) >= 'a'
                                        && tempa.charAt(0) <= 'z') {
                                    // 是小写就加进临时字符串
                                    tempstr = tempa + d;
                                    // 把字符串带进group里作比较
                                    if (model.group.contains(tempstr))
                                        // 如果临时字符串里的字符能匹配到group里就添加
                                        sb.append(d);
                                    else
                                        // 否则就是没有匹配到相同的字符串返回空字符串
                                        return model.group = "";
                                } else
                                    // 如果是大写就直接添加
                                    sb.append(d);
                            } else {
                                return model.group = "";
                            }
                            idx = matcher2.start();
                        }
                    }

                    return model.group = sb.toString();
                }

            }
            return model.group;
        }

        return model.group;
    }
}
