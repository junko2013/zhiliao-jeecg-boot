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
import org.jeecg.modules.im.entity.MucMsgDelete;
import org.jeecg.modules.im.service.IMucMsgDeleteService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 群聊消息删除记录
* @Author: jeecg-boot
* @Date:   2024-04-18
* @Version: V1.0
*/
@Api(tags="群聊消息删除记录")
@RestController
@RequestMapping("/im/mucMsgDelete")
@Slf4j
public class MucMsgDeleteController extends BaseBackController<MucMsgDelete, IMucMsgDeleteService> {

   /**
    * 分页列表查询
    *
    * @param mucMsgDelete
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "群聊消息删除记录-分页列表查询")
   @ApiOperation(value="群聊消息删除记录-分页列表查询", notes="群聊消息删除记录-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<MucMsgDelete>> queryPageList(MucMsgDelete mucMsgDelete,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<MucMsgDelete> queryWrapper = QueryGenerator.initQueryWrapper(mucMsgDelete, req.getParameterMap());
       Page<MucMsgDelete> page = new Page<MucMsgDelete>(pageNo, pageSize);
       IPage<MucMsgDelete> pageList = service.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param mucMsgDelete
    * @return
    */
   @AutoLog(value = "群聊消息删除记录-添加")
   @ApiOperation(value="群聊消息删除记录-添加", notes="群聊消息删除记录-添加")
   @RequiresPermissions("mucMsgDelete:im_muc_msg_delete:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody MucMsgDelete mucMsgDelete) {
       service.save(mucMsgDelete);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param mucMsgDelete
    * @return
    */
   @AutoLog(value = "群聊消息删除记录-编辑")
   @ApiOperation(value="群聊消息删除记录-编辑", notes="群聊消息删除记录-编辑")
   @RequiresPermissions("mucMsgDelete:im_muc_msg_delete:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody MucMsgDelete mucMsgDelete) {
       service.updateById(mucMsgDelete);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "群聊消息删除记录-通过id删除")
   @ApiOperation(value="群聊消息删除记录-通过id删除", notes="群聊消息删除记录-通过id删除")
   @RequiresPermissions("mucMsgDelete:im_muc_msg_delete:delete")
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
   @AutoLog(value = "群聊消息删除记录-批量删除")
   @ApiOperation(value="群聊消息删除记录-批量删除", notes="群聊消息删除记录-批量删除")
   @RequiresPermissions("mucMsgDelete:im_muc_msg_delete:deleteBatch")
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
   //@AutoLog(value = "群聊消息删除记录-通过id查询")
   @ApiOperation(value="群聊消息删除记录-通过id查询", notes="群聊消息删除记录-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<MucMsgDelete> queryById(@RequestParam(name="id",required=true) String id) {
       MucMsgDelete mucMsgDelete = service.getById(id);
       if(mucMsgDelete==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(mucMsgDelete);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param mucMsgDelete
   */
   @RequiresPermissions("mucMsgDelete:im_muc_msg_delete:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, MucMsgDelete mucMsgDelete) {
       return super.exportXls(request, mucMsgDelete, MucMsgDelete.class, "群聊消息删除记录");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequiresPermissions("mucMsgDelete:im_muc_msg_delete:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, MucMsgDelete.class);
   }

}
