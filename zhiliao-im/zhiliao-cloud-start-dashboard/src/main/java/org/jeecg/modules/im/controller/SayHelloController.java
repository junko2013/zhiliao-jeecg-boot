package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.entity.query_helper.QSayHello;
import org.jeecg.modules.im.service.ISayHelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/sayHello")
public class SayHelloController extends BaseBackController<SayHello,ISayHelloService> {

    @RequestMapping("/pagination")
    public Result<Object> list(QSayHello q){
        q.setServerId(getServerId());
        return success(service.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
