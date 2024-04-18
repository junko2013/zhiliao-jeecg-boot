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
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.Contact;
import org.jeecg.modules.im.service.IContactService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 手机通讯录联系人
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Api(tags="手机通讯录联系人")
@RestController
@RequestMapping("/im/contact")
@Slf4j
public class ContactController extends BaseBackController<Contact, IContactService> {

    /**
     * 分页列表查询
     *
     * @param contact
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "手机通讯录联系人-分页列表查询")
    @ApiOperation(value="手机通讯录联系人-分页列表查询", notes="手机通讯录联系人-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Contact>> queryPageList(Contact contact,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        QueryWrapper<Contact> queryWrapper = QueryGenerator.initQueryWrapper(contact, req.getParameterMap());
        Page<Contact> page = new Page<Contact>(pageNo, pageSize);
        IPage<Contact> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param contact
     * @return
     */
    @AutoLog(value = "手机通讯录联系人-添加")
    @ApiOperation(value="手机通讯录联系人-添加", notes="手机通讯录联系人-添加")
    @RequiresPermissions("contact:im_contact:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Contact contact) {
        service.save(contact);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param contact
     * @return
     */
    @AutoLog(value = "手机通讯录联系人-编辑")
    @ApiOperation(value="手机通讯录联系人-编辑", notes="手机通讯录联系人-编辑")
    @RequiresPermissions("contact:im_contact:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Contact contact) {
        service.updateById(contact);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "手机通讯录联系人-通过id删除")
    @ApiOperation(value="手机通讯录联系人-通过id删除", notes="手机通讯录联系人-通过id删除")
    @RequiresPermissions("contact:im_contact:delete")
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
    @AutoLog(value = "手机通讯录联系人-批量删除")
    @ApiOperation(value="手机通讯录联系人-批量删除", notes="手机通讯录联系人-批量删除")
    @RequiresPermissions("contact:im_contact:deleteBatch")
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
    //@AutoLog(value = "手机通讯录联系人-通过id查询")
    @ApiOperation(value="手机通讯录联系人-通过id查询", notes="手机通讯录联系人-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Contact> queryById(@RequestParam(name="id",required=true) String id) {
        Contact contact = service.getById(id);
        if(contact==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(contact);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contact
     */
    @RequiresPermissions("contact:im_contact:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Contact contact) {
        return super.exportXls(request, contact, Contact.class, "手机通讯录联系人");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("contact:im_contact:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Contact.class);
    }

}
