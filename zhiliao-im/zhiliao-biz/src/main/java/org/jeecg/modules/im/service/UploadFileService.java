package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Upload;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件上传 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
public interface UploadFileService extends IService<Upload> {
    /**
     * 上传普通文件
     */
    Result<Object> saveFile(Integer userId,String admin,String module,MultipartFile file);
}
