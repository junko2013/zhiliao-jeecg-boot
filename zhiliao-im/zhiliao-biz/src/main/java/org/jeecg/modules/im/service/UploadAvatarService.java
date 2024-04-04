package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Upload;
import org.jeecg.modules.im.entity.query_helper.QUpload;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 头像上传 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
public interface UploadAvatarService extends IService<Upload> {
    /**
     * 上传用户头像
     * 原图+缩略图
     * 图片大小限制2M以内
     * bmp、jpeg、jpg、png
     */
    Result<Object> saveAvatar(Integer userId,String admin,MultipartFile file);
    /**
     * 上传群组头像
     * 原图+缩略图
     * 图片大小限制2M以内
     * bmp、jpeg、jpg、png
     */
    Result<Object> saveMucAvatar(Integer userId,String admin,MultipartFile file, String mucId);
}
