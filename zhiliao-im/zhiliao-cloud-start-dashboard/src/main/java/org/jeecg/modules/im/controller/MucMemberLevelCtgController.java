package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelCtg;
import org.jeecg.modules.im.service.MucMemberLevelCtgService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * 群组成员等级分类
 */
@RestController
@RequestMapping("/im/mucMemberLevel/ctg")
public class MucMemberLevelCtgController extends BaseBackController {
    @Resource
    private MucMemberLevelCtgService mucMemberLevelCtgService;

    @RequestMapping("/pagination")
    public Result<IPage<MucMemberLevelCtg>> queryPageList(MucMemberLevelCtg ctg, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<MucMemberLevelCtg>> result = new Result<>();
        QueryWrapper<MucMemberLevelCtg> queryWrapper = QueryGenerator.initQueryWrapper(ctg, req.getParameterMap());
        Page<MucMemberLevelCtg> page = new Page<>(pageNo, pageSize);
        IPage<MucMemberLevelCtg> pageList = mucMemberLevelCtgService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody MucMemberLevelCtg ctg){
        return mucMemberLevelCtgService.createOrUpdate(ctg);
    }


    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(mucMemberLevelCtgService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return mucMemberLevelCtgService.del(ids);
    }
}
