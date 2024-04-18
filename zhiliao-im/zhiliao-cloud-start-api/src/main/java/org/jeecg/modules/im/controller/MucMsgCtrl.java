package org.jeecg.modules.im.controller;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucMsg;
import org.jeecg.modules.im.entity.query_helper.QMucMsg;
import org.jeecg.modules.im.service.IMucMsgReadService;
import org.jeecg.modules.im.service.IMucMsgService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 群聊消息
 */
@RestController
@RequestMapping("/a/mucMsg")
public class MucMsgCtrl extends BaseApiCtrl {
    @Resource
    private IMucMsgService IMucMsgService;
    @Resource
    private IMucMsgReadService readService;

    /**
     * 分页查询
     */
    @GetMapping("/pagination")
    public Result<Object> page(QMucMsg q){
        q.setUserId(getCurrentUserId());
        if(q.getAfter()){
            q.setPageSize(Integer.MAX_VALUE);
        }
        List<MucMsg> datas;
        if(StringUtils.isNotBlank(q.getMucIds())){
            datas = new ArrayList();
            for (String id : StringUtils.split(q.getMucIds(), ";")) {
                q.setMucId(Integer.parseInt(id));
                datas.addAll(IMucMsgService.pageApi(q));
            }
        }else{
            datas = IMucMsgService.pageApi(q);
        }
        for (MucMsg data : datas) {
            data.setReads(readService.listByMsgId(data.getId()));
        }
        return success(datas);
    }

    //置顶
    @PostMapping("/pin")
    public Result<Object> pin(@RequestParam String msgId){
        return success();
    }

    /**
     * 删除 将当前用户的消息可见时间改为当前时间
     * @param msgId
     * @return
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String msgId){
        return success();
    }

}
