package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Adver;
import org.jeecg.modules.im.entity.FriendDelete;

@Data
public class QFriendDelete extends FriendDelete {
    private String fromUserSearch;
    private String toUserSearch;
}
