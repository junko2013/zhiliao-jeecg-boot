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
import org.jeecg.modules.im.entity.FeedbackType;
import org.jeecg.modules.im.service.IFeedbackTypeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 反馈类型
* @Author: jeecg-boot
* @Date:   2024-04-18
* @Version: V1.0
*/
@Api(tags="反馈类型")
@RestController
@RequestMapping("/im/feedbackType")
@Slf4j
public class FeedbackTypeController extends BaseBackController<FeedbackType, IFeedbackTypeService> {
   
   /**
    * 分页列表查询
    *
    * @param feedbackType
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "反馈类型-分页列表查询")
   @ApiOperation(value="反馈类型-分页列表查询", notes="反馈类型-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<FeedbackType>> queryPageList(FeedbackType feedbackType,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<FeedbackType> queryWrapper = QueryGenerator.initQueryWrapper(feedbackType, req.getParameterMap());
       Page<FeedbackType> page = new Page<FeedbackType>(pageNo, pageSize);
       IPage<FeedbackType> pageList = service.page(page, queryWrapper);
       return Result.OK(pageList);
   }
   
   /**
    *   添加
    *
    * @param feedbackType
    * @return
    */
   @AutoLog(value = "反馈类型-添加")
   @ApiOperation(value="反馈类型-添加", notes="反馈类型-添加")
   @RequiresPermissions("feedbackType:im_feedback_type:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody FeedbackType feedbackType) {
       service.save(feedbackType);
       return Result.OK("添加成功！");
   }
   
   /**
    *  编辑
    *
    * @param feedbackType
    * @return
    */
   @AutoLog(value = "反馈类型-编辑")
   @ApiOperation(value="反馈类型-编辑", notes="反馈类型-编辑")
   @RequiresPermissions("feedbackType:im_feedback_type:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody FeedbackType feedbackType) {
       service.updateById(feedbackType);
       return Result.OK("编辑成功!");
   }
   
   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "反馈类型-通过id删除")
   @ApiOperation(value="反馈类型-通过id删除", notes="反馈类型-通过id删除")
   @RequiresPermissions("feedbackType:im_feedback_type:delete")
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
   @AutoLog(value = "反馈类型-批量删除")
   @ApiOperation(value="反馈类型-批量删除", notes="反馈类型-批量删除")
   @RequiresPermissions("feedbackType:im_feedback_type:deleteBatch")
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
   //@AutoLog(value = "反馈类型-通过id查询")
   @ApiOperation(value="反馈类型-通过id查询", notes="反馈类型-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<FeedbackType> queryById(@RequestParam(name="id",required=true) String id) {
       FeedbackType feedbackType = service.getById(id);
       if(feedbackType==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(feedbackType);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param feedbackType
   */
   @RequiresPermissions("feedbackType:im_feedback_type:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, FeedbackType feedbackType) {
       return super.exportXls(request, feedbackType, FeedbackType.class, "反馈类型");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequiresPermissions("feedbackType:im_feedback_type:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, FeedbackType.class);
   }

}
