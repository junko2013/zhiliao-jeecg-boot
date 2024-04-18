package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SecretAnswer;
import org.jeecg.modules.im.entity.query_helper.QSecretAnswer;
import org.jeecg.modules.im.service.ISecretAnswerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 密保答案
 */
@RestController
@RequestMapping("/im/secretAnswer")
public class SecretAnswerController extends BaseBackController<SecretAnswer, ISecretAnswerService> {

    @RequestMapping("/pagination")
    public Result<Object> list(QSecretAnswer q){
        return success(service.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

}
