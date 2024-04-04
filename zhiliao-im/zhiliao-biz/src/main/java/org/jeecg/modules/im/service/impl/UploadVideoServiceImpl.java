package org.jeecg.modules.im.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.component.BaseConfig;
import org.jeecg.modules.im.base.constant.ConstantFileSize;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.base.util.ToolFile;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.base.util.oss.AliyunOss;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.entity.Upload;
import org.jeecg.modules.im.mapper.UploadMapper;
import org.jeecg.modules.im.service.SysConfigService;
import org.jeecg.modules.im.service.UploadService;
import org.jeecg.modules.im.service.UploadVideoService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 视频上传 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Service
@Slf4j
public class UploadVideoServiceImpl extends BaseServiceImpl<UploadMapper, Upload> implements UploadVideoService {
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private UploadService uploadService;
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private BaseConfig baseConfig;


    //上传视频文件
    @Override
    public Result<Object> saveVideo(Integer userId,String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.video_file_length) {
            return fail("视频大小限制"+(ConstantFileSize.video_file_length/1024/1024)+"M以内");
        }
//        String contentType = multipartFile.getContentType();//类型
//        Set<String> types = new HashSet<>();
//        types.add("video/x-ms-wmv");
//        types.add("video/mpeg4");
//        types.add("video/avi");
//        types.add("video/wma");
//        types.add("video/mp4");
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        if (!ToolFile.isVideoFile(originalFileName)) {
            return fail("格式不支持");
        }
        String suffix = ToolFile.getExtension(originalFileName);
        String fileName = userId+"/" +UUIDTool.getUUID() + suffix;
        Kv data = Kv.create();
        SysConfig sysConfig = sysConfigService.get();
        try {
            String url;

            //上传到阿里云oss
            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                url = aliyunOss.uploadLocalFile(basePath + Upload.FileType.视频.getType() + "/"+ fileName, multipartFile.getInputStream());
            }else {
                url =  uploadService.uploadToMinio(multipartFile.getInputStream(),Upload.FileType.视频.getType() + "/" + fileName,false);
            }
            data.put("url", url);
            return success(data);
        } catch (Exception e) {
            log.error("上传视频文件异常", e);
            return fail();
        }
    }
}
