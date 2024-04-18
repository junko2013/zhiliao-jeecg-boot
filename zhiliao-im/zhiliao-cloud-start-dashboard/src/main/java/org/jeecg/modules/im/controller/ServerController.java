package org.jeecg.modules.im.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.anotation.NoNeedServerSet;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecg.modules.im.entity.query_helper.QServer;
import org.jeecg.modules.im.service.IServerConfigService;
import org.jeecg.modules.im.service.IServerService;
import org.jeecg.modules.im.vo.ServerPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/im/server")
public class ServerController extends BaseBackController<Server, IServerService> {
    @Autowired
    private IServerConfigService serverConfigService;

    @Resource
    private ISysBaseAPI sysBaseAPI;

    @NoNeedServerSet
    //获取租户的服务器列表
    @RequestMapping("/getCurrentTenantServers")
    public Result<Object> getCurrentTenantServers(){
        QServer q = new QServer();
        //如果不是超管，查询当前租户下的服务器
        if(!sysBaseAPI.getUserRoleSet(getCurrentAdminUserName()).contains("admin")){
            q.setTenantId(getTenantId());
        }
        return success(service.findAll(q));
    }
    @NoNeedServerSet
    @RequestMapping("/pagination")
    public Result<Object> list(QServer q){
        if(!sysBaseAPI.getUserRoleSet(getCurrentAdminUserName()).contains("admin")){
            q.setTenantId(getTenantId());
        }
        return success(service.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }


    /**
     * 分页列表查询
     *
     * @param server
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "im_server-分页列表查询")
    @ApiOperation(value="im_server-分页列表查询", notes="im_server-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Server>> queryPageList(Server server,
                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                               HttpServletRequest req) {
        if(!sysBaseAPI.getUserRoleSet(getCurrentAdminUserName()).contains("admin")){
            server.setTenantId(getTenantId());
        }
        QueryWrapper<Server> queryWrapper = QueryGenerator.initQueryWrapper(server, req.getParameterMap());
        Page<Server> page = new Page<Server>(pageNo, pageSize);
        IPage<Server> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param serverPage
     * @return
     */
    @AutoLog(value = "im_server-添加")
    @ApiOperation(value="im_server-添加", notes="im_server-添加")
    @RequiresPermissions("server:im_server:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ServerPage serverPage) {
        Server server = new Server();
        BeanUtils.copyProperties(serverPage, server);
        service.saveMain(server, serverPage.getServerConfigList());
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param serverPage
     * @return
     */
    @AutoLog(value = "im_server-编辑")
    @ApiOperation(value="im_server-编辑", notes="im_server-编辑")
    @RequiresPermissions("server:im_server:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody ServerPage serverPage) {
        Server server = new Server();
        BeanUtils.copyProperties(serverPage, server);
        Server serverEntity = service.getById(server.getId());
        if(serverEntity==null) {
            return Result.error("未找到对应数据");
        }
        service.updateMain(server, serverPage.getServerConfigList());
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "im_server-通过id删除")
    @ApiOperation(value="im_server-通过id删除", notes="im_server-通过id删除")
    @RequiresPermissions("server:im_server:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        service.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "im_server-批量删除")
    @ApiOperation(value="im_server-批量删除", notes="im_server-批量删除")
    @RequiresPermissions("server:im_server:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.service.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "im_server-通过id查询")
    @ApiOperation(value="im_server-通过id查询", notes="im_server-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Server> queryById(@RequestParam(name="id",required=true) String id) {
        Server server = service.getById(id);
        if(server==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(server);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "服务器配置通过主表ID查询")
    @ApiOperation(value="服务器配置主表ID查询", notes="服务器配置-通主表ID查询")
    @GetMapping(value = "/queryServerConfigByMainId")
    public Result<List<ServerConfig>> queryServerConfigListByMainId(@RequestParam(name="id",required=true) String id) {
        List<ServerConfig> serverConfigList = serverConfigService.selectByMainId(id);
        return Result.OK(serverConfigList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param server
     */
    @RequiresPermissions("server:im_server:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Server server) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<Server> queryWrapper = QueryGenerator.initQueryWrapper(server, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //配置选中数据查询条件
        String selections = request.getParameter("selections");
        if(oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id",selectionList);
        }
        //Step.2 获取导出数据
        List<Server> serverList = service.list(queryWrapper);

        // Step.3 组装pageList
        List<ServerPage> pageList = new ArrayList<ServerPage>();
        for (Server main : serverList) {
            ServerPage vo = new ServerPage();
            BeanUtils.copyProperties(main, vo);
            List<ServerConfig> serverConfigList = serverConfigService.selectByMainId(main.getId()+"");
            vo.setServerConfigList(serverConfigList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "im_server列表");
        mv.addObject(NormalExcelConstants.CLASS, ServerPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("im_server数据", "导出人:"+sysUser.getRealname(), "im_server"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("server:im_server:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ServerPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ServerPage.class, params);
                for (ServerPage page : list) {
                    Server po = new Server();
                    BeanUtils.copyProperties(page, po);
                    service.saveMain(po, page.getServerConfigList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("文件导入失败:"+e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

}
