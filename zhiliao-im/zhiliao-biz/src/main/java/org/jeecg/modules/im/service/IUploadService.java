package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Upload;
import org.jeecg.modules.im.entity.query_helper.QUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 上传记录 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
public interface IUploadService extends IService<Upload> {
    IPage<Upload> pagination(MyPage<Upload> page, QUpload q);

    /**
     * 上传音频文件
     *
     */
    Result<Object> saveAudio(Integer userId,String admin,MultipartFile file);
    /**
     * 上传语音文件
     *
     */
    Result<Object> saveVoice(Integer userId,String admin,MultipartFile file);
    /**
     * 上传视频文件
     *
     */
    Result<Object> saveVideo(Integer userId,String admin,MultipartFile file);

    /**
     * 根据链接设置记录为待删除状态
     */
    void deleteByHref(String href);

    boolean addUpload(String admin, Integer userId, String contentType, String href, String md5, String name, String path, String sourceName, long size, String storageType, String suffix, String type,String module);

    String uploadToMinio(InputStream stream, String relativePath) throws Exception;

    String uploadToMinio(InputStream stream, String relativePath, Boolean closeStream) throws Exception;
}
