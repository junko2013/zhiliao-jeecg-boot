package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Post;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;
import org.jeecg.modules.im.entity.Post;
import org.jeecg.modules.im.entity.Post;
import org.jeecg.modules.im.entity.query_helper.QPost;
import org.jeecg.modules.im.service.PostService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 动态
 */
@RestController
@RequestMapping("/im/post")
public class PostController extends BaseBackController {
    @Resource
    private PostService postService;

    @RequestMapping("/pagination")
    public Result<IPage<Post>> list(Post post, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<Post>> result = new Result<>();
        QueryWrapper<Post> queryWrapper = QueryGenerator.initQueryWrapper(post, req.getParameterMap());
        Page<Post> page = new Page<>(pageNo, pageSize);
        IPage<Post> pageList = postService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    public Result<Object> createOrUpdate(@RequestBody @Validated Post item, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return postService.edit(item);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(postService.getById(id));
    }

    /**
     * 获取被逻辑删除的动态列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Post> logicDeletedUserList = postService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的动态
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            postService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除动态
     *
     * @param ids 被删除的动态ID，多个id用半角逗号分割
     * @return
     */
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            postService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
