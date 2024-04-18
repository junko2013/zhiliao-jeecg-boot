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
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.query_helper.QDevice;
import org.jeecg.modules.im.service.IDeviceService;
import org.jeecg.modules.im.service.ILoginLogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@RestController
@RequestMapping("/im/device")
public class DeviceController extends BaseBackController<Device, IDeviceService> {

    @Resource
    private ILoginLogService loginLogService;
    /**
     * 分页列表查询
     *
     * @param device
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "设备-分页列表查询")
    @ApiOperation(value="设备-分页列表查询", notes="设备-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Device>> queryPageList(Device device,
                                                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                    HttpServletRequest req) {
        QueryWrapper<Device> queryWrapper = QueryGenerator.initQueryWrapper(device, req.getParameterMap());
        Page<Device> page = new Page<Device>(pageNo, pageSize);
        IPage<Device> pageList = service.page(page, queryWrapper);
        pageList.getRecords().forEach(e -> {
            e.setLoginLog(loginLogService.findLatestByDeviceId(e.getId()));
        });
        return Result.OK(pageList);
    }

    /**
     *  编辑
     *
     * @param device
     * @return
     */
    @AutoLog(value = "设备-编辑")
    @ApiOperation(value="设备-编辑", notes="设备-编辑")
    @RequiresPermissions("device:im_device:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Device device) {
        service.updateById(device);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备-通过id删除")
    @ApiOperation(value="设备-通过id删除", notes="设备-通过id删除")
    @RequiresPermissions("device:im_device:delete")
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
    @AutoLog(value = "设备-批量删除")
    @ApiOperation(value="设备-批量删除", notes="设备-批量删除")
    @RequiresPermissions("device:im_device:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    //封禁/解除封禁

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "设备-通过id查询")
    @ApiOperation(value="设备-通过id查询", notes="设备-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Device> queryById(@RequestParam(name="id",required=true) String id) {
        Device device = service.getById(id);
        if(device==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(device);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param device
     */
    @RequiresPermissions("device:im_device:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Device device) {
        return super.exportXls(request, device, Device.class, "设备");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("device:im_device:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Device.class);
    }
}
