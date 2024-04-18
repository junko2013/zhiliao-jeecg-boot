package org.jeecg.modules.im.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.component.BaseConfig;
import org.jeecg.modules.im.base.constant.ConstantFileSize;
import org.jeecg.modules.im.base.util.ImageUtils;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.base.util.ToolFile;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.base.util.oss.AliyunOss;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.entity.Upload;
import org.jeecg.modules.im.entity.UserAvatar;
import org.jeecg.modules.im.mapper.UploadMapper;
import org.jeecg.modules.im.service.ISysConfigService;
import org.jeecg.modules.im.service.IUploadAvatarService;
import org.jeecg.modules.im.service.IUploadService;
import org.jeecg.modules.im.service.IUserAvatarService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
public class UploadAvatarServiceImpl extends BaseServiceImpl<UploadMapper, Upload> implements IUploadAvatarService {
    @Resource
    private ISysConfigService ISysConfigService;
    @Resource
    private IUserAvatarService IUserAvatarService;
    @Resource
    private IUploadService IUploadService;
    @Autowired
    private BaseConfig baseConfig;

    @Override
    public Result<Object> saveAvatar(Integer userId,String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.avatar_file_length) {
            return fail("头像大小限制"+(ConstantFileSize.avatar_file_length/1024/1024)+"M以内");
        }
        // Validate content type
        String contentType = multipartFile.getContentType();
        Set<String> supportedTypes = new HashSet<>(Arrays.asList("image/bmp", "image/png", "image/jpg", "image/jpeg", "image/webp", "application/octet-stream"));
        if (!supportedTypes.contains(contentType)) {
            return fail("图片格式不支持");
        }

        SysConfig sysConfig = ISysConfigService.get();
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName=userId +"/"+uuid + suffix;
        if(userId==null){
            fileName = UUIDTool.getUUID2() +"/"+uuid + suffix;
        }
        String fileName2 = userId +"/"+uuid + ".webp";
        if(userId==null){
            fileName2 = UUIDTool.getUUID2() +"/"+uuid + ".webp";
        }
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.头像.getType();
            //保存原图到本地
            String originPath = targetPath + "/o/" + fileName;
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            String originThumbPath = targetPath + "/t/" + fileName;
            tempFile = new File(originThumbPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            File originFile = new File(originPath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File originThumbFile = new File(originThumbPath);
            if (originThumbFile.exists()) {
                originThumbFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, ConstantFileSize.avatar_thumbnail_size, originThumbPath);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/o/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            String destThumbPath = targetPath + "/t/" + fileName2;
            destThumbFile = new File(destThumbPath);
            if (!destThumbFile.exists()) {
                destThumbFile.getParentFile().mkdirs();
            }
            //原图和缩略图都转成webp,转换成功后删除原图
            if(!ImageUtils.convertToWebP(originPath,destPath,0)){
                return fail("上传失败");
            }
            if(!ImageUtils.convertToWebP(originThumbPath,destThumbPath,0)){
                return fail("上传失败");
            }

            f1 = new FileInputStream(destFile);
            FileUtils.deleteQuietly(originFile);
            f2 = new FileInputStream(destThumbFile);
            FileUtils.deleteQuietly(originThumbFile);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.头像.getType() +"/o/"+fileName2, destFile);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.头像.getType() + "/t/" + fileName2, destThumbFile);
            }else {
                originUrl =  IUploadService.uploadToMinio(f1,Upload.FileType.头像.getType() + "/o/"+fileName2,false);
                thumbnailUrl =  IUploadService.uploadToMinio(f2,Upload.FileType.头像.getType() + "/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)){
                IUploadService.addUpload(admin,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        destPath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.头像.getType(),
                        ".webp",
                        null
                );
            }
            if(!isEmpty(thumbnailUrl)){
                IUploadService.addUpload(admin,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destThumbPath,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.头像.getType(),
                        ".webp",
                        null
                );
            }
            UserAvatar userAvatar = new UserAvatar();
            userAvatar.setUserId(userId);
            userAvatar.setThumb(thumbnailUrl);
            userAvatar.setOrigin(originUrl);
            userAvatar.setTsCreate(getDate());
            userAvatar.setTsAudit(getDate());
            IUserAvatarService.save(userAvatar);
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传头像异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            assert f2!=null;
            try {
                f2.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
            FileUtils.deleteQuietly(destThumbFile);
        }
    }

    /**
     * 上传群头像
     * @param multipartFile
     * @param mucId
     * @return
     */
    @Override
    public Result<Object> saveMucAvatar(Integer userId,String admin,MultipartFile multipartFile, String mucId) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.avatar_file_length) {
            return fail("头像大小限制"+(ConstantFileSize.avatar_file_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName=uuid + suffix,fileName2 = uuid + ".webp";
        if (mucId != null) {
            fileName = mucId + suffix;
            fileName2 = mucId + ".webp";
        }
        Kv data = Kv.create();
        SysConfig sysConfig = ISysConfigService.get();
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.群组头像.getType();
            //保存原图到本地
            String originPath = targetPath + "/o/" + fileName;
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            String originThumbPath = targetPath + "/t/" + fileName;
            tempFile = new File(originThumbPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            File originFile = new File(originPath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File originThumbFile = new File(originThumbPath);
            if (originThumbFile.exists()) {
                originThumbFile.delete();
            }
//            fis = new FileInputStream(originFile);
            ImageUtils.createThumbnailsScale(originFile, ConstantFileSize.avatar_thumbnail_size, originThumbPath);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/o/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            String destThumbPath = targetPath + "/t/" + fileName2;
            destThumbFile = new File(destThumbPath);
            if (!destThumbFile.exists()) {
                destThumbFile.getParentFile().mkdirs();
            }
            //原图和缩略图都转成webp,转换成功后删除原图
            if(!ImageUtils.convertToWebP(originPath,destPath,0)){
                return fail("上传失败");
            }
            if(!ImageUtils.convertToWebP(originThumbPath,destThumbPath,0)){
                return fail("上传失败");
            }

            f1 = new FileInputStream(destPath);
            FileUtils.deleteQuietly(originFile);
            f2 = new FileInputStream(destThumbPath);
            FileUtils.deleteQuietly(originThumbFile);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.群组头像.getType() +"/o/"+fileName2, destFile);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.群组头像.getType() + "/t/" + fileName2, destThumbFile);
            }else{
                originUrl =  IUploadService.uploadToMinio(f1,Upload.FileType.群组头像.getType() + "/o/"+fileName2,false);
                thumbnailUrl =  IUploadService.uploadToMinio(f2,Upload.FileType.群组头像.getType() + "/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)){
                IUploadService.addUpload(admin,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        destPath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.群组头像.getType(),
                        ".webp",
                        null
                );
            }
            if(!isEmpty(thumbnailUrl)){
                IUploadService.addUpload(admin,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destThumbPath,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.群组头像.getType(),
                        ".webp",
                        null
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传群组头像异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            assert f2!=null;
            try {
                f2.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
            FileUtils.deleteQuietly(destThumbFile);
        }
    }
}
