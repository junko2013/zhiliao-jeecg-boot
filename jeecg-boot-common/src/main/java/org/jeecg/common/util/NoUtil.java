package org.jeecg.common.util;

import java.util.Random;

public class NoUtil {
    //生成部署系统编号
    public static String generateSystemNo() {
        Random random = new Random();
        char firstChar = (char) ('A' + random.nextInt(26));
        StringBuilder restChars = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            if (random.nextBoolean()) {
                restChars.append((char) ('A' + random.nextInt(26)));
            } else {
                restChars.append(random.nextInt(10));
            }
        }
        return firstChar + restChars.toString();
    }
    //生成服务器编号
    private static String generateServerNo() {
        Random random = new Random();

        // 随机生成一个大写字母
        char letter = (char) ('A' + random.nextInt(26));

        // 随机生成一个数字
        char digit = (char) ('0' + random.nextInt(10));

        // 第三个字符，可以是大写字母或数字
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        char thirdChar = chars.charAt(random.nextInt(chars.length()));

        // 将三个字符打乱顺序后组合成字符串
        String result = "" + letter + digit + thirdChar;
        return shuffleString(result);
    }

    private static String shuffleString(String input) {
        char[] a = input.toCharArray();
        Random random = new Random();
        for (int i = 0; i < a.length; i++) {
            int j = random.nextInt(a.length);
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        return new String(a);
    }

    public static void main(String[] args) {
        System.out.println(generateServerNo());
    }
}
