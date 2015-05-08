package com.src.playtime.thumb.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import android.app.ActivityManager;
import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	public static String getPinYinNum(String pinyin) {
		String s;
		pinyin = getPingYin(pinyin);
		StringBuffer sb = new StringBuffer();
		String s1 = pinyin.replaceAll("[A-Z]", "-$0").replaceFirst("-", "");// 正则替换
		s1 = s1.toLowerCase();
		for (int i = 0; i < s1.length(); i++) {
			switch (s1.charAt(i)) {
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
			case '-':
				sb.append("-");
				break;
			default:
				break;

			}
		}
		return sb.toString();
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
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd/  HH:mm");
        cc_time = cc_time.substring(0, 10);
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }
}
