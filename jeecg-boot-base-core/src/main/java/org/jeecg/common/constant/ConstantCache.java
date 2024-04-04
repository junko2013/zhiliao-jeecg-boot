package org.jeecg.common.constant;

public interface ConstantCache {
    //所有的ip黑名单
    String BLOCK_IPS = "blockIps_%s_%s";
    //gif发送次数
    String GIF_SEND_TIMES="gif_sendTimes_%s";
    String GIF_ADD_TIMES="gif_addTimes_%s";
    //贴纸发送次数
    String STICKER_ITEM_SEND_TIMES="stickerItem_sendTimes_%s";
    String STICKER_ADD_TIMES="sticker_addTimes_%s";
    //历史头像
    String HISTORY_AVATAR="history_avatar_%s";
    //敏感词
    String SENSITIVE_WORD="sensitive_word_%s";
    //群消息已读
    String MUC_MSG_READ="muc_msg_read_%s";
    //消息已读
    String MSG_READ="msg_read_%s";
    //服务器
    String SERVER="server_%s";
    String SERVER_CONFIG="serverConfig_%s";
    //通话心跳 通话id，发送人，时间戳
    String CALL_HEARTBEAT = "call_%s_%s";
}
