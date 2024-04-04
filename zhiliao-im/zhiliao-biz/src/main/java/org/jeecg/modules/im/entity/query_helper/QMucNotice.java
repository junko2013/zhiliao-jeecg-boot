package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Adver;
import org.jeecg.modules.im.entity.MucNotice;

@Data
public class QMucNotice extends MucNotice {
    private Boolean isPin;
    private Integer userId;
}
