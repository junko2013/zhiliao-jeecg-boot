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
import org.jeecg.modules.im.entity.MySticker;
import org.jeecg.modules.im.service.IMyStickerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 我的贴纸包
* @Author: jeecg-boot
* @Date:   2024-04-18
* @Version: V1.0
*/
@Api(tags="我的贴纸包")
@RestController
@RequestMapping("/IM/mySticker")
@Slf4j
public class MyStickerController extends BaseBackController<MySticker, IMyStickerService> {
   
   /**
    * 分页列表查询
    *
    * @param mySticker
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "我的贴纸包-分页列表查询")
   @ApiOperation(value="我的贴纸包-分页列表查询", notes="我的贴纸包-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<MySticker>> queryPageList(MySticker mySticker,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<MySticker> queryWrapper = QueryGenerator.initQueryWrapper(mySticker, req.getParameterMap());
       Page<MySticker> page = new Page<MySticker>(pageNo, pageSize);
       IPage<MySticker> pageList = service.page(page, queryWrapper);
       return Result.OK(pageList);
   }
   
   /**
    *   添加
    *
    * @param mySticker
    * @return
    */
   @AutoLog(value = "我的贴纸包-添加")
   @ApiOperation(value="我的贴纸包-添加", notes="我的贴纸包-添加")
   @RequiresPermissions("mySticker:im_my_sticker:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody MySticker mySticker) {
       service.save(mySticker);
       return Result.OK("添加成功！");
   }
   
   /**
    *  编辑
    *
    * @param mySticker
    * @return
    */
   @AutoLog(value = "我的贴纸包-编辑")
   @ApiOperation(value="我的贴纸包-编辑", notes="我的贴纸包-编辑")
   @RequiresPermissions("mySticker:im_my_sticker:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody MySticker mySticker) {
       service.updateById(mySticker);
       return Result.OK("编辑成功!");
   }
   
   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "我的贴纸包-通过id删除")
   @ApiOperation(value="我的贴纸包-通过id删除", notes="我的贴纸包-通过id删除")
   @RequiresPermissions("mySticker:im_my_sticker:delete")
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
   @AutoLog(value = "我的贴纸包-批量删除")
   @ApiOperation(value="我的贴纸包-批量删除", notes="我的贴纸包-批量删除")
   @RequiresPermissions("mySticker:im_my_sticker:deleteBatch")
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
   //@AutoLog(value = "我的贴纸包-通过id查询")
   @ApiOperation(value="我的贴纸包-通过id查询", notes="我的贴纸包-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<MySticker> queryById(@RequestParam(name="id",required=true) String id) {
       MySticker mySticker = service.getById(id);
       if(mySticker==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(mySticker);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param mySticker
   */
   @RequiresPermissions("mySticker:im_my_sticker:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, MySticker mySticker) {
       return super.exportXls(request, mySticker, MySticker.class, "我的贴纸包");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequiresPermissions("mySticker:im_my_sticker:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, MySticker.class);
   }

}
