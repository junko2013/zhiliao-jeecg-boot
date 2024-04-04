package org.jeecg.modules.im.base.tools;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;

public class SensitiveWordTool {
    public static void main(String[] args) {
        final String text = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前。";

        System.out.println(SensitiveWordHelper.contains(text));
    }
}
