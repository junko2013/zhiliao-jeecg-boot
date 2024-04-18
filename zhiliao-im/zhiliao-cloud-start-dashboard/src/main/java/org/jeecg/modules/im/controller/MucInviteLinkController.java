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
import org.jeecg.modules.im.entity.MucInviteLink;
import org.jeecg.modules.im.service.IMucInviteLinkService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 群组邀请链接
 */
@RestController
@RequestMapping("/im/mucInviteLink")
public class MucInviteLinkController extends BaseBackController<MucInviteLink, IMucInviteLinkService> {


    @RequestMapping("/lock")
    public Result<Object> lock(@RequestParam Integer id){
        MucInviteLink emoji = service.getById(id);
        if(emoji!=null){
            emoji.setEnable(!emoji.getEnable());
        }
        return success(service.updateById(emoji));
    }

    /**
     * 分页列表查询
     *
     * @param mucInviteLink
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "群聊邀请链接-分页列表查询")
    @ApiOperation(value="群聊邀请链接-分页列表查询", notes="群聊邀请链接-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MucInviteLink>> queryPageList(MucInviteLink mucInviteLink,
                                                                                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                                  HttpServletRequest req) {
        QueryWrapper<MucInviteLink> queryWrapper = QueryGenerator.initQueryWrapper(mucInviteLink, req.getParameterMap());
        Page<MucInviteLink> page = new Page<MucInviteLink>(pageNo, pageSize);
        IPage<MucInviteLink> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param mucInviteLink
     * @return
     */
    @AutoLog(value = "群聊邀请链接-添加")
    @ApiOperation(value="群聊邀请链接-添加", notes="群聊邀请链接-添加")
    @RequiresPermissions("mucInviteLink:im_muc_invite_link:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody MucInviteLink mucInviteLink) {
        service.save(mucInviteLink);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param mucInviteLink
     * @return
     */
    @AutoLog(value = "群聊邀请链接-编辑")
    @ApiOperation(value="群聊邀请链接-编辑", notes="群聊邀请链接-编辑")
    @RequiresPermissions("mucInviteLink:im_muc_invite_link:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody MucInviteLink mucInviteLink) {
        service.updateById(mucInviteLink);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "群聊邀请链接-通过id删除")
    @ApiOperation(value="群聊邀请链接-通过id删除", notes="群聊邀请链接-通过id删除")
    @RequiresPermissions("mucInviteLink:im_muc_invite_link:delete")
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
    @AutoLog(value = "群聊邀请链接-批量删除")
    @ApiOperation(value="群聊邀请链接-批量删除", notes="群聊邀请链接-批量删除")
    @RequiresPermissions("mucInviteLink:im_muc_invite_link:deleteBatch")
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
    //@AutoLog(value = "群聊邀请链接-通过id查询")
    @ApiOperation(value="群聊邀请链接-通过id查询", notes="群聊邀请链接-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MucInviteLink> queryById(@RequestParam(name="id",required=true) String id) {
        MucInviteLink mucInviteLink = service.getById(id);
        if(mucInviteLink==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mucInviteLink);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mucInviteLink
     */
    @RequiresPermissions("mucInviteLink:im_muc_invite_link:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MucInviteLink mucInviteLink) {
        return super.exportXls(request, mucInviteLink, MucInviteLink.class, "群聊邀请链接");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("mucInviteLink:im_muc_invite_link:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MucInviteLink.class);
    }


    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<MucInviteLink> logicDeletedUserList = service.queryLogicDeleted();
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
