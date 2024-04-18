package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.service.IMucService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * 群组
 */
@RestController
@RequestMapping("/im/muc")
public class MucController extends BaseBackController<Muc, IMucService> {

    /**
     * 分页列表查询
     *
     * @param muc
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "群聊-分页列表查询")
    @ApiOperation(value="群聊-分页列表查询", notes="群聊-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Muc>> queryPageList(Muc muc,
                                                                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                              HttpServletRequest req) {
        QueryWrapper<Muc> queryWrapper = QueryGenerator.initQueryWrapper(muc, req.getParameterMap());
        Page<Muc> page = new Page<Muc>(pageNo, pageSize);
        IPage<Muc> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param muc
     * @return
     */
    @AutoLog(value = "群聊-添加")
    @ApiOperation(value="群聊-添加", notes="群聊-添加")
    @RequiresPermissions("muc:im_muc:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Muc muc) {
        service.save(muc);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param muc
     * @return
     */
    @AutoLog(value = "群聊-编辑")
    @ApiOperation(value="群聊-编辑", notes="群聊-编辑")
    @RequiresPermissions("muc:im_muc:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Muc muc) {
        service.updateById(muc);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "群聊-通过id删除")
    @ApiOperation(value="群聊-通过id删除", notes="群聊-通过id删除")
    @RequiresPermissions("muc:im_muc:delete")
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
    @AutoLog(value = "群聊-批量删除")
    @ApiOperation(value="群聊-批量删除", notes="群聊-批量删除")
    @RequiresPermissions("muc:im_muc:deleteBatch")
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
    //@AutoLog(value = "群聊-通过id查询")
    @ApiOperation(value="群聊-通过id查询", notes="群聊-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Muc> queryById(@RequestParam(name="id",required=true) String id) {
        Muc muc = service.getById(id);
        if(muc==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(muc);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param muc
     */
    @RequiresPermissions("muc:im_muc:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Muc muc) {
        return super.exportXls(request, muc, Muc.class, "群聊");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("muc:im_muc:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Muc.class);
    }


    /**
     * 解散群组
     */
    @RequestMapping("/destroy")
    public Result<Object> destroy(@RequestParam Integer id){
        return service.consoleDestroy(id);
    }

}
