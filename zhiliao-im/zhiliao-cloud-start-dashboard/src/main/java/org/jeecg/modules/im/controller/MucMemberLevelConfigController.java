package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevelConfig;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelConfig;
import org.jeecg.modules.im.service.MucMemberLevelConfigService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 群组成员等级配置
 */
@RestController
@RequestMapping("/im/mucMemberLevel/config")
public class MucMemberLevelConfigController extends BaseBackController {
    @Resource
    private MucMemberLevelConfigService mucMemberLevelConfigService;

    @RequestMapping("/pagination")
    public Result<IPage<MucMemberLevelConfig>> queryPageList(MucMemberLevelConfig levelConfig, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<MucMemberLevelConfig>> result = new Result<>();
        QueryWrapper<MucMemberLevelConfig> q = QueryGenerator.initQueryWrapper(levelConfig, req.getParameterMap());
        Page<MucMemberLevelConfig> page = new Page<>(pageNo, pageSize);
        IPage<MucMemberLevelConfig> pageList = mucMemberLevelConfigService.page(page, q);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody MucMemberLevelConfig level){
        return mucMemberLevelConfigService.createOrUpdate(level);
    }


    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(mucMemberLevelConfigService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return mucMemberLevelConfigService.del(ids);
    }
}
