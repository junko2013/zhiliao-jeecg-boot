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
import org.jeecg.modules.im.entity.GifAlbum;
import org.jeecg.modules.im.entity.query_helper.QGifAlbum;
import org.jeecg.modules.im.service.IGifAlbumService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/im/gifAlbum")
public class GifAlbumController extends BaseBackController<GifAlbum, IGifAlbumService> {
    /**
     * 分页列表查询
     *
     * @param gifAlbum
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "gif集-分页列表查询")
    @ApiOperation(value="gif集-分页列表查询", notes="gif集-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GifAlbum>> queryPageList(GifAlbum gifAlbum,
                                                                                        @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                        HttpServletRequest req) {
        QueryWrapper<GifAlbum> queryWrapper = QueryGenerator.initQueryWrapper(gifAlbum, req.getParameterMap());
        Page<GifAlbum> page = new Page<GifAlbum>(pageNo, pageSize);
        IPage<GifAlbum> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param gifAlbum
     * @return
     */
    @AutoLog(value = "gif集-添加")
    @ApiOperation(value="gif集-添加", notes="gif集-添加")
    @RequiresPermissions("gifAlbum:im_gif_album:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody GifAlbum gifAlbum) {
        service.save(gifAlbum);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param gifAlbum
     * @return
     */
    @AutoLog(value = "gif集-编辑")
    @ApiOperation(value="gif集-编辑", notes="gif集-编辑")
    @RequiresPermissions("gifAlbum:im_gif_album:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody GifAlbum gifAlbum) {
        service.updateById(gifAlbum);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "gif集-通过id删除")
    @ApiOperation(value="gif集-通过id删除", notes="gif集-通过id删除")
    @RequiresPermissions("gifAlbum:im_gif_album:delete")
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
    @AutoLog(value = "gif集-批量删除")
    @ApiOperation(value="gif集-批量删除", notes="gif集-批量删除")
    @RequiresPermissions("gifAlbum:im_gif_album:deleteBatch")
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
    //@AutoLog(value = "gif集-通过id查询")
    @ApiOperation(value="gif集-通过id查询", notes="gif集-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<GifAlbum> queryById(@RequestParam(name="id",required=true) String id) {
        GifAlbum gifAlbum = service.getById(id);
        if(gifAlbum==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gifAlbum);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gifAlbum
     */
    @RequiresPermissions("gifAlbum:im_gif_album:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GifAlbum gifAlbum) {
        return super.exportXls(request, gifAlbum, GifAlbum.class, "gif集");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("gifAlbum:im_gif_album:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GifAlbum.class);
    }

}
