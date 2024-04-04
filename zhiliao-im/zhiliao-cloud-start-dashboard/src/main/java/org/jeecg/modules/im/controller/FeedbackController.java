package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Feedback;
import org.jeecg.modules.im.entity.Font;
import org.jeecg.modules.im.entity.query_helper.QFeedback;
import org.jeecg.modules.im.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 意见反馈
 */
@RestController
@RequestMapping("/im/feedback")
public class FeedbackController extends BaseBackController {
    @Resource
    private FeedbackService feedbackService;

    @RequestMapping("/pagination")
    public Result<Object> pagination(QFeedback q) {
        return success(feedbackService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }


    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        Feedback feedback = feedbackService.getById(id);
        feedback.setIsDeal(feedback.getTsDeal()>0);
        return success();
    }

    @RequestMapping("/deal")
    public Result<Object> update(@RequestBody QFeedback q){
        Feedback feedback = feedbackService.getById(q.getId());
        if(feedback==null||feedback.getDelFlag()==1){
            return fail();
        }
        if(q.getIsDeal()){
            feedback.setTsDeal(getTs());
        }else{
            feedback.setTsDeal(0L);
        }
        feedback.setReply(q.getReply());
        return success(feedbackService.updateById(feedback));
    }


    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return feedbackService.del(ids);
    }
    /**
     * 获取被逻辑删除的帮助列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Feedback> logicDeletedUserList = feedbackService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的帮助
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            feedbackService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除帮助
     *
     * @param ids 被删除的帮助ID，多个id用半角逗号分割
     * @return
     */
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            feedbackService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
