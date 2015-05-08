package com.src.playtime.thumb.utils;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.util.Log;

import com.src.playtime.thumb.bean.ContactModel;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 汉语拼音比较器
 * 
 * @author wanfei
 *
 */
public class PinyinComparator implements Comparator {

	public int compare(Object value1, Object value2) {
		//
		char s1 = ((ContactModel) value1).getPyname().toLowerCase().charAt(0);
		char s2 = ((ContactModel) value2).getPyname().toLowerCase().charAt(0);
		if (s1 - s2 > 0) {
			// 1表示s1比s2大
			return 1;
		} else if (s1 - s2 < 0) {
			// -1表示s2比s1大
			return -1;
		} else {
			// 0表示相同
			return 0;
		}
	}

	// }
	// else if (value1.getClass().getName().equals("java.lang.String")) {
	// String s1 = value1.toString();
	// String s2 = value2.toString();
	// return Collator.getInstance(Locale.CHINESE).compare(s1, s2);
	// }
	// return 0; // 0表示相同。

	public static List sort(List strList) {
		PinyinComparator comp = new PinyinComparator();
		Collections.sort(strList, comp);
		return strList; // 返回排序后的列表
	}

}
