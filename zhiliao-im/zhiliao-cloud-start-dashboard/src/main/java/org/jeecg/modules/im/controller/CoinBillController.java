package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.CoinBill;
import org.jeecg.modules.im.service.ICoinBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 金币账变
* @Author: jeecg-boot
* @Date:   2024-04-17
* @Version: V1.0
*/
@Api(tags="金币账变")
@RestController
@RequestMapping("/im/coinBill")
@Slf4j
public class CoinBillController extends BaseBackController<CoinBill, ICoinBillService> {
   
   /**
    * 分页列表查询
    *
    * @param coinBill
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "金币账变-分页列表查询")
   @ApiOperation(value="金币账变-分页列表查询", notes="金币账变-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<CoinBill>> queryPageList(CoinBill coinBill,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<CoinBill> queryWrapper = QueryGenerator.initQueryWrapper(coinBill, req.getParameterMap());
       Page<CoinBill> page = new Page<CoinBill>(pageNo, pageSize);
       IPage<CoinBill> pageList = service.page(page, queryWrapper);
       return Result.OK(pageList);
   }
   
   /**
    *   添加
    *
    * @param coinBill
    * @return
    */
   @AutoLog(value = "金币账变-添加")
   @ApiOperation(value="金币账变-添加", notes="金币账变-添加")
   @RequiresPermissions("coinBill:im_coin_bill:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody CoinBill coinBill) {
       service.save(coinBill);
       return Result.OK("添加成功！");
   }
   
   /**
    *  编辑
    *
    * @param coinBill
    * @return
    */
   @AutoLog(value = "金币账变-编辑")
   @ApiOperation(value="金币账变-编辑", notes="金币账变-编辑")
   @RequiresPermissions("coinBill:im_coin_bill:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody CoinBill coinBill) {
       service.updateById(coinBill);
       return Result.OK("编辑成功!");
   }
   
   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "金币账变-通过id删除")
   @ApiOperation(value="金币账变-通过id删除", notes="金币账变-通过id删除")
   @RequiresPermissions("coinBill:im_coin_bill:delete")
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
   @AutoLog(value = "金币账变-批量删除")
   @ApiOperation(value="金币账变-批量删除", notes="金币账变-批量删除")
   @RequiresPermissions("coinBill:im_coin_bill:deleteBatch")
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
   //@AutoLog(value = "金币账变-通过id查询")
   @ApiOperation(value="金币账变-通过id查询", notes="金币账变-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<CoinBill> queryById(@RequestParam(name="id",required=true) String id) {
       CoinBill coinBill = service.getById(id);
       if(coinBill==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(coinBill);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param coinBill
   */
   @RequiresPermissions("coinBill:im_coin_bill:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, CoinBill coinBill) {
       return super.exportXls(request, coinBill, CoinBill.class, "金币账变");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequiresPermissions("coinBill:im_coin_bill:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, CoinBill.class);
   }

}
