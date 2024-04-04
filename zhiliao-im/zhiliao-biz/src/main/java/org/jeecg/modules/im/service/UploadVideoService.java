package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Upload;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 上传视频 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
public interface UploadVideoService extends IService<Upload> {
    /**
     * 上传视频文件
     *
     */
    Result<Object> saveVideo(Integer userId,String admin,MultipartFile file);
}
