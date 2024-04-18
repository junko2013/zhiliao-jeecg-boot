package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.service.INoticeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 系统公告
 */
@RestController
@RequestMapping("/im/notice")
public class NoticeController extends BaseBackController<Notice, INoticeService> {

    /**
     * 分页列表查询
     *
     * @param notice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "系统公告-分页列表查询")
    @ApiOperation(value="系统公告-分页列表查询", notes="系统公告-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Notice>> queryPageList(Notice notice,
                                                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                    HttpServletRequest req) {
        QueryWrapper<Notice> queryWrapper = QueryGenerator.initQueryWrapper(notice, req.getParameterMap());
        Page<Notice> page = new Page<Notice>(pageNo, pageSize);
        IPage<Notice> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param notice
     * @return
     */
    @AutoLog(value = "系统公告-添加")
    @ApiOperation(value="系统公告-添加", notes="系统公告-添加")
    @RequiresPermissions("notice:im_notice:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Notice notice) {
        service.save(notice);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param notice
     * @return
     */
    @AutoLog(value = "系统公告-编辑")
    @ApiOperation(value="系统公告-编辑", notes="系统公告-编辑")
    @RequiresPermissions("notice:im_notice:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Notice notice) {
        service.updateById(notice);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统公告-通过id删除")
    @ApiOperation(value="系统公告-通过id删除", notes="系统公告-通过id删除")
    @RequiresPermissions("notice:im_notice:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        service.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "系统公告-批量删除")
    @ApiOperation(value="系统公告-批量删除", notes="系统公告-批量删除")
    @RequiresPermissions("notice:im_notice:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "系统公告-通过id查询")
    @ApiOperation(value="系统公告-通过id查询", notes="系统公告-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Notice> queryById(@RequestParam(name="id",required=true) String id) {
        Notice notice = service.getById(id);
        if(notice==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(notice);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param notice
     */
    @RequiresPermissions("notice:im_notice:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Notice notice) {
        return super.exportXls(request, notice, Notice.class, "系统公告");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("notice:im_notice:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Notice.class);
    }

    /**
     * 获取被逻辑删除的公告列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Notice> logicDeletedUserList = service.queryLogicDeleted();
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
            service.revertLogicDeleted(Arrays.asList(ids.split(",")));
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
            service.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
