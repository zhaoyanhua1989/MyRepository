package com.example.test.model;

import java.util.ArrayList;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.annotation.SuppressLint;

import com.example.test.util.HanziToPinyinUtil;

/**
 * 汉语转拼音处理业务类
 * 
 * @author HKW2962
 *
 */
public class LanguageConventService {

	/********************** HanziToPinyinUtil工具类获取拼音 ************************/
	@SuppressLint("DefaultLocale")
	public static String getPinYin(String input) {
		ArrayList<HanziToPinyinUtil.Token> tokens = HanziToPinyinUtil.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (HanziToPinyinUtil.Token token : tokens) {
				if (HanziToPinyinUtil.Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		} else {
			sb.append("%");
		}
		return sb.toString().toUpperCase();
	}

	/********************** pinyin4j-2.5.0.jar获取拼音 ************************/
	/**
	 * 获取汉字串拼音首字母，英文及特殊字符字符不变
	 * 
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音首字母
	 */
	@SuppressLint("DefaultLocale")
	public static String cn2FirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().toLowerCase().trim();
	}

	/**
	 * 获取汉字串拼音，英文字符不变
	 * 
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音
	 */
	public static String cn2Spell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}

}
