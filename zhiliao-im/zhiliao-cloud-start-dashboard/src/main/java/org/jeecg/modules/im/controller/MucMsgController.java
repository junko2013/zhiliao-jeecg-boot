package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMsg;
import org.jeecg.modules.im.entity.query_helper.QMucMsg;
import org.jeecg.modules.im.service.IMucMsgService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@RestController
@RequestMapping("/im/mucMsg")
public class MucMsgController extends BaseBackController<MucMsg, IMucMsgService> {

    /**
     * 分页列表查询
     *
     * @param mucMsg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "群聊消息-分页列表查询")
    @ApiOperation(value="群聊消息-分页列表查询", notes="群聊消息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MucMsg>> queryPageList(MucMsg mucMsg,
                                                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                    HttpServletRequest req) {
        QueryWrapper<MucMsg> queryWrapper = QueryGenerator.initQueryWrapper(mucMsg, req.getParameterMap());
        queryWrapper.eq("server_id",getServerId());
        Page<MucMsg> page = new Page<MucMsg>(pageNo, pageSize);
        IPage<MucMsg> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "群聊消息-通过id删除")
    @ApiOperation(value="群聊消息-通过id删除", notes="群聊消息-通过id删除")
    @RequiresPermissions("mucMsg:im_muc_msg:delete")
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
    @AutoLog(value = "群聊消息-批量删除")
    @ApiOperation(value="群聊消息-批量删除", notes="群聊消息-批量删除")
    @RequiresPermissions("mucMsg:im_muc_msg:deleteBatch")
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
    //@AutoLog(value = "群聊消息-通过id查询")
    @ApiOperation(value="群聊消息-通过id查询", notes="群聊消息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MucMsg> queryById(@RequestParam(name="id",required=true) String id) {
        MucMsg mucMsg = service.getById(id);
        if(mucMsg==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mucMsg);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mucMsg
     */
    @RequiresPermissions("mucMsg:im_muc_msg:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MucMsg mucMsg) {
        return super.exportXls(request, mucMsg, MucMsg.class, "群聊消息");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("mucMsg:im_muc_msg:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MucMsg.class);
    }

}
