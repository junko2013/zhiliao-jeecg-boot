package org.jeecg.modules.im.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.service.IFriendService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@RestController
@RequestMapping("/im/friend")
public class FriendController extends BaseBackController<Friend, IFriendService> {
    /**
     * 分页列表查询
     *
     * @param friend
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "好友-分页列表查询")
    @ApiOperation(value="好友-分页列表查询", notes="好友-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Friend>> queryPageList(Friend friend,
                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                               HttpServletRequest req) {
        QueryWrapper<Friend> queryWrapper = QueryGenerator.initQueryWrapper(friend, req.getParameterMap());
        Page<Friend> page = new Page<Friend>(pageNo, pageSize);
        IPage<Friend> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *  编辑
     *
     * @param friend
     * @return
     */
    @AutoLog(value = "好友-编辑")
    @ApiOperation(value="好友-编辑", notes="好友-编辑")
    @RequiresPermissions("friend:im_friend:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Friend friend) {
        service.updateById(friend);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "好友-通过id删除")
    @ApiOperation(value="好友-通过id删除", notes="好友-通过id删除")
    @RequiresPermissions("friend:im_friend:delete")
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
    @AutoLog(value = "好友-批量删除")
    @ApiOperation(value="好友-批量删除", notes="好友-批量删除")
    @RequiresPermissions("friend:im_friend:deleteBatch")
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
    //@AutoLog(value = "好友-通过id查询")
    @ApiOperation(value="好友-通过id查询", notes="好友-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Friend> queryById(@RequestParam(name="id",required=true) String id) {
        Friend friend = service.getById(id);
        if(friend==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(friend);
    }
}
