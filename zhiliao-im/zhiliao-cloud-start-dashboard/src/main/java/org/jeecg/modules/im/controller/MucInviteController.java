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
import org.jeecg.modules.im.entity.MucInvite;
import org.jeecg.modules.im.service.IMucInviteService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 群组邀请记录
 */
@RestController
@RequestMapping("/im/mucInvite")
public class MucInviteController extends BaseBackController<MucInvite, IMucInviteService> {


    /**
     * 分页列表查询
     *
     * @param mucInvite
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "群聊邀请记录-分页列表查询")
    @ApiOperation(value="群聊邀请记录-分页列表查询", notes="群聊邀请记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MucInvite>> queryPageList(MucInvite mucInvite,
                                                                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                          HttpServletRequest req) {
        QueryWrapper<MucInvite> queryWrapper = QueryGenerator.initQueryWrapper(mucInvite, req.getParameterMap());
        Page<MucInvite> page = new Page<MucInvite>(pageNo, pageSize);
        IPage<MucInvite> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param mucInvite
     * @return
     */
    @AutoLog(value = "群聊邀请记录-添加")
    @ApiOperation(value="群聊邀请记录-添加", notes="群聊邀请记录-添加")
    @RequiresPermissions("mucInvite:im_muc_invite:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody MucInvite mucInvite) {
        service.save(mucInvite);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param mucInvite
     * @return
     */
    @AutoLog(value = "群聊邀请记录-编辑")
    @ApiOperation(value="群聊邀请记录-编辑", notes="群聊邀请记录-编辑")
    @RequiresPermissions("mucInvite:im_muc_invite:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody MucInvite mucInvite) {
        service.updateById(mucInvite);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "群聊邀请记录-通过id删除")
    @ApiOperation(value="群聊邀请记录-通过id删除", notes="群聊邀请记录-通过id删除")
    @RequiresPermissions("mucInvite:im_muc_invite:delete")
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
    @AutoLog(value = "群聊邀请记录-批量删除")
    @ApiOperation(value="群聊邀请记录-批量删除", notes="群聊邀请记录-批量删除")
    @RequiresPermissions("mucInvite:im_muc_invite:deleteBatch")
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
    //@AutoLog(value = "群聊邀请记录-通过id查询")
    @ApiOperation(value="群聊邀请记录-通过id查询", notes="群聊邀请记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MucInvite> queryById(@RequestParam(name="id",required=true) String id) {
        MucInvite mucInvite = service.getById(id);
        if(mucInvite==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mucInvite);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mucInvite
     */
    @RequiresPermissions("mucInvite:im_muc_invite:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MucInvite mucInvite) {
        return super.exportXls(request, mucInvite, MucInvite.class, "群聊邀请记录");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("mucInvite:im_muc_invite:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MucInvite.class);
    }

    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<MucInvite> logicDeletedUserList = service.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            service.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            service.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }

}
