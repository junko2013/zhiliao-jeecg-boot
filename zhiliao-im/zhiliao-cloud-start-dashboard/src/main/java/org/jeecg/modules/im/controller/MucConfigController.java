package org.jeecg.modules.im.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.im.entity.MucConfig;
import org.jeecg.modules.im.service.IMucConfigService;
import org.springframework.web.bind.annotation.*;


/**
 * 群组配置
 */
@RestController
@RequestMapping("/im/mucConfig")
public class MucConfigController extends BaseBackController<MucConfig, IMucConfigService> {

    /**
     * 默认配置
     */
    @RequestMapping("/default")
    public Result<Object> detail(){
        return success(service.findDefault());
    }

    /**
     *  编辑
     *
     * @param mucConfig
     * @return
     */
    @AutoLog(value = "群聊配置-编辑")
    @ApiOperation(value="群聊配置-编辑", notes="群聊配置-编辑")
    @RequiresPermissions("mucConfig:im_muc_config:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody MucConfig mucConfig) {
        service.updateById(mucConfig);
        return Result.OK("编辑成功!");
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "群聊配置-通过id查询")
    @ApiOperation(value="群聊配置-通过id查询", notes="群聊配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MucConfig> queryById(@RequestParam(name="id",required=true) String id) {
        MucConfig mucConfig = service.getById(id);
        if(mucConfig==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mucConfig);
    }
}
