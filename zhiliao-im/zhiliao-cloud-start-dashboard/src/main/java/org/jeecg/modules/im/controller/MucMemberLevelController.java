
package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.MucMemberLevel;
import org.jeecg.modules.im.service.IMucMemberLevelService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * 群组成员等级
 */
@RestController
@RequestMapping("/im/mucMemberLevel")
public class MucMemberLevelController extends BaseBackController<MucMemberLevel, IMucMemberLevelService> {

    /**
     * 分页列表查询
     *
     * @param mucMemberLevel
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "群聊成员等级-分页列表查询")
    @ApiOperation(value="群聊成员等级-分页列表查询", notes="群聊成员等级-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MucMemberLevel>> queryPageList(MucMemberLevel mucMemberLevel,
                                                                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                                    HttpServletRequest req) {
        QueryWrapper<MucMemberLevel> queryWrapper = QueryGenerator.initQueryWrapper(mucMemberLevel, req.getParameterMap());
        Page<MucMemberLevel> page = new Page<MucMemberLevel>(pageNo, pageSize);
        IPage<MucMemberLevel> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param mucMemberLevel
     * @return
     */
    @AutoLog(value = "群聊成员等级-添加")
    @ApiOperation(value="群聊成员等级-添加", notes="群聊成员等级-添加")
    @RequiresPermissions("mucMemberLevel:im_muc_member_level:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody MucMemberLevel mucMemberLevel) {
        service.save(mucMemberLevel);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param mucMemberLevel
     * @return
     */
    @AutoLog(value = "群聊成员等级-编辑")
    @ApiOperation(value="群聊成员等级-编辑", notes="群聊成员等级-编辑")
    @RequiresPermissions("mucMemberLevel:im_muc_member_level:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody MucMemberLevel mucMemberLevel) {
        service.updateById(mucMemberLevel);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "群聊成员等级-通过id删除")
    @ApiOperation(value="群聊成员等级-通过id删除", notes="群聊成员等级-通过id删除")
    @RequiresPermissions("mucMemberLevel:im_muc_member_level:delete")
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
    @AutoLog(value = "群聊成员等级-批量删除")
    @ApiOperation(value="群聊成员等级-批量删除", notes="群聊成员等级-批量删除")
    @RequiresPermissions("mucMemberLevel:im_muc_member_level:deleteBatch")
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
    //@AutoLog(value = "群聊成员等级-通过id查询")
    @ApiOperation(value="群聊成员等级-通过id查询", notes="群聊成员等级-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MucMemberLevel> queryById(@RequestParam(name="id",required=true) String id) {
        MucMemberLevel mucMemberLevel = service.getById(id);
        if(mucMemberLevel==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mucMemberLevel);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mucMemberLevel
     */
    @RequiresPermissions("mucMemberLevel:im_muc_member_level:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MucMemberLevel mucMemberLevel) {
        return super.exportXls(request, mucMemberLevel, MucMemberLevel.class, "群聊成员等级");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("mucMemberLevel:im_muc_member_level:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MucMemberLevel.class);
    }
}
