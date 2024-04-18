package org.jeecg.modules.im.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.InviteCode;
import org.jeecg.modules.im.service.IInviteCodeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@RestController
@RequestMapping("/im/inviteCode")
public class InviteCodeController extends BaseBackController<InviteCode,IInviteCodeService> {

    /**
     * 分页列表查询
     *
     * @param inviteCode
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "注册邀请码-分页列表查询")
    @ApiOperation(value="注册邀请码-分页列表查询", notes="注册邀请码-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<InviteCode>> queryPageList(InviteCode inviteCode,
                                                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                            HttpServletRequest req) {
        QueryWrapper<InviteCode> queryWrapper = QueryGenerator.initQueryWrapper(inviteCode, req.getParameterMap());
        Page<InviteCode> page = new Page<InviteCode>(pageNo, pageSize);
        IPage<InviteCode> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param inviteCode
     * @return
     */
    @AutoLog(value = "注册邀请码-添加")
    @ApiOperation(value="注册邀请码-添加", notes="注册邀请码-添加")
    @RequiresPermissions("inviteCode:im_invite_code:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody InviteCode inviteCode) {
        service.save(inviteCode);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param inviteCode
     * @return
     */
    @AutoLog(value = "注册邀请码-编辑")
    @ApiOperation(value="注册邀请码-编辑", notes="注册邀请码-编辑")
    @RequiresPermissions("inviteCode:im_invite_code:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody InviteCode inviteCode) {
        service.updateById(inviteCode);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "注册邀请码-通过id删除")
    @ApiOperation(value="注册邀请码-通过id删除", notes="注册邀请码-通过id删除")
    @RequiresPermissions("inviteCode:im_invite_code:delete")
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
    @AutoLog(value = "注册邀请码-批量删除")
    @ApiOperation(value="注册邀请码-批量删除", notes="注册邀请码-批量删除")
    @RequiresPermissions("inviteCode:im_invite_code:deleteBatch")
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
    //@AutoLog(value = "注册邀请码-通过id查询")
    @ApiOperation(value="注册邀请码-通过id查询", notes="注册邀请码-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<InviteCode> queryById(@RequestParam(name="id",required=true) String id) {
        InviteCode inviteCode = service.getById(id);
        if(inviteCode==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(inviteCode);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param inviteCode
     */
    @RequiresPermissions("inviteCode:im_invite_code:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InviteCode inviteCode) {
        return super.exportXls(request, inviteCode, InviteCode.class, "注册邀请码");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("inviteCode:im_invite_code:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, InviteCode.class);
    }

}
