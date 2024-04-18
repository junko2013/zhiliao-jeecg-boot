package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.RedPackOpen;
import org.jeecg.modules.im.entity.query_helper.QRedPackOpen;
import org.jeecg.modules.im.service.IRedPackOpenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/redPackOpen")
public class RedPackOpenController extends BaseBackController<RedPackOpen, IRedPackOpenService> {

    @RequestMapping("/pagination")
    public Result<Object> pagination(QRedPackOpen q){
        return success(service.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }


}
