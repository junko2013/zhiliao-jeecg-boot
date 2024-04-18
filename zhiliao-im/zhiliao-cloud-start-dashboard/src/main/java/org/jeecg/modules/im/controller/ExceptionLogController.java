package org.jeecg.modules.im.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ExceptionLog;
import org.jeecg.modules.im.entity.query_helper.QExceptionLog;
import org.jeecg.modules.im.service.IExceptionLogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@Api(tags="系统异常日志")
@RestController
@RequestMapping("/im/exceptionLog")
@Slf4j
public class ExceptionLogController extends BaseBackController<ExceptionLog, IExceptionLogService> {

    /**
     * 分页列表查询
     *
     * @param exceptionLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "系统异常日志-分页列表查询")
    @ApiOperation(value="系统异常日志-分页列表查询", notes="系统异常日志-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ExceptionLog>> queryPageList(ExceptionLog exceptionLog,
                                                                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                                HttpServletRequest req) {
        QueryWrapper<ExceptionLog> queryWrapper = QueryGenerator.initQueryWrapper(exceptionLog, req.getParameterMap());
        Page<ExceptionLog> page = new Page<ExceptionLog>(pageNo, pageSize);
        IPage<ExceptionLog> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统异常日志-通过id删除")
    @ApiOperation(value="系统异常日志-通过id删除", notes="系统异常日志-通过id删除")
    @RequiresPermissions("exceptionLog:im_exception_log:delete")
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
    @AutoLog(value = "系统异常日志-批量删除")
    @ApiOperation(value="系统异常日志-批量删除", notes="系统异常日志-批量删除")
    @RequiresPermissions("exceptionLog:im_exception_log:deleteBatch")
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
    //@AutoLog(value = "系统异常日志-通过id查询")
    @ApiOperation(value="系统异常日志-通过id查询", notes="系统异常日志-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ExceptionLog> queryById(@RequestParam(name="id",required=true) String id) {
        ExceptionLog exceptionLog = service.getById(id);
        if(exceptionLog==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(exceptionLog);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param exceptionLog
     */
    @RequiresPermissions("exceptionLog:im_exception_log:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExceptionLog exceptionLog) {
        return super.exportXls(request, exceptionLog, ExceptionLog.class, "系统异常日志");
    }

}
