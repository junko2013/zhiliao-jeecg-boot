package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Feedback;

@Data
public class QFeedback extends Feedback {
    private String userSearch;
}
