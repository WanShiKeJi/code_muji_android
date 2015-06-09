package com.src.playtime.thumb.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import android.app.ActivityManager;
import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtil {

	public final static String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE, Phone.CONTACT_ID };

	public static String STRS[] = { "0", "1", "[abc2]", "[def3]", "[ghi4]", "[jkl5]",
			"[mno6]", "[pqrs7]", "[tuv8]", "[wxyz9]" };

	/**
	 * 根据汉字拼音转化成数字
	 * 
	 * @param pinyin
	 *            输入的拼音
	 * @return 返回一个转化好了的数字字符串
	 */
	public static List<String> getPinYinNum(String pinyin) {
        List<String> mPynameList=new ArrayList<String>();
        String pyname=BaseUtil.converterToSpell(pinyin);
        String pynameArray[]=pyname.split(",");
        String TempPynameSum="";
        for (int i = 0; i <pynameArray.length ; i++) {
            String temp =pynameArray[i];
            TempPynameSum+=LetterToNumber(temp);
            if(!TempPynameSum.equals("")){
                if(TempPynameSum.charAt(0)!='-'){
                    TempPynameSum="-"+TempPynameSum;
                    mPynameList.add(TempPynameSum);
                }else{
                    mPynameList.add(TempPynameSum);
                }
            }
            TempPynameSum="";
        }
		return mPynameList;
	}

    /**
     * 字母转化为数字
     * @param str
     * @return
     */
    public static String LetterToNumber(String str){
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    sb.append("-"+str.charAt(i));
                    break;
                case 'A':
                case 'B':
                case 'C':
                    sb.append("-2");
                    break;
                case 'D':
                case 'E':
                case 'F':
                    sb.append("-3");
                    break;
                case 'G':
                case 'H':
                case 'I':
                    sb.append("-4");
                    break;
                case 'J':
                case 'K':
                case 'L':
                    sb.append("-5");
                    break;
                case 'M':
                case 'N':
                case 'O':
                    sb.append("-6");
                    break;
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                    sb.append("-7");
                    break;
                case 'T':
                case 'U':
                case 'V':
                    sb.append("-8");
                    break;
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                    sb.append("-9");
                    break;
                case 'a':
                case 'b':
                case 'c':
                    sb.append("2");
                    break;
                case 'd':
                case 'e':
                case 'f':
                    sb.append("3");
                    break;
                case 'g':
                case 'h':
                case 'i':
                    sb.append("4");
                    break;
                case 'j':
                case 'k':
                case 'l':
                    sb.append("5");
                    break;
                case 'm':
                case 'n':
                case 'o':
                    sb.append("6");
                    break;
                case 'p':
                case 'q':
                case 'r':
                case 's':
                    sb.append("7");
                    break;
                case 't':
                case 'u':
                case 'v':
                    sb.append("8");
                    break;
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    sb.append("9");
                    break;
                case '*':
                    sb.append("-A");
                    break;
                case '#':
                    sb.append("-B");
                    break;
                case '+':
                    sb.append("-C");
                    break;
                default:
                    sb.append("0");
                    break;

            }
        }

        return sb.toString();
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字，生成方式如（长沙市长:cssc,zssz,zssc,cssz）
     *
     * @param chines
     *            汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            // 取首字母
                            pinyinName.append(strs[j].charAt(0));
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                    // else {
                    // pinyinName.append(nameChar[i]);
                    // }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     * 汉字转换位汉语全拼，英文字符不变，特殊字符丢失
     * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen
     * ,chongdangshen,zhongdangshen,chongdangcan）
     *
     * @param chines
     *            汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            pinyinName.append(strs[j].replaceFirst(strs[j].substring(0, 1),
                                    strs[j].substring(0, 1).toUpperCase()));
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     * 去除多音字重复数据
     *
     * @param theStr
     * @return
     */
    private static List<Map<String, Integer>> discountTheChinese(String theStr) {
        // 去除重复拼音后的拼音列表
        List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();
        // 用于处理每个字的多音字，去掉重复
        Map<String, Integer> onlyOne = null;
        String[] firsts = theStr.split(" ");
        // 读出每个汉字的拼音
        for (String str : firsts) {
            onlyOne = new Hashtable<String, Integer>();
            String[] china = str.split(",");
            // 多音字处理
            for (String s : china) {
                Integer count = onlyOne.get(s);
                if (count == null) {
                    onlyOne.put(s, new Integer(1));
                } else {
                    onlyOne.remove(s);
                    count++;
                    onlyOne.put(s, count);
                }
            }
            mapList.add(onlyOne);
        }
        return mapList;
    }

    /**
     * 解析并组合拼音，对象合并方案(推荐使用)
     *
     * @return
     */
    private static String parseTheChineseByObject(
            List<Map<String, Integer>> list) {
        Map<String, Integer> first = null; // 用于统计每一次,集合组合数据
        // 遍历每一组集合
        for (int i = 0; i < list.size(); i++) {
            // 每一组集合与上一次组合的Map
            Map<String, Integer> temp = new Hashtable<String, Integer>();
            // 第一次循环，first为空
            if (first != null) {
                // 取出上次组合与此次集合的字符，并保存
                for (String s : first.keySet()) {
                    for (String s1 : list.get(i).keySet()) {
                        String str = s + s1;
                        temp.put(str, 1);
                    }
                }
                // 清理上一次组合数据
                if (temp != null && temp.size() > 0) {
                    first.clear();
                }
            } else {
                for (String s : list.get(i).keySet()) {
                    String str = s;
                    temp.put(str, 1);
                }
            }
            // 保存组合数据以便下次循环使用
            if (temp != null && temp.size() > 0) {
                first = temp;
            }
        }
        String returnStr = "";
        if (first != null) {
            // 遍历取出组合字符串
            for (String str : first.keySet()) {
                returnStr += (str + ",");
            }
        }
        if (returnStr.length() > 0) {
            returnStr = returnStr.substring(0, returnStr.length() - 1);
        }
        return returnStr;
    }

    /**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
		if (TextUtils.isEmpty(inputString)) {
            return "";
        }
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);
					if (temp == null || TextUtils.isEmpty(temp[0])) {
						continue;
					}
					output += temp[0].replaceFirst(temp[0].substring(0, 1),
							temp[0].substring(0, 1).toUpperCase());
				} else
					output += java.lang.Character.toString(input[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public static boolean isInLauncher(Context context, String ClsName) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (int i = 0; i < manager.getRunningTasks(1).size(); i++) {
			String name = manager.getRunningTasks(1).get(i).topActivity
					.getClassName();
			Log.e("name------>", name + "ClsName----->" + ClsName);
			if (name.equals(ClsName)) {
				return true;
			}
		}
		return false;
	}

    /**
     * 返回转化好的字符串时间
     *
     * @param cc_time
     * @return
     */
    public static String getStrTime(String cc_time) {
        if(cc_time.equals("")){
            return "";
        }
        String re_StrTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd/  HH:mm");
        cc_time = cc_time.substring(0, 10);
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 将字符串时间转化为date
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        dateString = dateString.substring(0, 10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd/  HH:mm");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    /**
     * 返回号码的运营商
     * @return
     */
    public static String getOperator(String tel){
        if(tel.length()<7){
            return "";
        }
        tel=tel.replace("+86","").substring(0,7).trim();
        if(tel.matches("^1(34[0-8]|(3[5-9]|47|5[0-2]|57[124]|5[89]|8[2378])\\d)\\d{3}$")){
            return "移动";
        }else if(tel.matches("^1(3[0-2]|45|5[56]|8[56])\\d{4}$")){
            return "联通";
        }else if(tel.matches("^1(33|53|8[09])\\d{4}$")){
            return "电信";
        }
        return "";
    }
}
