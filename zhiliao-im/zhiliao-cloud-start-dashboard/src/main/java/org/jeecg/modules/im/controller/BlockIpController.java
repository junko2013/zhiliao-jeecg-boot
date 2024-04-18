package org.jeecg.modules.im.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.BlockIp;
import org.jeecg.modules.im.entity.query_helper.QBlockIp;
import org.jeecg.modules.im.service.IBlockIpService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/im/blockIp")
public class BlockIpController extends BaseBackController<BlockIp, IBlockIpService> {

    /**
     * 分页列表查询
     *
     * @param blockIp
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "ip黑名单-分页列表查询")
    @ApiOperation(value="ip黑名单-分页列表查询", notes="ip黑名单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<BlockIp>> queryPageList(BlockIp blockIp,
                                                                                      @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                      HttpServletRequest req) {
        QueryWrapper<BlockIp> queryWrapper = QueryGenerator.initQueryWrapper(blockIp, req.getParameterMap());
        queryWrapper.eq("server_id",getServerId());
        Page<BlockIp> page = new Page<BlockIp>(pageNo, pageSize);
        IPage<BlockIp> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param blockIp
     * @return
     */
    @AutoLog(value = "ip黑名单-添加")
    @ApiOperation(value="ip黑名单-添加", notes="ip黑名单-添加")
    @RequiresPermissions("blockIp:im_block_ip:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody BlockIp blockIp) {
        blockIp.setServerId(getServer().getId());
        service.save(blockIp);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param blockIp
     * @return
     */
    @AutoLog(value = "ip黑名单-编辑")
    @ApiOperation(value="ip黑名单-编辑", notes="ip黑名单-编辑")
    @RequiresPermissions("blockIp:im_block_ip:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody BlockIp blockIp) {
        service.updateById(blockIp);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "ip黑名单-通过id删除")
    @ApiOperation(value="ip黑名单-通过id删除", notes="ip黑名单-通过id删除")
    @RequiresPermissions("blockIp:im_block_ip:delete")
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
    @AutoLog(value = "ip黑名单-批量删除")
    @ApiOperation(value="ip黑名单-批量删除", notes="ip黑名单-批量删除")
    @RequiresPermissions("blockIp:im_block_ip:deleteBatch")
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
    //@AutoLog(value = "ip黑名单-通过id查询")
    @ApiOperation(value="ip黑名单-通过id查询", notes="ip黑名单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<BlockIp> queryById(@RequestParam(name="id",required=true) String id) {
        BlockIp blockIp = service.getById(id);
        if(blockIp==null) {
            return Result.error("未找到对应数据");
        }
        if(blockIp.getType().equals(BlockIp.Type.区间.name())){
            blockIp.setIp1(IPUtil.longToIP(blockIp.getNum1()));
            blockIp.setIp2(IPUtil.longToIP(blockIp.getNum2()));
        }
        return Result.OK(blockIp);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param blockIp
     */
    @RequiresPermissions("blockIp:im_block_ip:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BlockIp blockIp) {
        return super.exportXls(request, blockIp, BlockIp.class, "ip黑名单");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("blockIp:im_block_ip:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BlockIp.class);
    }
}
