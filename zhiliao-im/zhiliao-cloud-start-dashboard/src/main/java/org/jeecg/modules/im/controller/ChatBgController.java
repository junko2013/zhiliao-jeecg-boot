package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.ChatBg;
import org.jeecg.modules.im.service.IChatBgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 聊天背景图
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Api(tags="聊天背景图")
@RestController
@RequestMapping("/chatBg/chatBg")
@Slf4j
public class ChatBgController extends BaseBackController<ChatBg, IChatBgService> {

    /**
     * 分页列表查询
     *
     * @param chatBg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "聊天背景图-分页列表查询")
    @ApiOperation(value="聊天背景图-分页列表查询", notes="聊天背景图-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ChatBg>> queryPageList(ChatBg chatBg,
                                                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                    HttpServletRequest req) {
        QueryWrapper<ChatBg> queryWrapper = QueryGenerator.initQueryWrapper(chatBg, req.getParameterMap());
        Page<ChatBg> page = new Page<ChatBg>(pageNo, pageSize);
        IPage<ChatBg> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param chatBg
     * @return
     */
    @AutoLog(value = "聊天背景图-添加")
    @ApiOperation(value="聊天背景图-添加", notes="聊天背景图-添加")
    @RequiresPermissions("chatBg:im_chat_bg:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ChatBg chatBg) {
        chatBg.setTsCreate(getDate());
        service.save(chatBg);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param chatBg
     * @return
     */
    @AutoLog(value = "聊天背景图-编辑")
    @ApiOperation(value="聊天背景图-编辑", notes="聊天背景图-编辑")
    @RequiresPermissions("chatBg:im_chat_bg:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody ChatBg chatBg) {
        service.updateById(chatBg);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "聊天背景图-通过id删除")
    @ApiOperation(value="聊天背景图-通过id删除", notes="聊天背景图-通过id删除")
    @RequiresPermissions("chatBg:im_chat_bg:delete")
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
    @AutoLog(value = "聊天背景图-批量删除")
    @ApiOperation(value="聊天背景图-批量删除", notes="聊天背景图-批量删除")
    @RequiresPermissions("chatBg:im_chat_bg:deleteBatch")
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
    //@AutoLog(value = "聊天背景图-通过id查询")
    @ApiOperation(value="聊天背景图-通过id查询", notes="聊天背景图-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ChatBg> queryById(@RequestParam(name="id",required=true) String id) {
        ChatBg chatBg = service.getById(id);
        if(chatBg==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(chatBg);
    }

    /**
     * 获取被逻辑删除的聊天背景图列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<org.jeecg.modules.im.entity.ChatBg> logicDeletedUserList = service.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的聊天背景图
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            service.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除聊天背景图
     *
     * @param ids 被删除的聊天背景图ID，多个id用半角逗号分割
     * @return
     */
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            service.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param chatBg
     */
    @RequiresPermissions("chatBg:im_chat_bg:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ChatBg chatBg) {
        return super.exportXls(request, chatBg, ChatBg.class, "聊天背景图");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("chatBg:im_chat_bg:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ChatBg.class);
    }

}
