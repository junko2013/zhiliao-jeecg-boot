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
import org.jeecg.modules.im.entity.ClientVer;
import org.jeecg.modules.im.service.IClientVerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Api(tags="客户端版本")
@Slf4j
@RestController
@RequestMapping("/im/clientVer")
public class ClientVerController extends BaseBackController<ClientVer, IClientVerService> {

    /**
     * 分页列表查询
     *
     * @param clientVer
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "客户端版本-分页列表查询")
    @ApiOperation(value="客户端版本-分页列表查询", notes="客户端版本-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ClientVer>> queryPageList(ClientVer clientVer,
                                                                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                          HttpServletRequest req) {
        QueryWrapper<ClientVer> queryWrapper = QueryGenerator.initQueryWrapper(clientVer, req.getParameterMap());
        Page<ClientVer> page = new Page<ClientVer>(pageNo, pageSize);
        IPage<ClientVer> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param clientVer
     * @return
     */
    @AutoLog(value = "客户端版本-添加")
    @ApiOperation(value="客户端版本-添加", notes="客户端版本-添加")
    @RequiresPermissions("clientVer:im_client_ver:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ClientVer clientVer) {
        service.save(clientVer);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param clientVer
     * @return
     */
    @AutoLog(value = "客户端版本-编辑")
    @ApiOperation(value="客户端版本-编辑", notes="客户端版本-编辑")
    @RequiresPermissions("clientVer:im_client_ver:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody ClientVer clientVer) {
        service.updateById(clientVer);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客户端版本-通过id删除")
    @ApiOperation(value="客户端版本-通过id删除", notes="客户端版本-通过id删除")
    @RequiresPermissions("clientVer:im_client_ver:delete")
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
    @AutoLog(value = "客户端版本-批量删除")
    @ApiOperation(value="客户端版本-批量删除", notes="客户端版本-批量删除")
    @RequiresPermissions("clientVer:im_client_ver:deleteBatch")
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
    //@AutoLog(value = "客户端版本-通过id查询")
    @ApiOperation(value="客户端版本-通过id查询", notes="客户端版本-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ClientVer> queryById(@RequestParam(name="id",required=true) String id) {
        ClientVer clientVer = service.getById(id);
        if(clientVer==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(clientVer);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param clientVer
     */
    @RequiresPermissions("clientVer:im_client_ver:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ClientVer clientVer) {
        return super.exportXls(request, clientVer, ClientVer.class, "客户端版本");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("clientVer:im_client_ver:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ClientVer.class);
    }
}
