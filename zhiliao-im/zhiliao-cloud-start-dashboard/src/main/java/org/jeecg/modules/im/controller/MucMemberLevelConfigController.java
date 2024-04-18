package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.MucMemberLevelConfig;
import org.jeecg.modules.im.service.IMucMemberLevelConfigService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * 群组成员等级配置
 */
@RestController
@RequestMapping("/im/mucMemberLevel/config")
public class MucMemberLevelConfigController extends BaseBackController<MucMemberLevelConfig, IMucMemberLevelConfigService> {

    /**
     * 分页列表查询
     *
     * @param mucMemberLevelConfig
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "群聊成员等级配置-分页列表查询")
    @ApiOperation(value="群聊成员等级配置-分页列表查询", notes="群聊成员等级配置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MucMemberLevelConfig>> queryPageList(MucMemberLevelConfig mucMemberLevelConfig,
                                                                                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                                                HttpServletRequest req) {
        QueryWrapper<MucMemberLevelConfig> queryWrapper = QueryGenerator.initQueryWrapper(mucMemberLevelConfig, req.getParameterMap());
        Page<MucMemberLevelConfig> page = new Page<MucMemberLevelConfig>(pageNo, pageSize);
        IPage<MucMemberLevelConfig> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param mucMemberLevelConfig
     * @return
     */
    @AutoLog(value = "群聊成员等级配置-添加")
    @ApiOperation(value="群聊成员等级配置-添加", notes="群聊成员等级配置-添加")
    @RequiresPermissions("mucMemberLevelConfig:im_muc_member_level_config:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody MucMemberLevelConfig mucMemberLevelConfig) {
        service.save(mucMemberLevelConfig);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param mucMemberLevelConfig
     * @return
     */
    @AutoLog(value = "群聊成员等级配置-编辑")
    @ApiOperation(value="群聊成员等级配置-编辑", notes="群聊成员等级配置-编辑")
    @RequiresPermissions("mucMemberLevelConfig:im_muc_member_level_config:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody MucMemberLevelConfig mucMemberLevelConfig) {
        service.updateById(mucMemberLevelConfig);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "群聊成员等级配置-通过id删除")
    @ApiOperation(value="群聊成员等级配置-通过id删除", notes="群聊成员等级配置-通过id删除")
    @RequiresPermissions("mucMemberLevelConfig:im_muc_member_level_config:delete")
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
    @AutoLog(value = "群聊成员等级配置-批量删除")
    @ApiOperation(value="群聊成员等级配置-批量删除", notes="群聊成员等级配置-批量删除")
    @RequiresPermissions("mucMemberLevelConfig:im_muc_member_level_config:deleteBatch")
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
    //@AutoLog(value = "群聊成员等级配置-通过id查询")
    @ApiOperation(value="群聊成员等级配置-通过id查询", notes="群聊成员等级配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MucMemberLevelConfig> queryById(@RequestParam(name="id",required=true) String id) {
        MucMemberLevelConfig mucMemberLevelConfig = service.getById(id);
        if(mucMemberLevelConfig==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mucMemberLevelConfig);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mucMemberLevelConfig
     */
    @RequiresPermissions("mucMemberLevelConfig:im_muc_member_level_config:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MucMemberLevelConfig mucMemberLevelConfig) {
        return super.exportXls(request, mucMemberLevelConfig, MucMemberLevelConfig.class, "群聊成员等级配置");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("mucMemberLevelConfig:im_muc_member_level_config:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MucMemberLevelConfig.class);
    }
}
