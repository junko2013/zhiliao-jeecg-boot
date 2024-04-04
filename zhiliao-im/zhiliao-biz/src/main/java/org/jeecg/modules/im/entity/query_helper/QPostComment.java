package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.PostComment;

@Data
public class QPostComment extends PostComment {
    private String userSearch;
    private Boolean isDelete;
}
