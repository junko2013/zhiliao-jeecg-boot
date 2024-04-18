package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SignIn;
import org.jeecg.modules.im.entity.query_helper.QSignIn;
import org.jeecg.modules.im.service.ISignInService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/signIn")
public class SignInController extends BaseBackController<SignIn, ISignInService> {

    @RequestMapping("/pagination")
    public Result<Object> adminLoginLog(QSignIn q){
        q.setServerId(getServerId());
        return success(service.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

}
