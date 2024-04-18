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
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.entity.Upload;
import org.jeecg.modules.im.mapper.UploadMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 图片上传 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Service
@Slf4j
public class UploadImageServiceImpl extends BaseServiceImpl<UploadMapper, Upload> implements IUploadImageService {
    @Resource
    private ISysConfigService sysConfigService;
    @Resource
    private ICustomEmojiService customEmojiService;
    @Autowired
    private IUploadService uploadService;
    @Autowired
    private BaseConfig baseConfig;

    @Override
    public Result<Object> saveMsgImg(Integer userId,String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.msg_img_length) {
            return fail("图片大小限制"+(ConstantFileSize.msg_img_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = "msg/"+userId+"/"+uuid + suffix;
        String fileName2 = "msg/"+userId +"/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.消息图片.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/o/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            String thumbnailFilePath = targetPath + "/t/" + fileName;
            tempFile = new File(thumbnailFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            File originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File thumbnailFile = new File(thumbnailFilePath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, ConstantFileSize.msg_img_thumbnail_size, thumbnailFilePath);

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

            String originWebp = targetPath + "/o/" + fileName2;
            String destWebp = targetPath + "/t/" + fileName2;
            //原图和缩略图都转成webp,转换成功后删除原图
            if(!ImageUtils.convertToWebP(originFilePath,originWebp,0)){
                return fail("上传失败");
            }
            if(!ImageUtils.convertToWebP(thumbnailFilePath,destWebp,0)){
                return fail("上传失败");
            }

            f1 = new FileInputStream(originWebp);
            FileUtils.deleteQuietly(originFile);
            f2 = new FileInputStream(destWebp);
            FileUtils.deleteQuietly(thumbnailFile);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.消息图片.getType() +"/o/"+fileName2, f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.消息图片.getType() + "/t/" + fileName2, f2);
            }else {
                originUrl =  uploadService.uploadToMinio(f1, Upload.FileType.消息图片.getType() +"/o/"+fileName2,false);
                thumbnailUrl =  uploadService.uploadToMinio(f2, Upload.FileType.消息图片.getType() +"/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)){
                uploadService.addUpload(admin,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        originWebp,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.消息图片.getType(),
                        ".webp",
                        null
                );
            }
            if(!isEmpty(thumbnailUrl)){
                uploadService.addUpload(admin,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destWebp,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.消息图片.getType(),
                        ".webp",
                        null
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传消息图片异常", e);
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
    @Override
    public Result<Object> savePostImg(Integer userId,String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.post_img_length) {
            return fail("图片大小限制"+(ConstantFileSize.post_img_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = "post/"+userId+"/"+uuid + suffix;
        String fileName2 = "post/"+userId +"/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.朋友圈动态图片.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/o/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            String thumbnailFilePath = targetPath + "/t/" + fileName;
            tempFile = new File(thumbnailFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            File originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File thumbnailFile = new File(thumbnailFilePath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, ConstantFileSize.post_img_thumbnail_size, thumbnailFilePath);

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

            String originWebp = targetPath + "/o/" + fileName2;
            String destWebp = targetPath + "/t/" + fileName2;
            //原图和缩略图都转成webp,转换成功后删除原图
            if(!ImageUtils.convertToWebP(originFilePath,originWebp,0)){
                return fail("上传失败");
            }
            if(!ImageUtils.convertToWebP(thumbnailFilePath,destWebp,0)){
                return fail("上传失败");
            }

            f1 = new FileInputStream(originWebp);
            FileUtils.deleteQuietly(originFile);
            f2 = new FileInputStream(destWebp);
            FileUtils.deleteQuietly(thumbnailFile);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.朋友圈动态图片.getType() +"/o/"+fileName2, f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.朋友圈动态图片.getType() + "/t/" + fileName2, f2);
            }else {
                originUrl =  uploadService.uploadToMinio(f1, Upload.FileType.朋友圈动态图片.getType() +"/o/"+fileName2,false);
                thumbnailUrl =  uploadService.uploadToMinio(f2, Upload.FileType.朋友圈动态图片.getType() +"/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)){
                uploadService.addUpload(admin,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        originWebp,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.朋友圈动态图片.getType(),
                        ".webp",
                        null
                );
            }
            if(!isEmpty(thumbnailUrl)){
                uploadService.addUpload(admin,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destWebp,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.朋友圈动态图片.getType(),
                        ".webp",
                        null
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传朋友圈动态图片异常", e);
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

    @Override
    public Result<Object> saveChatBg(String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.common_img_length) {
            return fail("图片大小限制"+(ConstantFileSize.common_img_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = "chatBg/"+uuid + suffix;
        String fileName2 = "chatBg/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.聊天背景图.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/o/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            String thumbnailFilePath = targetPath + "/t/" + fileName;
            tempFile = new File(thumbnailFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            File originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File thumbnailFile = new File(thumbnailFilePath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, ConstantFileSize.chatBg_img_thumbnail_size, thumbnailFilePath);

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

            String originWebp = targetPath + "/o/" + fileName2;
            String destWebp = targetPath + "/t/" + fileName2;
            //原图和缩略图都转成webp,转换成功后删除原图
            if(!ImageUtils.convertToWebP(originFilePath,originWebp,0)){
                return fail("上传失败");
            }
            if(!ImageUtils.convertToWebP(thumbnailFilePath,destWebp,0)){
                return fail("上传失败");
            }

            f1 = new FileInputStream(originWebp);
            FileUtils.deleteQuietly(originFile);
            f2 = new FileInputStream(destWebp);
            FileUtils.deleteQuietly(thumbnailFile);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.聊天背景图.getType() +"/o/"+fileName2, f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.聊天背景图.getType() + "/t/" + fileName2, f2);
            }else {
                originUrl =  uploadService.uploadToMinio(f1, Upload.FileType.聊天背景图.getType() +"/o/"+fileName2,false);
                thumbnailUrl =  uploadService.uploadToMinio(f2, Upload.FileType.聊天背景图.getType() +"/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)){
                uploadService.addUpload(admin,
                        null,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        originWebp,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.聊天背景图.getType(),
                        ".webp",
                        null
                );
            }
            if(!isEmpty(thumbnailUrl)){
                uploadService.addUpload(admin,
                        null,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destWebp,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.聊天背景图.getType(),
                        ".webp",
                        null
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传聊天背景图异常", e);
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

    @Override
    public Result<Object> saveCustomEmoji(Integer userId,String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.custom_emoji_length) {
            return fail("图片大小限制"+(ConstantFileSize.custom_emoji_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = "custom_emoji/"+userId+"/"+uuid + suffix;
        String fileName2 = "custom_emoji/"+userId +"/"+uuid + ".webp";
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        SysConfig sysConfig = sysConfigService.get();
        int h,w;
        try {
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            h = image.getHeight();
            w = image.getWidth();
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.自定义表情.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/o/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            String thumbnailFilePath = targetPath + "/t/" + fileName;
            tempFile = new File(thumbnailFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            File originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File thumbnailFile = new File(thumbnailFilePath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, ConstantFileSize.custom_emoji_thumbnail_size, thumbnailFilePath);

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

            String originWebp = targetPath + "/o/" + fileName2;
            String destWebp = targetPath + "/t/" + fileName2;
            //原图和缩略图都转成webp,转换成功后删除原图
            if(!ImageUtils.convertToWebP(originFilePath,originWebp,0)){
                return fail("上传失败");
            }
            if(!ImageUtils.convertToWebP(thumbnailFilePath,destWebp,0)){
                return fail("上传失败");
            }

            f1 = new FileInputStream(originWebp);
            FileUtils.deleteQuietly(originFile);
            f2 = new FileInputStream(destWebp);
            FileUtils.deleteQuietly(thumbnailFile);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.自定义表情.getType() +"/o/"+fileName2, f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.自定义表情.getType() + "/t/" + fileName2, f2);
            }else {
                originUrl =  uploadService.uploadToMinio(f1, Upload.FileType.自定义表情.getType() +"/o/"+fileName2,false);
                thumbnailUrl =  uploadService.uploadToMinio(f2, Upload.FileType.自定义表情.getType() +"/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)){
                uploadService.addUpload(admin,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        originWebp,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.自定义表情.getType(),
                        ".webp",
                        null
                );
            }
            if(!isEmpty(thumbnailUrl)){
                uploadService.addUpload(admin,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destWebp,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.自定义表情.getType(),
                        ".webp",
                        null
                );
            }
            CustomEmoji emoji = new CustomEmoji();
            emoji.setOrigin(originUrl);
            emoji.setThumb(thumbnailUrl);
            emoji.setUserId(userId);
            emoji.setTsCreate(getDate());
            emoji.setWidth(w);
            emoji.setHeight(h);
            emoji.setServerId(getServerId());
            customEmojiService.save(emoji);
            return success(emoji);
        } catch (Exception e) {
            log.error("上传自定义表情异常", e);
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

    @Override
    public Result<Object> saveImg(Integer userId,String admin,MultipartFile multipartFile,Integer width) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.common_img_length) {
            return fail("图片大小限制"+ ConstantFileSize.common_img_length/1024/1024+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName=UUIDTool.getUUID2() +"/"+uuid + suffix;
        String fileName2 = UUIDTool.getUUID2() +"/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null;
        File destFile = null;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String originUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.普通图片.getType();
            //保存原图到本地
            String originPath = targetPath + "/" + fileName;
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            File originFile = new File(originPath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            //原图和缩略图都转成webp,转换成功后删除原图
            if(!ImageUtils.convertToWebP(originPath,destPath,0)){
                return fail("上传失败");
            }
            f1 = new FileInputStream(destPath);
            FileUtils.deleteQuietly(originFile);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.普通图片.getType() +"/"+fileName2, f1);
            }else{
                originUrl = uploadService.uploadToMinio(f1,Upload.FileType.普通图片.getType() +"/"+fileName2,false);
            }
            if(!isEmpty(originUrl)){
                uploadService.addUpload(admin,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        destPath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.普通图片.getType(),
                        ".webp",
                        null
                );
            }
            data.put("img", originUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传图片异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
        }
    }
    @Override
    public Result<Object> saveFontPreview(String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.common_img_length) {
            return fail("图片大小限制"+ ConstantFileSize.common_img_length/1024/1024+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName=UUIDTool.getUUID2() +"/"+uuid + suffix;
        String fileName2 = UUIDTool.getUUID2() +"/"+uuid + ".webp";
        FileInputStream f1 = null;
        File destFile = null;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String originUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.字体预览.getType();
            //保存原图到本地
            String originPath = targetPath + "/" + fileName;
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            File originFile = new File(originPath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);

            //webp 压缩后的图
            String destPath = targetPath + "/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            //原图转成webp,转换成功后删除原图
            if(!ImageUtils.convertToWebP(originPath,destPath,0)){
                return fail("上传失败");
            }
            f1 = new FileInputStream(destPath);
            FileUtils.deleteQuietly(originFile);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.字体预览.getType() +"/"+fileName2, f1);
            }else{
                originUrl = uploadService.uploadToMinio(f1,Upload.FileType.字体预览.getType() +"/"+fileName2,false);
            }
            if(!isEmpty(originUrl)){
                uploadService.addUpload(admin,
                        null,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        destPath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.字体预览.getType(),
                        ".webp",
                        null
                );
            }
            return success(originUrl);
        } catch (Exception e) {
            log.error("上传字体预览图异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
        }
    }
    @Override
    public Result<Object> saveAdver(String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.common_img_length) {
            return fail("图片大小限制"+ ConstantFileSize.common_img_length/1024/1024+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName=UUIDTool.getUUID2() +"/"+uuid + suffix;
        String fileName2 = UUIDTool.getUUID2() +"/"+uuid + ".webp";
        FileInputStream f1 = null;
        File destFile = null;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String originUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.广告图.getType();
            //保存原图到本地
            String originPath = targetPath + "/" + fileName;
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            File originFile = new File(originPath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            //原图和缩略图都转成webp,转换成功后删除原图
            if(!ImageUtils.convertToWebP(originPath,destPath,0)){
                return fail("上传失败");
            }
            f1 = new FileInputStream(destPath);
            FileUtils.deleteQuietly(originFile);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.广告图.getType() +"/"+fileName2, f1);
            }else{
                originUrl = uploadService.uploadToMinio(f1,Upload.FileType.广告图.getType() +"/"+fileName2,false);
            }
            if(!isEmpty(originUrl)){
                uploadService.addUpload(admin,
                        null,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        destPath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.广告图.getType(),
                        ".webp",
                        null
                );
            }
            return success(originUrl);
        } catch (Exception e) {
            log.error("上传图片异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
        }
    }
}
