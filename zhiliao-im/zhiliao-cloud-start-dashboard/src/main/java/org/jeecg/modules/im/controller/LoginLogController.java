package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.LoginLog;
import org.jeecg.modules.im.entity.query_helper.QLoginLog;
import org.jeecg.modules.im.service.ILoginLogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@RestController
@RequestMapping("/im/loginLog")
public class LoginLogController extends BaseBackController<LoginLog, ILoginLogService> {


    /**
     * 分页列表查询
     *
     * @param loginLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "登录/注册日志-分页列表查询")
    @ApiOperation(value="登录/注册日志-分页列表查询", notes="登录/注册日志-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<LoginLog>> queryPageList(LoginLog loginLog,
                                                                                        @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                        HttpServletRequest req) {
        loginLog.setServerId(getServerId());
        QueryWrapper<LoginLog> queryWrapper = QueryGenerator.initQueryWrapper(loginLog, req.getParameterMap());
        Page<LoginLog> page = new Page<LoginLog>(pageNo, pageSize);
        IPage<LoginLog> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param loginLog
     * @return
     */
    @AutoLog(value = "登录/注册日志-添加")
    @ApiOperation(value="登录/注册日志-添加", notes="登录/注册日志-添加")
    @RequiresPermissions("loginLog:im_login_log:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody LoginLog loginLog) {
        service.save(loginLog);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param loginLog
     * @return
     */
    @AutoLog(value = "登录/注册日志-编辑")
    @ApiOperation(value="登录/注册日志-编辑", notes="登录/注册日志-编辑")
    @RequiresPermissions("loginLog:im_login_log:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody LoginLog loginLog) {
        service.updateById(loginLog);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "登录/注册日志-通过id删除")
    @ApiOperation(value="登录/注册日志-通过id删除", notes="登录/注册日志-通过id删除")
    @RequiresPermissions("loginLog:im_login_log:delete")
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
    @AutoLog(value = "登录/注册日志-批量删除")
    @ApiOperation(value="登录/注册日志-批量删除", notes="登录/注册日志-批量删除")
    @RequiresPermissions("loginLog:im_login_log:deleteBatch")
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
    //@AutoLog(value = "登录/注册日志-通过id查询")
    @ApiOperation(value="登录/注册日志-通过id查询", notes="登录/注册日志-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<LoginLog> queryById(@RequestParam(name="id",required=true) String id) {
        LoginLog loginLog = service.getById(id);
        if(loginLog==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(loginLog);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param loginLog
     */
    @RequiresPermissions("loginLog:im_login_log:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LoginLog loginLog) {
        return super.exportXls(request, loginLog, LoginLog.class, "登录/注册日志");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("loginLog:im_login_log:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, LoginLog.class);
    }

}
