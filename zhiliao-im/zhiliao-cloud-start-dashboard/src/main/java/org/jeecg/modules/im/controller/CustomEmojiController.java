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
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.query_helper.QCustomEmoji;
import org.jeecg.modules.im.service.ICustomEmojiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 自定义表情
 */
@Api(tags="自定义表情")
@RestController
@RequestMapping("/im/customEmoji")
@Slf4j
public class CustomEmojiController extends BaseBackController<CustomEmoji, ICustomEmojiService> {

    /**
     * 分页列表查询
     *
     * @param customEmoji
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "自定义表情-分页列表查询")
    @ApiOperation(value="自定义表情-分页列表查询", notes="自定义表情-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<CustomEmoji>> queryPageList(CustomEmoji customEmoji,
                                                                                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                              HttpServletRequest req) {
        QueryWrapper<CustomEmoji> queryWrapper = QueryGenerator.initQueryWrapper(customEmoji, req.getParameterMap());
        Page<CustomEmoji> page = new Page<CustomEmoji>(pageNo, pageSize);
        IPage<CustomEmoji> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param customEmoji
     * @return
     */
    @AutoLog(value = "自定义表情-添加")
    @ApiOperation(value="自定义表情-添加", notes="自定义表情-添加")
    @RequiresPermissions("customEmoji:im_custom_emoji:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody CustomEmoji customEmoji) {
        service.save(customEmoji);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param customEmoji
     * @return
     */
    @AutoLog(value = "自定义表情-编辑")
    @ApiOperation(value="自定义表情-编辑", notes="自定义表情-编辑")
    @RequiresPermissions("customEmoji:im_custom_emoji:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody CustomEmoji customEmoji) {
        service.updateById(customEmoji);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "自定义表情-通过id删除")
    @ApiOperation(value="自定义表情-通过id删除", notes="自定义表情-通过id删除")
    @RequiresPermissions("customEmoji:im_custom_emoji:delete")
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
    @AutoLog(value = "自定义表情-批量删除")
    @ApiOperation(value="自定义表情-批量删除", notes="自定义表情-批量删除")
    @RequiresPermissions("customEmoji:im_custom_emoji:deleteBatch")
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
    //@AutoLog(value = "自定义表情-通过id查询")
    @ApiOperation(value="自定义表情-通过id查询", notes="自定义表情-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<CustomEmoji> queryById(@RequestParam(name="id",required=true) String id) {
        CustomEmoji customEmoji = service.getById(id);
        if(customEmoji==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(customEmoji);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param customEmoji
     */
    @RequiresPermissions("customEmoji:im_custom_emoji:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CustomEmoji customEmoji) {
        return super.exportXls(request, customEmoji, CustomEmoji.class, "自定义表情");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("customEmoji:im_custom_emoji:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CustomEmoji.class);
    }

    @RequiresPermissions("customEmoji:im_custom_emoji:lock")
    @RequestMapping("/lock")
    public Result<Object> lock(@RequestParam Integer id){
        CustomEmoji emoji = service.getById(id);
        if(emoji!=null){
            emoji.setEnable(!emoji.getEnable());
        }
        return success(service.updateById(emoji));
    }


    @RequiresPermissions("customEmoji:im_custom_emoji:recycleBin")
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<CustomEmoji> logicDeletedUserList = service.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    @RequiresPermissions("customEmoji:im_custom_emoji:putRecycleBin")
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            service.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    @RequiresPermissions("customEmoji:im_custom_emoji:deleteRecycleBin")
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            service.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
