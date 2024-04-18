package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.Call;
import org.jeecg.modules.im.service.ICallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@RestController
@RequestMapping("/im/call")
public class CallController extends BaseBackController<Call, ICallService> {
    @Autowired
    private ICallService callService;
    
    /**
     * 分页列表查询
     *
     * @param call
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "音视频通话-分页列表查询")
    @ApiOperation(value="音视频通话-分页列表查询", notes="音视频通话-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Call>> queryPageList(Call call,
                                                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                HttpServletRequest req) {
        QueryWrapper<Call> queryWrapper = QueryGenerator.initQueryWrapper(call, req.getParameterMap());
        Page<Call> page = new Page<Call>(pageNo, pageSize);
        IPage<Call> pageList = callService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param call
     * @return
     */
    @AutoLog(value = "音视频通话-添加")
    @ApiOperation(value="音视频通话-添加", notes="音视频通话-添加")
    @RequiresPermissions("call:im_call:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Call call) {
        callService.save(call);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param call
     * @return
     */
    @AutoLog(value = "音视频通话-编辑")
    @ApiOperation(value="音视频通话-编辑", notes="音视频通话-编辑")
    @RequiresPermissions("call:im_call:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Call call) {
        callService.updateById(call);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "音视频通话-通过id删除")
    @ApiOperation(value="音视频通话-通过id删除", notes="音视频通话-通过id删除")
    @RequiresPermissions("call:im_call:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        callService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "音视频通话-批量删除")
    @ApiOperation(value="音视频通话-批量删除", notes="音视频通话-批量删除")
    @RequiresPermissions("call:im_call:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.callService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "音视频通话-通过id查询")
    @ApiOperation(value="音视频通话-通过id查询", notes="音视频通话-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Call> queryById(@RequestParam(name="id",required=true) String id) {
        Call call = callService.getById(id);
        if(call==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(call);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param call
     */
    @RequiresPermissions("call:im_call:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Call call) {
        return super.exportXls(request, call, Call.class, "音视频通话");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("call:im_call:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Call.class);
    }

}
