package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Post;

@Data
public class QPost extends Post {
    private String userSearch;
    private Boolean isDelete;
}
