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
import org.jeecg.modules.im.entity.MyGif;
import org.jeecg.modules.im.service.IMyGifService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 我的gif
* @Author: jeecg-boot
* @Date:   2024-04-18
* @Version: V1.0
*/
@Api(tags="我的gif")
@RestController
@RequestMapping("/im/myGif")
@Slf4j
public class MyGifController extends BaseBackController<MyGif, IMyGifService> {

   /**
    * 分页列表查询
    *
    * @param myGif
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "我的gif-分页列表查询")
   @ApiOperation(value="我的gif-分页列表查询", notes="我的gif-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<MyGif>> queryPageList(MyGif myGif,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<MyGif> queryWrapper = QueryGenerator.initQueryWrapper(myGif, req.getParameterMap());
       Page<MyGif> page = new Page<MyGif>(pageNo, pageSize);
       IPage<MyGif> pageList = service.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param myGif
    * @return
    */
   @AutoLog(value = "我的gif-添加")
   @ApiOperation(value="我的gif-添加", notes="我的gif-添加")
   @RequiresPermissions("myGif:im_my_gif:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody MyGif myGif) {
       service.save(myGif);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param myGif
    * @return
    */
   @AutoLog(value = "我的gif-编辑")
   @ApiOperation(value="我的gif-编辑", notes="我的gif-编辑")
   @RequiresPermissions("myGif:im_my_gif:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody MyGif myGif) {
       service.updateById(myGif);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "我的gif-通过id删除")
   @ApiOperation(value="我的gif-通过id删除", notes="我的gif-通过id删除")
   @RequiresPermissions("myGif:im_my_gif:delete")
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
   @AutoLog(value = "我的gif-批量删除")
   @ApiOperation(value="我的gif-批量删除", notes="我的gif-批量删除")
   @RequiresPermissions("myGif:im_my_gif:deleteBatch")
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
   //@AutoLog(value = "我的gif-通过id查询")
   @ApiOperation(value="我的gif-通过id查询", notes="我的gif-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<MyGif> queryById(@RequestParam(name="id",required=true) String id) {
       MyGif myGif = service.getById(id);
       if(myGif==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(myGif);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param myGif
   */
   @RequiresPermissions("myGif:im_my_gif:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, MyGif myGif) {
       return super.exportXls(request, myGif, MyGif.class, "我的gif");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequiresPermissions("myGif:im_my_gif:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, MyGif.class);
   }

}
