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
import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.entity.query_helper.QMucMember;
import org.jeecg.modules.im.service.IMucMemberService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * 群组成员
 */
@RestController
@RequestMapping("/im/mucMember")
public class MucMemberController extends BaseBackController<MucMember, IMucMemberService> {

    /**
     * 分页列表查询
     *
     * @param mucMember
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "群聊成员-分页列表查询")
    @ApiOperation(value="群聊成员-分页列表查询", notes="群聊成员-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MucMember>> queryPageList(MucMember mucMember,
                                                                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                          HttpServletRequest req) {
        QueryWrapper<MucMember> queryWrapper = QueryGenerator.initQueryWrapper(mucMember, req.getParameterMap());
        Page<MucMember> page = new Page<MucMember>(pageNo, pageSize);
        IPage<MucMember> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param mucMember
     * @return
     */
    @AutoLog(value = "群聊成员-添加")
    @ApiOperation(value="群聊成员-添加", notes="群聊成员-添加")
    @RequiresPermissions("mucMember:im_muc_member:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody MucMember mucMember) {
        service.save(mucMember);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param mucMember
     * @return
     */
    @AutoLog(value = "群聊成员-编辑")
    @ApiOperation(value="群聊成员-编辑", notes="群聊成员-编辑")
    @RequiresPermissions("mucMember:im_muc_member:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody MucMember mucMember) {
        service.updateById(mucMember);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "群聊成员-通过id删除")
    @ApiOperation(value="群聊成员-通过id删除", notes="群聊成员-通过id删除")
    @RequiresPermissions("mucMember:im_muc_member:delete")
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
    @AutoLog(value = "群聊成员-批量删除")
    @ApiOperation(value="群聊成员-批量删除", notes="群聊成员-批量删除")
    @RequiresPermissions("mucMember:im_muc_member:deleteBatch")
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
    //@AutoLog(value = "群聊成员-通过id查询")
    @ApiOperation(value="群聊成员-通过id查询", notes="群聊成员-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MucMember> queryById(@RequestParam(name="id",required=true) String id) {
        MucMember mucMember = service.getById(id);
        if(mucMember==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mucMember);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mucMember
     */
    @RequiresPermissions("mucMember:im_muc_member:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MucMember mucMember) {
        return super.exportXls(request, mucMember, MucMember.class, "群聊成员");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("mucMember:im_muc_member:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MucMember.class);
    }
    
    /**
     * 邀请用户进群
     */
    @RequestMapping("/invite")
    public Result<Object> invite(@RequestParam Integer mucId, @RequestParam String userIds) {
        return service.consoleInvite(mucId,userIds);
    }
}
