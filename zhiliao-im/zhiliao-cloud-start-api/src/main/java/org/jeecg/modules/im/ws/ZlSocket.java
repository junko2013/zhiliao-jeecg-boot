package org.jeecg.modules.im.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.java.emoji.EmojiConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.boot.starter.rabbitmq.client.RabbitMqClient;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.*;
import org.jeecg.modules.im.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * 用于数据同步等一些不需要经过openfire的
 * 同时优化http传输带来的瓶颈
 * 一次性发送数据量太大会导致连接断开，尤其是查询列表数据时
 */
@Slf4j
@Component
@ServerEndpoint("/a/ws/{userId}/{deviceId}/{token}")
public class ZlSocket {
    @Getter
    private Session session;
    @Resource
    private UserService userService;
    @Resource
    private FriendService friendService;
    @Resource
    private MucService mucService;
    @Resource
    private MucInviteService mucInviteService;
    @Resource
    private MucMsgService mucMsgService;
    @Resource
    private MucMsgReadService readService;
    @Resource
    private MsgService msgService;
    @Resource
    private MucMemberService mucMemberService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private FeedbackTypeService feedbackTypeService;
    @Resource
    private ChatBgService chatBgService;
    @Resource
    private LinkService linkService;
    @Resource
    private LocaleService localeService;
    @Resource
    private SignInService signInService;
    @Resource
    private MucInviteLinkService mucInviteLinkService;
    @Resource
    private HelpsService helpsService;
    @Resource
    private CustomEmojiService customEmojiService;
    @Resource
    private CallService callService;
    @Resource
    private GifService gifService;
    @Resource
    private PostService postService;
    @Resource
    private RedPackService redPackService;
    @Resource
    private StickerService stickerService;
    private final EmojiConverter emojiConverter = EmojiConverter.getInstance();

    @Autowired
    private RabbitMqClient rabbitMqClient;

    private static ZlSocket zlSocket;

    //service是单例的，websocket是多例的，所以需要依赖静态对象
    @PostConstruct
    public void init(){
        zlSocket = this;
    }

    /**
     * 当前用户id
     */
    private Integer userId;
    private String token;
    /**
     * 设备id，用于标识同一用户，不同设备的标识
     */
    private String deviceId;
    /**
     * 当前socket唯一id
     */
    private String socketId;

    /**
     * 用户连接池，包含单个用户的所有socket连接；
     * 因为一个用户同时会登录多个设备，就会有多个连接；
     * key是userId，value是Map对象；子Map的key是deviceId，value是ZlSocket对象
     */
    private static Map<Integer, Map<String, ZlSocket>> userPool = new HashMap<>();
    /**
     * 连接池，包含所有WebSocket连接；
     * key是socketId，value是ZlSocket对象
     */
    private static Map<String, ZlSocket> socketPool = new HashMap<>();

    /**
     * 获取某个用户所有的设备
     */
    public static Map<String, ZlSocket> getUserPool(Integer userId) {
        return userPool.computeIfAbsent(userId, k -> new HashMap<>(5));
    }

    /**
     * 向当前用户发送消息
     *
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        try {
            synchronized (this) {
                this.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("【ZlSocket】消息发送失败：" + e.getMessage());
        }
//        Kv kv = Kv.by("userId",userId).set("deviceId",deviceId).set("msg",message);
//        zlSocket.rabbitMqClient.sendMessage(ConstantMQ.DATA_SYNC,JSON.toJSONString(kv));
    }

    /**
     * 向指定用户的所有页面发送消息
     *
     * @param userId  接收消息的用户ID
     * @param message 消息内容
     */
    public static void sendMessageTo(Integer userId, String message) {
        Collection<ZlSocket> values = getUserPool(userId).values();
        if (!values.isEmpty()) {
            for (ZlSocket socket : values) {
                socket.sendMessage(message);
            }
        } else {
            log.warn("【ZlSocket】消息发送失败：userId\"" + userId + "\"不存在或未在线！");
        }
    }

    /**
     * 向指定用户的指定页面发送消息
     *
     * @param userId  接收消息的用户ID
     * @param message 消息内容
     */
    public static void sendMessageTo(Integer userId, String deviceId, String message) {
        ZlSocket socket = getUserPool(userId).get(deviceId);
        if (socket != null) {
            socket.sendMessage(message);
        } else {
            log.warn("【ZlSocket】消息发送失败：userId\"" + userId + "\"的deviceId\"" + deviceId + "\"不存在或未在线！");
        }
    }

    /**
     * 向多个用户的所有页面发送消息
     *
     * @param userIds 接收消息的用户ID数组
     * @param message 消息内容
     */
    public static void sendMessageTo(Integer[] userIds, String message) {
        for (Integer userId : userIds) {
            ZlSocket.sendMessageTo(userId, message);
        }
    }

    /**
     * 向所有用户的所有页面发送消息
     *
     * @param message 消息内容
     */
    public static void sendMessageToAll(String message) {
        for (ZlSocket socket : socketPool.values()) {
            socket.sendMessage(message);
        }
    }

    /**
     * websocket 开启连接
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId, @PathParam("deviceId") String deviceId,@PathParam("token") String token) {
        //校验token
        if(JwtUtilApp.verify(token)==null){
//            sendMessageTo(userId,JSON.toJSONString(Result.error("token invalid")));
            return;
        }
        try {
            this.userId = userId;
            this.deviceId = deviceId;
            this.token = token;
            this.socketId = userId + deviceId;
            this.session = session;

            socketPool.put(this.socketId, this);
            getUserPool(userId).put(this.deviceId, this);

            log.info("【ZlSocket】有新的连接，总数为:" + socketPool.size());
        } catch (Exception ignored) {
        }
    }

    /**
     * websocket 断开连接
     */
    @OnClose
    public void onClose() {
        try {
            socketPool.remove(this.socketId);
            getUserPool(this.userId).remove(this.deviceId);

            log.info("【ZlSocket】连接断开，总数为:" + socketPool.size());
        } catch (Exception ignored) {
        }
    }

    /**
     * websocket 收到消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【ZlSocket】onMessage:" + message);
        //校验token
        if(JwtUtilApp.verify(token)==null){
            sendMessageTo(userId,JSON.toJSONString(Result.error("token invalid")));
            return;
        }
        JSONObject json;
        try {
            json = JSON.parseObject(message);
            String module = json.getString("m");
            String type = json.getString("t");
            switch (module) {
                case "user": {
                    dealUser(type,json);
                    break;
                }
                case "msg": {
                    dealMsg(type,json);
                    break;
                }
                case "muc": {
                    dealMuc(type,json);
                    break;
                }
                case "mucMsg": {
                    dealMucMsg(type,json);
                    break;
                }
                case "mucInvite": {
                    dealMucInvite(type,json);
                    break;
                }
                case "mucInviteLink": {
                    dealMucInviteLink(type,json);
                    break;
                }
                case "mucMember": {
                    dealMucMember(type,json);
                    break;
                }
                case "device": {
                    dealDevice(type,json);
                    break;
                }
                case "friend": {
                    dealFriend(type,json);
                    break;
                }
                case "feedback": {
                    dealFeedback(type,json);
                    break;
                }
                case "chatBg": {
                    dealChatBg(type,json);
                    break;
                }
                case "link": {
                    dealLink(type,json);
                    break;
                }
                case "locale": {
                    dealLocale(type,json);
                    break;
                }
                case "signIn": {
                    dealSingIn(type,json);
                    break;
                }
                case "gif": {
                    dealGif(type,json);
                    break;
                }
                case "call": {
                    dealCall(type,json);
                    break;
                }
                case "customEmoji": {
                    dealCustomEmoji(type,json);
                    break;
                }
                case "helps": {
                    dealHelps(type,json);
                    break;
                }
                case "sticker": {
                    dealSticker(type,json);
                    break;
                }

                default:
                    log.warn("【ZlSocket】收到不识别的消息类型:" + type);
                    break;
            }
        } catch (Exception e) {
            log.error("【ZlSocket】收到不合法的消息:{}" ,message,e);
        }
    }

    // 连接异常
    @OnError
    public void onError(Throwable throwable) throws IOException {

        log.info("[websocket] 连接异常：id={}，throwable={}", this.session.getId(), throwable.getMessage());

        // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
        this.session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }

    //用户的
    private void dealUser(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","user").set("t",type);
        if ("info".equals(type)) {
            kv.set("data",zlSocket.userService.getBasicInfoById(userId));
            result = Result.ok(kv);
        }else if ("getById".equals(type)) {
            kv.set("data",zlSocket.userService.getById(json.getInteger("id")));
            result = Result.ok(kv);
        }else{
            result = Result.error("unknown user option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //好友
    private void dealFriend(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","friend").set("t",type);
        if ("all".equals(type)) {
            QFriend q = new QFriend();
            q.setUserId(userId);
            kv.set("data",zlSocket.friendService.findAll(q));
            result = Result.ok(kv);
        }else if ("one".equals(type)) {
            kv.set("data", zlSocket.friendService.findOne(userId,json.getInteger("id")));
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown friend option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //群聊
    private void dealMuc(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","muc").set("t",type);
        if ("all".equals(type)) {
            kv.set("data",zlSocket.mucService.findMyAll(userId));
            result = Result.ok(kv);
        }else if ("one".equals(type)) {
            kv.set("data", zlSocket.mucService.findByIdOfUser(userId,json.getInteger("id")));
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown muc option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //群聊邀请记录
    private void dealMucInvite(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","mucInvite").set("t",type);
        if ("pagination".equals(type)) {
            QMucInvite q = new QMucInvite();
            q.setMucId(json.getInteger("mucId"));
            Integer page = json.getInteger("page");
            Integer size = json.getInteger("size");
            kv.set("data",zlSocket.mucInviteService.paginationApi(new MyPage<>(page,size),q));
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown mucInvite option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //群聊邀请链接
    private void dealMucInviteLink(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","mucInviteLink").set("t",type);
        if ("all".equals(type)) {
            kv.set("data",zlSocket.mucInviteLinkService.findByMuc(json.getInteger("mucId")));
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown mucInviteLink option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //群聊成员
    private void dealMucMember(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","mucMember").set("t",type);
        //指定单个或多个群的成员
        if ("pagination".equals(type)) {
            QMucMember q = new QMucMember();
            q.setStatus(MucMember.Status.normal.getCode());
            Integer page = json.getInteger("page");
            Integer size = json.getInteger("size");
            if(StringUtils.isNotBlank(q.getMucIds())){
                Kv data = Kv.create();
                for (String mucId : StringUtils.split(q.getMucIds(),";")) {
                    if(StringUtils.isBlank(mucId)){
                        continue;
                    }
                    q.setMucId(Integer.parseInt(mucId));
                    data.put(mucId, mucMemberService.pageApi(new MyPage<>(page,size),q));
                }
                kv.set("data",data);
            }else {
                kv.set("data", mucMemberService.pageApi(new MyPage<>(page, size), q));
            }
            result = Result.ok(kv);
        }
        //我的所有成员
        else if ("mine".equals(type)) {
            kv.set("data",zlSocket.mucMemberService.findMine(userId));
            result = Result.ok(kv);
        }
        //根据id查询
        else if ("one".equals(type)) {
            kv.set("data",zlSocket.mucMemberService.findById(json.getInteger("id")));
            result = Result.ok(kv);
        }
        //某个群某个用户的成员
        else if ("getByUserIdOfMuc".equals(type)) {
            kv.set("data",zlSocket.mucMemberService.findByMucIdOfUser(json.getInteger("mucId"),json.getInteger("userId")));
            result = Result.ok(kv);
        }
        //某个群所有成员
        else if ("getAllOfMuc".equals(type)) {
            kv.set("data",zlSocket.mucMemberService.findAll(json.getInteger("mucId")));
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown mucInviteLink option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //群聊消息
    private void dealMucMsg(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","mucMsg").set("t",type);
        if ("pagination".equals(type)) {
            QMucMsg q = new QMucMsg();
            q.setUserId(userId);
            q.setTsSend(json.getLong("tsSend"));
            q.setPageSize(json.getInteger("pageSize"));
            q.setAfter(json.getBoolean("after"));
            if(q.getAfter()){
                q.setPageSize(Integer.MAX_VALUE);
            }
            List<MucMsg> datas;
            if(StringUtils.isNotBlank(q.getMucIds())){
                datas = new ArrayList();
                for (String id : StringUtils.split(q.getMucIds(), ";")) {
                    q.setMucId(Integer.parseInt(id));
                    datas.addAll(zlSocket.mucMsgService.pageApi(q));
                }
            }else{
                datas = zlSocket.mucMsgService.pageApi(q);
            }
            for (MucMsg data : datas) {
                data.setReads(zlSocket.readService.listByMsgId(data.getStanzaId()));
            }
            kv.set("data",datas);
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown friend option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //单聊消息
    private void dealMsg(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","msg").set("t",type);
        //分页查询
        if ("pagination".equals(type)) {
            QMsg q = new QMsg();
            q.setUserId(userId);
            q.setToUserId(json.getInteger("toUserId"));
            q.setTsSend(json.getLong("tsSend"));
            q.setPageSize(json.getInteger("pageSize"));
            q.setAfter(json.getBoolean("after"));
            if(q.getAfter()){
                q.setPageSize(Integer.MAX_VALUE);
            }
            kv.set("data",zlSocket.msgService.paginationApi(q));
            result = Result.ok(kv);
        }
        //查询所有好友最近20条
        else if ("syncAll".equals(type)) {
            QMsg q = new QMsg();
            q.setUserId(userId);
            //查询我的好友
            QFriend qf = new QFriend();
            qf.setUserId(userId);
            List<Friend> friends = zlSocket.friendService.findAll(qf);
            List<Msg> msgs = new ArrayList<>();
            for (Friend friend : friends) {
                q.setToUserId(friend.getToUser().getId());
                q.setPageSize(json.getInteger("pageSize"));
                msgs.addAll(zlSocket.msgService.paginationApi(q));
            }
            kv.set("data", msgs);
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown friend option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //设备
    private void dealDevice(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","device").set("t",type);
        if ("all".equals(type)) {
            kv.set("data",zlSocket.deviceService.findAll(userId));
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown device option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //意见反馈
    private void dealFeedback(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","feedback").set("t",type);
        if ("allType".equals(type)) {
            kv.set("data",zlSocket.feedbackTypeService.findAll());
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown feedback option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //聊天背景图
    private void dealChatBg(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","chatBg").set("t",type);
        if ("all".equals(type)) {
            kv.set("data",zlSocket.chatBgService.findAll());
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown chatBg option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //发现页链接
    private void dealLink(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","link").set("t",type);
        if ("all".equals(type)) {
            kv.set("data",zlSocket.linkService.findByServerId(json.getInteger("serverId")));
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown link option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //语言包
    private void dealLocale(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","locale").set("t",type);
        if ("all".equals(type)) {
            kv.set("data",zlSocket.localeService.findAll());
            result = Result.ok(kv);
        }else if ("content".equals(type)) {
            kv.set("data",zlSocket.localeService.getContent(json.getInteger("id")));
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown locale option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //签到
    private void dealSingIn(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","singIn").set("t",type);
        //今天是否签到
        if ("today".equals(type)) {
            kv.set("data",zlSocket.signInService.findByDateOfUser(ToolDateTime.getDate(ToolDateTime.pattern_ymd),json.getInteger("id")));
            result = Result.ok(kv);
        }
        //执行签到
        else if ("sign".equals(type)) {
            kv.set("data",zlSocket.signInService.sign(json.getInteger("id")));
            result = Result.ok(kv);
        }else {
            result = Result.error("unknown locale option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //gif
    private void dealGif(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","helps").set("t",type);
        if ("search".equals(type)) {
            String search = json.getString("search");
            QGif q = new QGif();
            if(!StringUtils.isEmpty(search)){
                q.setEmojiCode(emojiConverter.toAlias(search));
                q.setKeyword(q.getSearch());
            }
            kv.set("data",zlSocket.gifService.paginationApi(new MyPage<>(json.getInteger("page"),json.getInteger("size")),q));
            result = Result.ok(kv);
        }
        else {
            result = Result.error("unknown gif option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //通话
    private void dealCall(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","call").set("t",type);
        if ("all".equals(type)) {
            kv.set("data",zlSocket.callService.findAll(userId));
            result = Result.ok(kv);
        }
        else if ("one".equals(type)) {
            Call record = zlSocket.callService.getById(json.getInteger("id"));
            //既不是发起者也不是接收者
            if(record==null||(!record.getFromId().equals(userId) &&!record.getToId().equals(userId))){
                result = Result.error(null);
            }else {
                kv.set("data",record);
                result = Result.ok(kv);
            }
        }
        else {
            result = Result.error("unknown call option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //自定义表情
    private void dealCustomEmoji(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","customEmoji").set("t",type);
        if ("all".equals(type)) {
            kv.set("data",zlSocket.customEmojiService.findAll(userId));
            result = Result.ok(kv);
        }
        else {
            result = Result.error("unknown customEmoji option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //帮助
    private void dealHelps(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","helps").set("t",type);
        if ("all".equals(type)) {
            kv.set("data",zlSocket.helpsService.findAll());
            result = Result.ok(kv);
        }
        else {
            result = Result.error("unknown helps option:" + type);
        }
        sendMessage(JSON.toJSONString(result));
    }
    //贴纸
    private void dealSticker(String type,JSONObject json) {
        Result result;
        Kv kv = Kv.by("m","sticker").set("t",type);
        if ("all".equals(type)) {
            List<Sticker> list = zlSocket.stickerService.findAll();
            kv.set("size",list.size());
            for (Sticker sticker : list) {
                kv.set("item",sticker);
                result = Result.ok(kv);
                sendMessage(JSON.toJSONString(result));
            }
            return;
        }
        result = Result.error("unknown sticker option:" + type);
        sendMessage(JSON.toJSONString(result));
    }
}
