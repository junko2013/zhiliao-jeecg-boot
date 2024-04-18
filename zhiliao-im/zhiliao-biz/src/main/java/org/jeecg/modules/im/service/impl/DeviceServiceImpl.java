package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.query_helper.QDevice;
import org.jeecg.modules.im.mapper.DeviceMapper;
import org.jeecg.modules.im.service.IDeviceService;
import org.jeecg.modules.im.service.ILoginLogService;
import org.jeecg.modules.im.service.IUserInfoService;
import org.jeecg.modules.im.service.IXMPPService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户设备 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-06
 */
@Service
@Slf4j
public class DeviceServiceImpl extends BaseServiceImpl<DeviceMapper, Device> implements IDeviceService {
    @Autowired
    private DeviceMapper deviceMapper;
    @Resource
    private IUserInfoService IUserInfoService;
    @Resource
    private ILoginLogService ILoginLogService;
    @Resource
    private IXMPPService IXMPPService;

    @Override
    public IPage<Device> pagination(MyPage<Device> page, QDevice q) {
        return deviceMapper.pagination(page,q);
    }

    @Override
    public List<Device> findAll(Integer userId) {
        List<Device> devices = deviceMapper.findAll(userId);
        for (Device device : devices) {
            device.setLoginLog(ILoginLogService.findLatestByDeviceId(device.getId()));
        }
        return devices;
    }

    @Override
    public List<Device> findByNo(String no) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getNo,no);
        return list(wrapper);
    }

    @Override
    public Device findByIdOfUser(Integer userId, String no) {
        return deviceMapper.findByIdOfUser(userId,no);
    }

    @Override
    public List<Device> findAllOnline(Integer userId) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        if(userId!=null){
            wrapper.eq(Device::getUserId,userId);
        }
        wrapper.eq(Device::getIsOnline,true);
        return list(wrapper);
    }

    @Override
    public List<Device> findCanOfflinePush(Integer userId) {
        return deviceMapper.findCanOfflinePush(userId);
    }
    @Override
    public List<String> getJPushIds(Integer userId,String platform) {
        return deviceMapper.getJPushIds(userId,platform);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Device findByPlatform(String no, String platform, String detail, User user) {
        if(Device.Platform.getByName(platform)==null){
            log.error("platform "+platform+" is not found");
            return null;
        }
        Device device = null;
        //先根据设备详情查询
        if(StringUtils.isNoneBlank(detail)){
            device = findByDetail(detail,user.getId());
        }
        //再根据平台和序列号查询  同一个设备序列号不固定，但设备详情是固定的
        if(device==null){
            device = deviceMapper.findByPlatform(no,platform, user.getId());
        }
        if (device == null) {
            device = new Device();
            device.setNo(no);
            device.setPlatform(platform);
            device.setUserId(user.getId());
            device.setTsCreate(getDate());
            device.setTsDisabled(null);
            device.setServerId(user.getServerId());
            save(device);
        }
        return device;
    }
    @Override
    public Device findByDetail(String detail, Integer userId) {
        return deviceMapper.findByDetail(detail, userId);
    }
    @Override
    public Integer getCount(Integer userId,Boolean isOnline) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        if(userId!=null){
            wrapper.eq(Device::getUserId,userId);
        }
        if(isOnline!=null){
            wrapper.eq(Device::getIsOnline,true);
        }
        return (int)count(wrapper);
    }

    @Override
    public Result<Object> terminate(Integer userId,Integer id,Boolean except) {
        Device device = getById(id);
        if(device==null){
            return fail("会话不存在，无法操作！");
        }
        if(!except){
            device.setToken(null);
            updateById(device);
        }else{
            deviceMapper.clearToken(id,userId);
        }
        //无论是否在线，都发送终止命令
        MessageBean messageBean = new MessageBean();
        messageBean.setUserId(userId);
        Kv data = Kv.by("no",device.getNo()).set("except",except);
        messageBean.setContent(JSONObject.toJSONString(data));
        messageBean.setType(MsgType.terminate.getType());
        IXMPPService.sendMsgToSelf(messageBean);
        return success();
    }
    @Override
    public Result<Object> cleanJpushId(String jpushId,Integer id) {
        return success(deviceMapper.cleanJpushId(jpushId,id));
    }

    @Override
    public Integer clearAllToken(Integer userId) {
        return deviceMapper.clearAllToken(userId);
    }

    @Override
    public Integer updateOffline(Long ts) {
        return deviceMapper.updateOffline(ts);
    }


}
