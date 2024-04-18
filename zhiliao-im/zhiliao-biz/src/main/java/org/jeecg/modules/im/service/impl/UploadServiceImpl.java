package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.Kv;
import org.jeecg.common.util.MinioUtil;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.component.BaseConfig;
import org.jeecg.modules.im.base.constant.ConstantFileSize;
import org.jeecg.modules.im.base.util.*;
import org.jeecg.modules.im.base.util.oss.AliyunOss;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.QUpload;
import org.jeecg.modules.im.mapper.UploadMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * <p>
 * 上传记录 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Service
@Slf4j
public class UploadServiceImpl extends BaseServiceImpl<UploadMapper, Upload> implements IUploadService {
    @Resource
    private ISysConfigService ISysConfigService;
    @Resource
    private IStickerItemService IStickerItemService;
    @Resource
    private IGifService IGifService;
    @Resource
    private IServerConfigService serverConfigService;
    @Resource
    private IUserAvatarService IUserAvatarService;
    @Resource
    private ICustomEmojiService ICustomEmojiService;
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private BaseConfig baseConfig;

    @Override
    public IPage<Upload> pagination(MyPage<Upload> page, QUpload q) {
        return uploadMapper.pagination(page, q);
    }

    /**
     * 音频文件
     * @param multipartFile
     * @return
     */
    @Override
    public Result<Object> saveAudio(Integer userId,String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.audio_file_length) {
            return fail("音频大小限制"+(ConstantFileSize.audio_file_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
//        Set<String> types = new HashSet<>();
//        types.add("audio/amr");
//        types.add("audio/mp3");
//        types.add("audio/wav");
//        if (!types.contains(contentType)) {
//            return fail("格式不支持");
//        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = uuid + suffix;
        Kv data = Kv.create();
        SysConfig sysConfig = ISysConfigService.get();
        FileInputStream f1 = null;
        File originFile = null;
        try {
            String originUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.音频.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            f1 = new FileInputStream(originFile);

            //上传到阿里云oss
            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.音频.getType() + "/" + fileName, originFile);
            }else {
                originUrl =  uploadToMinio(f1,Upload.FileType.音频.getType() + "/" + fileName,false);
            }
            if(!isEmpty(originUrl)){
                addUpload(admin,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + suffix,
                        originFilePath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.音频.getType(),
                        suffix,
                        null
                );
            }
            data.put("url", originUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传音频文件异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            FileUtils.deleteQuietly(originFile);
        }
    }
    //上传语音文件
    @Override
    public Result<Object> saveVoice(Integer userId,String admin,MultipartFile multipartFile) {
        return fail();
    }
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
        SysConfig sysConfig = ISysConfigService.get();
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
                url =  uploadToMinio(multipartFile.getInputStream(),Upload.FileType.视频.getType() + "/" + fileName,false);
            }
            data.put("url", url);
            return success(data);
        } catch (Exception e) {
            log.error("上传视频文件异常", e);
            return fail();
        }
    }

    @Override
    public void deleteByHref(String href) {
        Upload upload = findByHref(href);
        if(upload==null){
            return;
        }
        upload.setStatus(Upload.Status.FutureDelete.getCode());
        upload.setTsDelete(ToolDateTime.getDateByDatePlusDays(new Date(),3).getTime());
        uploadMapper.updateById(upload);
    }

    private Upload findByHref(String href) {
        return uploadMapper.findByHref(href);
    }

    @Override
    public boolean addUpload(String admin,Integer userId,String contentType,String href,String md5,String name,String path,String sourceName,long size,String storageType,String suffix,String type,String module){
        Upload upload = new Upload();
        upload.setAdmin(admin);
        upload.setUserId(userId);
        upload.setContentType(contentType);
        upload.setHref(href);
        upload.setMd5(md5);
        upload.setName(name);
        upload.setPath(path);
        upload.setSourceName(sourceName);
        upload.setSize(size);
        upload.setStorageType(storageType);
        upload.setSuffix(suffix);
        upload.setType(type);
        upload.setTsCreate(getTs());
        upload.setServerId(getServerId());
        upload.setModule(module);
        return save(upload);
    }
    @Override
    public String uploadToMinio(InputStream stream,String relativePath) throws Exception {
        SysConfig config = ISysConfigService.get();
        String resourceDomain = config.getResourceDomain();
        if(!resourceDomain.endsWith("/")){
            resourceDomain+="/";
        }
        return MinioUtil.upload(stream,relativePath).replace(MinioUtil.getMinioUrl(),resourceDomain);
    }
    @Override
    public String uploadToMinio(InputStream stream, String relativePath, Boolean closeStream) throws Exception {
        SysConfig config = ISysConfigService.get();
        String resourceDomain = config.getResourceDomain();
        if(!resourceDomain.endsWith("/")){
            resourceDomain+="/";
        }
        return MinioUtil.upload(stream,relativePath,closeStream).replace(MinioUtil.getMinioUrl(),resourceDomain);
    }
}
