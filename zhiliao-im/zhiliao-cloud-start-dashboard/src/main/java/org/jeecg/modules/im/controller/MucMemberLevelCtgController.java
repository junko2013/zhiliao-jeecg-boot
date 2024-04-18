package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;
import org.jeecg.modules.im.service.IMucMemberLevelCtgService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * 群组成员等级分类
 */
@RestController
@RequestMapping("/im/mucMemberLevel/ctg")
public class MucMemberLevelCtgController extends BaseBackController<MucMemberLevelCtg, IMucMemberLevelCtgService> {

    /**
     * 分页列表查询
     *
     * @param mucMemberLevelCtg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "群聊成员等级分类-分页列表查询")
    @ApiOperation(value="群聊成员等级分类-分页列表查询", notes="群聊成员等级分类-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MucMemberLevelCtg>> queryPageList(MucMemberLevelCtg mucMemberLevelCtg,
                                                                                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                                          HttpServletRequest req) {
        QueryWrapper<MucMemberLevelCtg> queryWrapper = QueryGenerator.initQueryWrapper(mucMemberLevelCtg, req.getParameterMap());
        Page<MucMemberLevelCtg> page = new Page<MucMemberLevelCtg>(pageNo, pageSize);
        IPage<MucMemberLevelCtg> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param mucMemberLevelCtg
     * @return
     */
    @AutoLog(value = "群聊成员等级分类-添加")
    @ApiOperation(value="群聊成员等级分类-添加", notes="群聊成员等级分类-添加")
    @RequiresPermissions("mucMemberLevelCtg:im_muc_member_level_ctg:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody MucMemberLevelCtg mucMemberLevelCtg) {
        service.save(mucMemberLevelCtg);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param mucMemberLevelCtg
     * @return
     */
    @AutoLog(value = "群聊成员等级分类-编辑")
    @ApiOperation(value="群聊成员等级分类-编辑", notes="群聊成员等级分类-编辑")
    @RequiresPermissions("mucMemberLevelCtg:im_muc_member_level_ctg:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody MucMemberLevelCtg mucMemberLevelCtg) {
        service.updateById(mucMemberLevelCtg);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "群聊成员等级分类-通过id删除")
    @ApiOperation(value="群聊成员等级分类-通过id删除", notes="群聊成员等级分类-通过id删除")
    @RequiresPermissions("mucMemberLevelCtg:im_muc_member_level_ctg:delete")
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
    @AutoLog(value = "群聊成员等级分类-批量删除")
    @ApiOperation(value="群聊成员等级分类-批量删除", notes="群聊成员等级分类-批量删除")
    @RequiresPermissions("mucMemberLevelCtg:im_muc_member_level_ctg:deleteBatch")
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
    //@AutoLog(value = "群聊成员等级分类-通过id查询")
    @ApiOperation(value="群聊成员等级分类-通过id查询", notes="群聊成员等级分类-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MucMemberLevelCtg> queryById(@RequestParam(name="id",required=true) String id) {
        MucMemberLevelCtg mucMemberLevelCtg = service.getById(id);
        if(mucMemberLevelCtg==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mucMemberLevelCtg);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mucMemberLevelCtg
     */
    @RequiresPermissions("mucMemberLevelCtg:im_muc_member_level_ctg:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MucMemberLevelCtg mucMemberLevelCtg) {
        return super.exportXls(request, mucMemberLevelCtg, MucMemberLevelCtg.class, "群聊成员等级分类");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("mucMemberLevelCtg:im_muc_member_level_ctg:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MucMemberLevelCtg.class);
    }
}
