package org.jeecg.common.util.encryption;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description: AES 加密
 * @author: jeecg-boot
 * @date: 2022/3/30 11:48
 */
@Slf4j
public class AesEncryptUtil {

    /**
     * 使用AES-128-CBC加密模式 key和iv可以相同
     */
    public static  String KEY = "tmnsy5P1lnLOTHDOkXZNHQ==";

    public static  String IV  = "gTjkr9BMFjikBTZB";

    /**
     * 加密方法
     * @param data  要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key, String iv) {
        try {

            //"算法/模式/补码方式"NoPadding PkcsPadding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encrypted);

        } catch (Exception e) {
            log.error("数据加密异常：",e);
            return data;
        }
    }

    /**
     * 解密方法
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static String desEncrypt(String data, String key, String iv) throws Exception {
        byte[] encrypted1 = Base64.decode(data);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] original = cipher.doFinal(encrypted1);

        return new String(original);
    }

    /**
     * 使用默认的key和iv加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) {
        return encrypt(data, KEY, IV);
    }

    /**
     * 使用默认的key和iv解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String data) throws Exception {
        return desEncrypt(data, KEY, IV);
    }



    /**
     * 测试
     */
    public static void main(String args[]) throws Exception {
//        String data = "/CL5z7PBb6Jd7lhFRrGHgw==";
//        System.out.println("解密："+desEncrypt(data));
//        System.out.println(encrypt("e9ca23d68d884d4ebb19d07889727dae"));
        System.out.println(desEncrypt("pbEdxoDwlHC6t32hCH1hYDVgMmtuBifNZW2EW+KbM2iOutEGVLyXnV7SgFM++MAkY7fjJm3TqH2EDDDBphWumKAmNDnnWVInEcZtx/B0xwY="));
    }

}
