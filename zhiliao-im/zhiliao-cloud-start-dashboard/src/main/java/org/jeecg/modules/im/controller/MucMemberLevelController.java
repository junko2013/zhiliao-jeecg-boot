package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevel;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevel;
import org.jeecg.modules.im.service.MucMemberLevelService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 群组成员等级
 */
@RestController
@RequestMapping("/im/mucMemberLevel")
public class MucMemberLevelController extends BaseBackController {
    @Resource
    private MucMemberLevelService mucMemberLevelService;

    @RequestMapping("/pagination")
    public Result<IPage<MucMemberLevel>> queryPageList(MucMemberLevel level, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<MucMemberLevel>> result = new Result<>();
        QueryWrapper<MucMemberLevel> q = QueryGenerator.initQueryWrapper(level, req.getParameterMap());
        Page<MucMemberLevel> page = new Page<>(pageNo, pageSize);
        IPage<MucMemberLevel> pageList = mucMemberLevelService.page(page, q);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody MucMemberLevel level){
        return mucMemberLevelService.createOrUpdate(level);
    }


    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(mucMemberLevelService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return mucMemberLevelService.del(ids);
    }
}
