package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.query_helper.QMuc;
import org.jeecg.modules.im.service.IMucService;
import org.jeecg.modules.im.service.IXMPPService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 群组
 */
@RestController
@RequestMapping("/a/muc")
public class MucCtrl extends BaseApiCtrl {
    @Resource
    private IMucService IMucService;
    @Resource
    private IXMPPService IXMPPService;

    /**
     * 新建
     */
    @PostMapping("/create")
    public Result<Object> create(@RequestParam String name,
                              @RequestParam(required = false) String info,
                              @RequestParam(required = false) String subject,
                              @RequestParam(required = false) String avatar,
                              @RequestParam(required = false) String smallAvatar,
                              @RequestParam(required = false) String inviteAccounts){
        Muc temp = new Muc();
        temp.setUserId(getCurrentUserId());
        temp.setQrCode(UUIDTool.getUUID());
        temp.setName(name);
        temp.setAvatar(avatar);
        temp.setSmallAvatar(smallAvatar);
        temp.setName(name);
        temp.setInfo(info);
        temp.setSubject(subject);
        temp.setTsCreate(getDate());
        temp.setServerId(getServerId());

        Result result = IMucService.create(temp,inviteAccounts);
        if(result.isSuccess()){
            Muc muc = (Muc) result.getResult();
            //群主发送群聊已创建的消息
            MessageBean messageBean = new MessageBean();
            messageBean.setMucId(muc.getId());
            messageBean.setType(MsgType.mucCreate.getType());
            messageBean.setContent(JSONObject.toJSONString(muc));
            IXMPPService.sendMucMsg(messageBean);
        }
        return result;
    }
    /**
     * 我所有的
     */
    @PostMapping("/myAll")
    public Result<Object> myAll(){
        return IMucService.findMyAll(getCurrentUserId());
    }
    /**
     * 获取用户的某个群
     */
    @PostMapping("/getOneOfMy")
    public Result<Object> getOneOfMy(Integer id){
        return success(IMucService.findByIdOfUser(id,getCurrentUserId()));
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(){
        return success();
    }
    /**
     * 加入
     */
    @PostMapping("/join")
    public Result<Object> join(){
        return success();
    }
    /**
     * 退出
     */
    @PostMapping("/leave")
    public Result<Object> leave(){
        return success();
    }
    /**
     * 列表
     */
    @PostMapping("/list")
    public Result<Object> list(){
        return success();
    }
    /**
     * 设置、取消管理员
     * flag:1=设置 0=取消
     */
    @PostMapping("/setManagers")
    public Result<Object> setManagers(Integer id,String memberIds,Integer flag){
        return IMucService.setManagers(getCurrentUserId(),id,memberIds,flag);
    }
    /**
     * 更新二维码
     */
    @PostMapping("/updateQrcode")
    public Result<Object> updateQrcode(Muc muc){
        return IMucService.updateQrcode(getCurrentUserId(),muc);
    }
    //更新群组名称
    @PostMapping("/updateName")
    public Result<Object> updateName(Muc muc){
        return IMucService.updateName(getCurrentUserId(),muc);
    }
    //更新群组简介
    @PostMapping("/updateInfo")
    public Result<Object> updateInfo(Muc muc){
        return IMucService.updateInfo(getCurrentUserId(),muc);
    }
    //更新群组欢迎语
    @PostMapping("/updateWelcomes")
    public Result<Object> updateWelcomes(QMuc temp){
        return IMucService.updateWelcomes(getCurrentUserId(),temp);
    }
    //更新群组头像
    @PostMapping("/updateAvatar")
    public Result<Object> updateAvatar(Muc muc){
        return IMucService.updateAvatar(getCurrentUserId(),muc);
    }
}
