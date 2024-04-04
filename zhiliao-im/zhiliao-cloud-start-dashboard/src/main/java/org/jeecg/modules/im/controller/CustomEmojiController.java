package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.query_helper.QCustomEmoji;
import org.jeecg.modules.im.service.CustomEmojiService;
import org.jeecg.modules.im.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 自定义表情
 */
@RestController
@RequestMapping("/im/customEmoji")
public class CustomEmojiController extends BaseBackController {
    @Resource
    private CustomEmojiService customEmojiService;

    @RequestMapping("/pagination")
    public Result<Object> queryPageList(QCustomEmoji q) {
        return success(customEmojiService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(customEmojiService.getById(id));
    }

    @RequestMapping("/lock")
    public Result<Object> lock(@RequestParam Integer id){
        CustomEmoji emoji = customEmojiService.getById(id);
        if(emoji!=null){
            emoji.setStatus(emoji.getStatus()==1?0:1);
        }
        return success(customEmojiService.updateById(emoji));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return customEmojiService.del(ids);
    }

    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<CustomEmoji> logicDeletedUserList = customEmojiService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            customEmojiService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            customEmojiService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
