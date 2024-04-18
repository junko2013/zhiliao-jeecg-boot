package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.Bill;
import org.jeecg.modules.im.service.IBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * 账单
 */
@RestController
@RequestMapping("/im/bill")
public class BillController extends BaseBackController<Bill,IBillService>{
    /**
     * 分页列表查询
     *
     * @param bill
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "账变-分页列表查询")
    @ApiOperation(value="账变-分页列表查询", notes="账变-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Bill>> queryPageList(Bill bill,
                                                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                HttpServletRequest req) {
        QueryWrapper<Bill> queryWrapper = QueryGenerator.initQueryWrapper(bill, req.getParameterMap());
        Page<Bill> page = new Page<Bill>(pageNo, pageSize);
        IPage<Bill> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param bill
     * @return
     */
    @AutoLog(value = "账变-添加")
    @ApiOperation(value="账变-添加", notes="账变-添加")
    @RequiresPermissions("bill:im_bill:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Bill bill) {
        service.save(bill);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param bill
     * @return
     */
    @AutoLog(value = "账变-编辑")
    @ApiOperation(value="账变-编辑", notes="账变-编辑")
    @RequiresPermissions("bill:im_bill:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Bill bill) {
        service.updateById(bill);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "账变-通过id删除")
    @ApiOperation(value="账变-通过id删除", notes="账变-通过id删除")
    @RequiresPermissions("bill:im_bill:delete")
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
    @AutoLog(value = "账变-批量删除")
    @ApiOperation(value="账变-批量删除", notes="账变-批量删除")
    @RequiresPermissions("bill:im_bill:deleteBatch")
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
    //@AutoLog(value = "账变-通过id查询")
    @ApiOperation(value="账变-通过id查询", notes="账变-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Bill> queryById(@RequestParam(name="id",required=true) String id) {
        Bill bill = service.getById(id);
        if(bill==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bill);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bill
     */
    @RequiresPermissions("bill:im_bill:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Bill bill) {
        return super.exportXls(request, bill, Bill.class, "账变");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("bill:im_bill:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Bill.class);
    }

}
