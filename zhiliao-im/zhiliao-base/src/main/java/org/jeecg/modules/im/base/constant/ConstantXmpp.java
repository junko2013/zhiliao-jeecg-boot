package org.jeecg.modules.im.base.constant;

import lombok.Getter;

/**
 * xmpp常量
 */
public interface ConstantXmpp {
    //系统账号取值范围
    int sysAccountBegin = 1;
    int sysAccountEnd = 999;
    //业务账号取值范围
    int businessAccountBegin = 1000;
    int businessAccountEnd = 9999;
    //用户账号取值范围
    int userAccountBegin = 10000;
    int userAccountEnd = 9999999;
    //群聊账号取值范围
    int mucAccountBegin = 10000000;
    int mucAccountEnd = 999999999;

    //系统账号 有昵称，用于区分不同场景 用于不同场景消息通知
    enum SystemNo{
        system(100,"系统管理员",true),
        pay(104,"支付通知",true),
        notice(105,"系统通知",true),
        collection(106,"收藏夹",false),
        ;
        @Getter
        Integer account;
        @Getter
        boolean isPublic;
        @Getter
        String name;

        SystemNo(Integer account, String name,boolean isPublic) {
            this.account = account;
            this.name = name;
            this.isPublic = isPublic;
        }

    }
}
