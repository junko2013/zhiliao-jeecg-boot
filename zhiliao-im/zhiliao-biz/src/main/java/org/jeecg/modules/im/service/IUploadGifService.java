package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Upload;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * gif上传 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
public interface IUploadGifService extends IService<Upload> {

    /**
     * 上传gif
     */
    Result<Object> saveGif(Integer userId,String admin,MultipartFile file,Integer gifAlbumId,Integer w);
    //批量导入
    Result<Object> saveGifBatch(Integer userId,String admin,MultipartFile file,Integer gifAlbumId);
}
