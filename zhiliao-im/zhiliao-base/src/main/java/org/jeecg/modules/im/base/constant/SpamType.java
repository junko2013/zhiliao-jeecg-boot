package org.jeecg.modules.im.base.constant;

import lombok.Getter;

public enum SpamType {
    gg(0,"广告"),
    bl(1,"暴力"),
    sq(2,"色情"),
    nt(3,"虐童"),
    bq(4,"版权"),
    zz(5,"政治"),
    ys(6,"隐私"),
    mm(7,"脏话"),
    jy(8,"交易"),
    jc(9,"禁词"),
    qt(10,"其他")
    ;
    @Getter
    int code;
    @Getter
    String desc;
    SpamType(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

}
