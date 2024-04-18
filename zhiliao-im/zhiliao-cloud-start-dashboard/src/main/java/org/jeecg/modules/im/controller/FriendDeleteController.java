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
import org.jeecg.modules.im.entity.FriendDelete;
import org.jeecg.modules.im.service.IFriendDeleteService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 好友删除
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Api(tags="好友删除")
@RestController
@RequestMapping("/im/friendDelete")
@Slf4j
public class FriendDeleteController extends BaseBackController<FriendDelete, IFriendDeleteService> {

    /**
     * 分页列表查询
     *
     * @param friendDelete
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "好友删除-分页列表查询")
    @ApiOperation(value="好友删除-分页列表查询", notes="好友删除-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FriendDelete>> queryPageList(FriendDelete friendDelete,
                                                                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                                HttpServletRequest req) {
        QueryWrapper<FriendDelete> queryWrapper = QueryGenerator.initQueryWrapper(friendDelete, req.getParameterMap());
        Page<FriendDelete> page = new Page<FriendDelete>(pageNo, pageSize);
        IPage<FriendDelete> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "好友删除-通过id删除")
    @ApiOperation(value="好友删除-通过id删除", notes="好友删除-通过id删除")
    @RequiresPermissions("friendDelete:im_friend_delete:delete")
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
    @AutoLog(value = "好友删除-批量删除")
    @ApiOperation(value="好友删除-批量删除", notes="好友删除-批量删除")
    @RequiresPermissions("friendDelete:im_friend_delete:deleteBatch")
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
    //@AutoLog(value = "好友删除-通过id查询")
    @ApiOperation(value="好友删除-通过id查询", notes="好友删除-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FriendDelete> queryById(@RequestParam(name="id",required=true) String id) {
        FriendDelete friendDelete = service.getById(id);
        if(friendDelete==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(friendDelete);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param friendDelete
     */
    @RequiresPermissions("friendDelete:im_friend_delete:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FriendDelete friendDelete) {
        return super.exportXls(request, friendDelete, FriendDelete.class, "好友删除");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("friendDelete:im_friend_delete:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FriendDelete.class);
    }

}
