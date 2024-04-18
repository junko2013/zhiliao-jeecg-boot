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
import org.jeecg.modules.im.entity.MucMsgRead;
import org.jeecg.modules.im.service.IMucMsgReadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 群消息已读记录
* @Author: jeecg-boot
* @Date:   2024-04-18
* @Version: V1.0
*/
@Api(tags="群消息已读记录")
@RestController
@RequestMapping("/im/mucMsgRead")
@Slf4j
public class MucMsgReadController extends BaseBackController<MucMsgRead, IMucMsgReadService> {

   /**
    * 分页列表查询
    *
    * @param mucMsgRead
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "群消息已读记录-分页列表查询")
   @ApiOperation(value="群消息已读记录-分页列表查询", notes="群消息已读记录-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<MucMsgRead>> queryPageList(MucMsgRead mucMsgRead,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<MucMsgRead> queryWrapper = QueryGenerator.initQueryWrapper(mucMsgRead, req.getParameterMap());
       Page<MucMsgRead> page = new Page<MucMsgRead>(pageNo, pageSize);
       IPage<MucMsgRead> pageList = service.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param mucMsgRead
    * @return
    */
   @AutoLog(value = "群消息已读记录-添加")
   @ApiOperation(value="群消息已读记录-添加", notes="群消息已读记录-添加")
   @RequiresPermissions("mucMsgRead:im_muc_msg_read:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody MucMsgRead mucMsgRead) {
       service.save(mucMsgRead);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param mucMsgRead
    * @return
    */
   @AutoLog(value = "群消息已读记录-编辑")
   @ApiOperation(value="群消息已读记录-编辑", notes="群消息已读记录-编辑")
   @RequiresPermissions("mucMsgRead:im_muc_msg_read:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody MucMsgRead mucMsgRead) {
       service.updateById(mucMsgRead);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "群消息已读记录-通过id删除")
   @ApiOperation(value="群消息已读记录-通过id删除", notes="群消息已读记录-通过id删除")
   @RequiresPermissions("mucMsgRead:im_muc_msg_read:delete")
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
   @AutoLog(value = "群消息已读记录-批量删除")
   @ApiOperation(value="群消息已读记录-批量删除", notes="群消息已读记录-批量删除")
   @RequiresPermissions("mucMsgRead:im_muc_msg_read:deleteBatch")
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
   //@AutoLog(value = "群消息已读记录-通过id查询")
   @ApiOperation(value="群消息已读记录-通过id查询", notes="群消息已读记录-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<MucMsgRead> queryById(@RequestParam(name="id",required=true) String id) {
       MucMsgRead mucMsgRead = service.getById(id);
       if(mucMsgRead==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(mucMsgRead);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param mucMsgRead
   */
   @RequiresPermissions("mucMsgRead:im_muc_msg_read:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, MucMsgRead mucMsgRead) {
       return super.exportXls(request, mucMsgRead, MucMsgRead.class, "群消息已读记录");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequiresPermissions("mucMsgRead:im_muc_msg_read:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, MucMsgRead.class);
   }

}
