package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Bill;

@Data
public class QChatBg extends Bill {
    private String userSearch;
    private Boolean isDelete;
}
