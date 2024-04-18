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
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.query_helper.QLink;
import org.jeecg.modules.im.service.ILinkService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * 发现页网页链接
 */
@RestController
@RequestMapping("/im/link")
public class LinkController extends BaseBackController<Link, ILinkService> {

    /**
     * 分页列表查询
     *
     * @param link
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "发现页网页链接-分页列表查询")
    @ApiOperation(value="发现页网页链接-分页列表查询", notes="发现页网页链接-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Link>> queryPageList(Link link,
                                                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                HttpServletRequest req) {
        QueryWrapper<Link> queryWrapper = QueryGenerator.initQueryWrapper(link, req.getParameterMap());
        Page<Link> page = new Page<Link>(pageNo, pageSize);
        IPage<Link> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param link
     * @return
     */
    @AutoLog(value = "发现页网页链接-添加")
    @ApiOperation(value="发现页网页链接-添加", notes="发现页网页链接-添加")
    @RequiresPermissions("link:im_link:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Link link) {
        service.save(link);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param link
     * @return
     */
    @AutoLog(value = "发现页网页链接-编辑")
    @ApiOperation(value="发现页网页链接-编辑", notes="发现页网页链接-编辑")
    @RequiresPermissions("link:im_link:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Link link) {
        service.updateById(link);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "发现页网页链接-通过id删除")
    @ApiOperation(value="发现页网页链接-通过id删除", notes="发现页网页链接-通过id删除")
    @RequiresPermissions("link:im_link:delete")
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
    @AutoLog(value = "发现页网页链接-批量删除")
    @ApiOperation(value="发现页网页链接-批量删除", notes="发现页网页链接-批量删除")
    @RequiresPermissions("link:im_link:deleteBatch")
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
    //@AutoLog(value = "发现页网页链接-通过id查询")
    @ApiOperation(value="发现页网页链接-通过id查询", notes="发现页网页链接-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Link> queryById(@RequestParam(name="id",required=true) String id) {
        Link link = service.getById(id);
        if(link==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(link);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param link
     */
    @RequiresPermissions("link:im_link:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Link link) {
        return super.exportXls(request, link, Link.class, "发现页网页链接");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("link:im_link:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Link.class);
    }

}
