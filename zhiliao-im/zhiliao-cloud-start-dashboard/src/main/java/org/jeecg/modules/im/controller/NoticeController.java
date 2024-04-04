package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.query_helper.QNotice;
import org.jeecg.modules.im.service.NoticeService;
import org.jeecg.modules.im.service.NoticeService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 系统公告
 */
@RestController
@RequestMapping("/im/notice")
public class NoticeController extends BaseBackController {
    @Resource
    private NoticeService noticeService;

    @RequestMapping("/pagination")
    public Result<IPage<Notice>> queryPageList(Notice notice, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<Notice>> result = new Result<>();
        QueryWrapper<Notice> queryWrapper = QueryGenerator.initQueryWrapper(notice, req.getParameterMap());
        Page<Notice> page = new Page<>(pageNo, pageSize);
        IPage<Notice> pageList = noticeService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody Notice notice){
        if(notice.getServerId()==null){
            notice.setServerId(getServer().getId());
        }
        return noticeService.createOrUpdate(notice);
    }


    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(noticeService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return noticeService.del(ids);
    }
    /**
     * 获取被逻辑删除的公告列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Notice> logicDeletedUserList = noticeService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的公告
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            noticeService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除公告
     *
     * @param ids 被删除的公告ID，多个id用半角逗号分割
     * @return
     */
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            noticeService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
