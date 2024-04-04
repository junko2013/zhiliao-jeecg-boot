package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.query_helper.QDevice;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户设备 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-06
 */
public interface  DeviceService extends IService<Device> {
    IPage<Device> pagination(MyPage<Device> page, QDevice q);
    //查询某个用户的所有
    List<Device> findAll(Integer userId);
    //根据设备号查询
    List<Device> findByNo(String no);
    //根据设备号和用户
    Device findByIdOfUser(Integer userId,String no);
    List<Device> findAllOnline(Integer userId);
    //可以离线推送的设备
    List<Device> findCanOfflinePush(Integer userId);
    //查找指定平台的推送id
    List<String> getJPushIds(Integer userId,String platform);

    Device findByPlatform(String deviceNo, String platform, String detail, User user);
    Device findByDetail(String detail,Integer userId);
    //查询设备数
    Integer getCount(Integer userId,Boolean isOnline);
    //终止特定,except为true表终止除指定外所有
    Result<Object> terminate(Integer userId,Integer id,Boolean except);

    Result<Object> cleanJpushId(String jspushId,Integer id);
    //清空该用户所有的设备token
    Integer clearAllToken(Integer userId);
    //更新心跳超时离线离线
    Integer updateOffline(Long ts);
}
