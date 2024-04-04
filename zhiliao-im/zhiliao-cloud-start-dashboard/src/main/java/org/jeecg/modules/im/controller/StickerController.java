package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.query_helper.QSticker;
import org.jeecg.modules.im.service.StickerService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 贴纸
 */
@RestController
@RequestMapping("/im/sticker")
public class StickerController extends BaseBackController {
    @Resource
    private StickerService stickerService;

    @RequestMapping("/pagination")
    public Result<Object> list(QSticker q){
        q.setServerId(getServerId());
        return success(stickerService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 创建或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated Sticker sticker, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        sticker.setServerId(getServerId());
        return stickerService.createOrUpdate(null,sticker);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(Integer id){
        return success(stickerService.findById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return stickerService.del(ids);
    }


    /**
     * 获取被逻辑删除的贴纸包列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Sticker> logicDeletedUserList = stickerService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的贴纸包
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            stickerService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除贴纸包
     *
     * @param ids 被删除的贴纸包ID，多个id用半角逗号分割
     * @return
     */
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            stickerService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
