package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Upload;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 图片上传 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
public interface UploadImageService extends IService<Upload> {
    /**
     * 上传消息图片
     * 原图+缩略图
     */
    Result<Object> saveMsgImg(Integer userId,String admin,MultipartFile file);
    /**
     * 上传朋友圈图片
     * 原图+缩略图
     */
    Result<Object> savePostImg(Integer userId,String admin,MultipartFile file);
    /**
     * 上传自定义表情
     * 原图+缩略图
     */
    Result<Object> saveCustomEmoji(Integer userId,String admin,MultipartFile file);
    //上传普通图片
    Result<Object> saveImg(Integer userId,String admin,MultipartFile file,Integer w);
    //上传广告图
    Result<Object> saveAdver(String admin,MultipartFile file);
    //上传聊天背景图
    Result<Object> saveChatBg(String admin,MultipartFile file);
    //上传字体预览图
    Result<Object> saveFontPreview(String admin,MultipartFile file);
}
