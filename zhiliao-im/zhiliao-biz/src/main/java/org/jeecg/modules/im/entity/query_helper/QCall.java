package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Call;

@Data
public class QCall extends Call {
    private String sender;
    private String receiver;
}
