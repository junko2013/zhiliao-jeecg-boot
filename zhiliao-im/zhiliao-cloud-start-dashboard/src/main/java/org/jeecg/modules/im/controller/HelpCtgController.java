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
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.entity.HelpCtg;
import org.jeecg.modules.im.service.IHelpCtgService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
* @Description: 使用帮助分类
* @Author: jeecg-boot
* @Date:   2024-04-18
* @Version: V1.0
*/
@Api(tags="使用帮助分类")
@RestController
@RequestMapping("/im/helpCtg")
@Slf4j
public class HelpCtgController extends BaseBackController<HelpCtg, IHelpCtgService>{

   /**
    * 分页列表查询
    *
    * @param helpCtg
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "使用帮助分类-分页列表查询")
   @ApiOperation(value="使用帮助分类-分页列表查询", notes="使用帮助分类-分页列表查询")
   @GetMapping(value = "/rootList")
   public Result<IPage<HelpCtg>> queryPageList(HelpCtg helpCtg,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       String hasQuery = req.getParameter("hasQuery");
       if(hasQuery != null && "true".equals(hasQuery)){
           QueryWrapper<HelpCtg> queryWrapper =  QueryGenerator.initQueryWrapper(helpCtg, req.getParameterMap());
           List<HelpCtg> list = service.queryTreeListNoPage(queryWrapper);
           IPage<HelpCtg> pageList = new Page<>(1, 10, list.size());
           pageList.setRecords(list);
           return Result.OK(pageList);
       }else{
           Integer parentId = helpCtg.getParentId();
           if (oConvertUtils.isEmpty(parentId)) {
               parentId = 0;
           }
           helpCtg.setParentId(null);
           QueryWrapper<HelpCtg> queryWrapper = QueryGenerator.initQueryWrapper(helpCtg, req.getParameterMap());
           // 使用 eq 防止模糊查询
           queryWrapper.eq("parent_id", parentId);
           Page<HelpCtg> page = new Page<HelpCtg>(pageNo, pageSize);
           IPage<HelpCtg> pageList = service.page(page, queryWrapper);
           return Result.OK(pageList);
       }
   }

    /**
     * 【vue3专用】加载节点的子数据
     *
     * @param pid
     * @return
     */
    @RequestMapping(value = "/loadTreeChildren", method = RequestMethod.GET)
    public Result<List<SelectTreeModel>> loadTreeChildren(@RequestParam(name = "pid") String pid) {
        Result<List<SelectTreeModel>> result = new Result<>();
        try {
            List<SelectTreeModel> ls = service.queryListByPid(Integer.parseInt(pid));
            result.setResult(ls);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 【vue3专用】加载一级节点/如果是同步 则所有数据
     *
     * @param async
     * @param pcode
     * @return
     */
    @RequestMapping(value = "/loadTreeRoot", method = RequestMethod.GET)
    public Result<List<SelectTreeModel>> loadTreeRoot(@RequestParam(name = "async") Boolean async, @RequestParam(name = "pcode") String pcode) {
        Result<List<SelectTreeModel>> result = new Result<>();
        try {
            List<SelectTreeModel> ls = service.queryListByCode(pcode);
            if (!async) {
                loadAllChildren(ls);
            }
            result.setResult(ls);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 【vue3专用】递归求子节点 同步加载用到
     *
     * @param ls
     */
    private void loadAllChildren(List<SelectTreeModel> ls) {
        for (SelectTreeModel tsm : ls) {
            List<SelectTreeModel> temp = service.queryListByPid(Integer.valueOf(tsm.getKey()));
            if (temp != null && !temp.isEmpty()) {
                tsm.setChildren(temp);
                loadAllChildren(temp);
            }
        }
    }

    /**
     * 获取子数据
     * @param helpCtg
     * @param req
     * @return
     */
   //@AutoLog(value = "使用帮助分类-获取子数据")
   @ApiOperation(value="使用帮助分类-获取子数据", notes="使用帮助分类-获取子数据")
   @GetMapping(value = "/childList")
   public Result<IPage<HelpCtg>> queryPageList(HelpCtg helpCtg,HttpServletRequest req) {
       QueryWrapper<HelpCtg> queryWrapper = QueryGenerator.initQueryWrapper(helpCtg, req.getParameterMap());
       List<HelpCtg> list = service.list(queryWrapper);
       IPage<HelpCtg> pageList = new Page<>(1, 10, list.size());
       pageList.setRecords(list);
       return Result.OK(pageList);
   }

   /**
     * 批量查询子节点
     * @param parentIds 父ID（多个采用半角逗号分割）
     * @return 返回 IPage
     * @param parentIds
     * @return
     */
   //@AutoLog(value = "使用帮助分类-批量获取子数据")
   @ApiOperation(value="使用帮助分类-批量获取子数据", notes="使用帮助分类-批量获取子数据")
   @GetMapping("/getChildListBatch")
   public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
       try {
           QueryWrapper<HelpCtg> queryWrapper = new QueryWrapper<>();
           List<String> parentIdList = Arrays.asList(parentIds.split(","));
           queryWrapper.in("parent_id", parentIdList);
           List<HelpCtg> list = service.list(queryWrapper);
           IPage<HelpCtg> pageList = new Page<>(1, 10, list.size());
           pageList.setRecords(list);
           return Result.OK(pageList);
       } catch (Exception e) {
           log.error(e.getMessage(), e);
           return Result.error("批量查询子节点失败：" + e.getMessage());
       }
   }
   
   /**
    *   添加
    *
    * @param helpCtg
    * @return
    */
   @AutoLog(value = "使用帮助分类-添加")
   @ApiOperation(value="使用帮助分类-添加", notes="使用帮助分类-添加")
   @RequiresPermissions("helpCtg:im_help_ctg:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody HelpCtg helpCtg) {
       service.addHelpCtg(helpCtg);
       return Result.OK("添加成功！");
   }
   
   /**
    *  编辑
    *
    * @param helpCtg
    * @return
    */
   @AutoLog(value = "使用帮助分类-编辑")
   @ApiOperation(value="使用帮助分类-编辑", notes="使用帮助分类-编辑")
   @RequiresPermissions("helpCtg:im_help_ctg:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody HelpCtg helpCtg) {
       service.updateHelpCtg(helpCtg);
       return Result.OK("编辑成功!");
   }
   
   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "使用帮助分类-通过id删除")
   @ApiOperation(value="使用帮助分类-通过id删除", notes="使用帮助分类-通过id删除")
   @RequiresPermissions("helpCtg:im_help_ctg:delete")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       service.deleteHelpCtg(Integer.valueOf(id));
       return Result.OK("删除成功!");
   }
   
   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "使用帮助分类-批量删除")
   @ApiOperation(value="使用帮助分类-批量删除", notes="使用帮助分类-批量删除")
   @RequiresPermissions("helpCtg:im_help_ctg:deleteBatch")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.service.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功！");
   }
   
   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "使用帮助分类-通过id查询")
   @ApiOperation(value="使用帮助分类-通过id查询", notes="使用帮助分类-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<HelpCtg> queryById(@RequestParam(name="id",required=true) String id) {
       HelpCtg helpCtg = service.getById(id);
       if(helpCtg==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(helpCtg);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param helpCtg
   */
   @RequiresPermissions("helpCtg:im_help_ctg:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, HelpCtg helpCtg) {
       return super.exportXls(request, helpCtg, HelpCtg.class, "使用帮助分类");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequiresPermissions("helpCtg:im_help_ctg:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, HelpCtg.class);
   }

}
