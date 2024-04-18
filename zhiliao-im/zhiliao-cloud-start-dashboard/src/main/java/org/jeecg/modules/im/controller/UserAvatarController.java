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
import org.jeecg.modules.im.entity.UserAvatar;
import org.jeecg.modules.im.entity.query_helper.QUserAvatar;
import org.jeecg.modules.im.service.IUserAvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;


@RestController
@RequestMapping("/im/userAvatar")
public class UserAvatarController extends BaseBackController {

    @Autowired
    private IUserAvatarService userAvatarService;

    /**
     * 分页列表查询
     *
     * @param userAvatar
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "用户历史头像-分页列表查询")
    @ApiOperation(value="用户历史头像-分页列表查询", notes="用户历史头像-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<UserAvatar>> queryPageList(UserAvatar userAvatar,
                                                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                            HttpServletRequest req) {
        QueryWrapper<UserAvatar> queryWrapper = QueryGenerator.initQueryWrapper(userAvatar, req.getParameterMap());
        Page<UserAvatar> page = new Page<UserAvatar>(pageNo, pageSize);
        IPage<UserAvatar> pageList = userAvatarService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param userAvatar
     * @return
     */
    @AutoLog(value = "用户历史头像-添加")
    @ApiOperation(value="用户历史头像-添加", notes="用户历史头像-添加")
    @RequiresPermissions("userAvatar:im_user_avatar:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody UserAvatar userAvatar) {
        userAvatar.setTsCreate(new Date());
        userAvatarService.save(userAvatar);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param userAvatar
     * @return
     */
    @AutoLog(value = "用户历史头像-编辑")
    @ApiOperation(value="用户历史头像-编辑", notes="用户历史头像-编辑")
    @RequiresPermissions("userAvatar:im_user_avatar:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody UserAvatar userAvatar) {
        userAvatarService.updateById(userAvatar);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户历史头像-通过id删除")
    @ApiOperation(value="用户历史头像-通过id删除", notes="用户历史头像-通过id删除")
    @RequiresPermissions("userAvatar:im_user_avatar:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        userAvatarService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户历史头像-批量删除")
    @ApiOperation(value="用户历史头像-批量删除", notes="用户历史头像-批量删除")
    @RequiresPermissions("userAvatar:im_user_avatar:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.userAvatarService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "用户历史头像-通过id查询")
    @ApiOperation(value="用户历史头像-通过id查询", notes="用户历史头像-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<UserAvatar> queryById(@RequestParam(name="id",required=true) String id) {
        UserAvatar userAvatar = userAvatarService.getById(id);
        if(userAvatar==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(userAvatar);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param userAvatar
     */
    @RequiresPermissions("userAvatar:im_user_avatar:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, UserAvatar userAvatar) {
        return super.exportXls(request, userAvatar, UserAvatar.class, "用户历史头像");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("userAvatar:im_user_avatar:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, UserAvatar.class);
    }

}
