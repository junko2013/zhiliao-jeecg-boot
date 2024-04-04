package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QCall;
import org.jeecg.modules.im.service.CallService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/call")
public class CallController extends BaseBackController {
    @Resource
    private CallService callService;

    @RequestMapping("/pagination")
    public Result<Object> list(QCall q) {
        return success(callService.pagination(new MyPage<>(getPage(), getPageSize()), q));
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return callService.del(ids);
    }

}
