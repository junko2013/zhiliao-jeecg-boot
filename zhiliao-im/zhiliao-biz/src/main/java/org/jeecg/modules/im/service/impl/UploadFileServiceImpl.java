package org.jeecg.modules.im.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.ConstantFileSize;
import org.jeecg.modules.im.base.util.ToolFile;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.base.util.oss.AliyunOss;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.entity.Upload;
import org.jeecg.modules.im.mapper.UploadMapper;
import org.jeecg.modules.im.service.SysConfigService;
import org.jeecg.modules.im.service.UploadFileService;
import org.jeecg.modules.im.service.UploadService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 文件上传 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Service
@Slf4j
public class UploadFileServiceImpl extends BaseServiceImpl<UploadMapper, Upload> implements UploadFileService {
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private UploadService uploadService;

    @Override
    public Result<Object> saveFile(Integer userId,String admin,String module,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.file_length) {
            return fail("文件大小限制"+ ConstantFileSize.file_length/1024/1024+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String path = userId == null ? "" : userId + "/";
        String fileName = "/" + module + "/" + suffix + "/" + path + uuid + "." + suffix;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String fileUrl;
            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                fileUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.文件.getType() +fileName, multipartFile.getInputStream());
            }else{
                fileUrl = uploadService.uploadToMinio(multipartFile.getInputStream(),Upload.FileType.文件.getType() +fileName);
            }
            uploadService.addUpload(admin,
                    userId,
                    contentType,
                    fileUrl,
                    DigestUtils.md5Hex(multipartFile.getInputStream()),
                    uuid + suffix,
                    null,
                    fileName,
                    multipartFile.getInputStream().available(),
                    sysConfig.getStorageType(),
                    Upload.FileType.文件.getType(),
                    suffix,
                    module
            );
            return success(fileUrl);
        } catch (Exception e) {
            log.error("上传文件异常", e);
            return fail();
        }
    }
}
