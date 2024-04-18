package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Upload;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 贴纸上传 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
public interface IUploadStickerService extends IService<Upload> {

    //上传贴纸
    Result<Object> saveSticker(Integer userId,String admin,MultipartFile file,Integer stickerId,Integer w);
    //批量上传贴纸
    Result<Object> saveStickerBatch(Integer userId,String admin,MultipartFile file,Integer stickerId);

    //上传动画贴纸包的压缩文件
    Result<Object> saveStickerAnimatedPack(Integer userId,String admin,MultipartFile file);
    //上传单个动画贴纸
    Result<Object> saveStickerAnimated(Integer userId,String admin,MultipartFile file,Integer stickerId);
    //批量导入
    Result<Object> saveStickerAnimatedBatch(Integer userId,String admin,MultipartFile file,Integer stickerId);
}
