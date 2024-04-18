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
import org.jeecg.modules.im.entity.Invitation;
import org.jeecg.modules.im.entity.query_helper.QInvitation;
import org.jeecg.modules.im.service.IInvitationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@RestController
@RequestMapping("/im/invitation")
public class InvitationController extends BaseBackController<Invitation, IInvitationService> {

    /**
     * 分页列表查询
     *
     * @param invitation
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "邀请注册记录-分页列表查询")
    @ApiOperation(value="邀请注册记录-分页列表查询", notes="邀请注册记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Invitation>> queryPageList(Invitation invitation,
                                                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                            HttpServletRequest req) {
        QueryWrapper<Invitation> queryWrapper = QueryGenerator.initQueryWrapper(invitation, req.getParameterMap());
        Page<Invitation> page = new Page<Invitation>(pageNo, pageSize);
        IPage<Invitation> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param invitation
     * @return
     */
    @AutoLog(value = "邀请注册记录-添加")
    @ApiOperation(value="邀请注册记录-添加", notes="邀请注册记录-添加")
    @RequiresPermissions("invitation:im_invitation:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Invitation invitation) {
        service.save(invitation);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param invitation
     * @return
     */
    @AutoLog(value = "邀请注册记录-编辑")
    @ApiOperation(value="邀请注册记录-编辑", notes="邀请注册记录-编辑")
    @RequiresPermissions("invitation:im_invitation:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Invitation invitation) {
        service.updateById(invitation);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "邀请注册记录-通过id删除")
    @ApiOperation(value="邀请注册记录-通过id删除", notes="邀请注册记录-通过id删除")
    @RequiresPermissions("invitation:im_invitation:delete")
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
    @AutoLog(value = "邀请注册记录-批量删除")
    @ApiOperation(value="邀请注册记录-批量删除", notes="邀请注册记录-批量删除")
    @RequiresPermissions("invitation:im_invitation:deleteBatch")
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
    //@AutoLog(value = "邀请注册记录-通过id查询")
    @ApiOperation(value="邀请注册记录-通过id查询", notes="邀请注册记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Invitation> queryById(@RequestParam(name="id",required=true) String id) {
        Invitation invitation = service.getById(id);
        if(invitation==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(invitation);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param invitation
     */
    @RequiresPermissions("invitation:im_invitation:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Invitation invitation) {
        return super.exportXls(request, invitation, Invitation.class, "邀请注册记录");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("invitation:im_invitation:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Invitation.class);
    }
}
