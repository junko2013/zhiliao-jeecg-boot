package org.jeecg.modules.im.base.constant;

import lombok.Getter;

public enum ErrorCode {
    //系统
    sys_maintain(0x0001,"系统维护中"),
    sys_error(0x0002,"服务错误"),
    sys_biz_error(0x0003,"业务错误"),
    sys_param_error(0x000,"参数错误"),
    //用户
    account_does_not_exists(0x1001, "账号不存在"),
    account_locked(0x1002, "账号已被冻结"),
    account_or_pwd_error(0x1003, "账号或密码错误"),
    //订单
    //交易
    //充值
    //提现
    //订单
    //会员级别

    ;
    @Getter
    int code;
    @Getter
    String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}