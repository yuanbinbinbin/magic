package com.yb.magicplayer.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanZiToPinYin {
	
	private static HanyuPinyinOutputFormat  pinyinOutputFormat = new HanyuPinyinOutputFormat();
	
	public static char getFirstChar(String src)
	{
		pinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);//小写拼音字母
		pinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不加语调标志
		pinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);//u的生母替为v
		
		if(src.length() > 0)
		{
			char ch = src.charAt(0);
			if(ch >='a'&&ch<='z')
				return (char)(ch-'a'+'A');
			if(ch>='A'&&ch<='Z')
				return ch;
			try {
				String[] arr = PinyinHelper.toHanyuPinyinStringArray(ch, pinyinOutputFormat);
				if(arr != null &&arr[0].charAt(0)>='A'&&arr[0].charAt(0)<='Z')
					return arr[0].charAt(0);
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return '#';
	}
}


