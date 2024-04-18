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
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.UserLog;
import org.jeecg.modules.im.service.IUserLogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 用户操作日志
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Api(tags="用户操作日志")
@RestController
@RequestMapping("/im/userLog")
@Slf4j
public class UserLogController extends BaseBackController<UserLog, IUserLogService> {

    /**
     * 分页列表查询
     *
     * @param userLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "用户操作日志-分页列表查询")
    @ApiOperation(value="用户操作日志-分页列表查询", notes="用户操作日志-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<UserLog>> queryPageList(UserLog userLog,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        QueryWrapper<UserLog> queryWrapper = QueryGenerator.initQueryWrapper(userLog, req.getParameterMap());
        Page<UserLog> page = new Page<UserLog>(pageNo, pageSize);
        IPage<UserLog> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户操作日志-通过id删除")
    @ApiOperation(value="用户操作日志-通过id删除", notes="用户操作日志-通过id删除")
    @RequiresPermissions("userLog:im_user_log:delete")
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
    @AutoLog(value = "用户操作日志-批量删除")
    @ApiOperation(value="用户操作日志-批量删除", notes="用户操作日志-批量删除")
    @RequiresPermissions("userLog:im_user_log:deleteBatch")
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
    //@AutoLog(value = "用户操作日志-通过id查询")
    @ApiOperation(value="用户操作日志-通过id查询", notes="用户操作日志-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<UserLog> queryById(@RequestParam(name="id",required=true) String id) {
        UserLog userLog = service.getById(id);
        if(userLog==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(userLog);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param userLog
     */
    @RequiresPermissions("userLog:im_user_log:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, UserLog userLog) {
        return super.exportXls(request, userLog, UserLog.class, "用户操作日志");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("userLog:im_user_log:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, UserLog.class);
    }

}
