package org.jeecg.modules.im.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.base.component.BaseConfig;
import org.jeecg.modules.im.base.constant.ConstantFileSize;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.util.*;
import org.jeecg.modules.im.base.util.oss.AliyunOss;
import org.jeecg.modules.im.entity.StickerItem;
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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 贴纸上传 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Service
@Slf4j
public class UploadStickerServiceImpl extends BaseServiceImpl<UploadMapper, Upload> implements UploadStickerService {
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private StickerItemService stickerItemService;
    @Resource
    private ServerConfigService serverConfigService;
    @Resource
    private UploadService uploadService;
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private BaseConfig baseConfig;

    @Override
    public Result<Object> saveSticker(Integer userId,String admin,MultipartFile multipartFile, Integer stickerId,Integer width) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.sticker_file_length) {
            return fail("图片大小限制"+(ConstantFileSize.sticker_file_length/1024/1024)+"M以内");
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
        String fileName = stickerId +"/"+uuid + suffix;
        String fileName2 = stickerId +"/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        File originFile = null,thumbnailFile = null;
        SysConfig sysConfig = sysConfigService.get();
        String originWebp = null,destWebp = null;
        int h,w;
        try {
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            h = image.getHeight();
            w = image.getWidth();
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.贴纸.getType();
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
            originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            thumbnailFile = new File(thumbnailFilePath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, width!=null?width: ConstantFileSize.sticker_thumbnail_size, thumbnailFilePath);

            originWebp = targetPath + "/o/" + fileName2;
            destWebp = targetPath + "/t/" + fileName2;
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
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.贴纸.getType() +"/o/"+fileName2, f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.贴纸.getType() + "/t/" + fileName2, f2);
            }else {
                originUrl =  uploadService.uploadToMinio(f1,Upload.FileType.贴纸.getType() +"/o/"+fileName2,false);
                thumbnailUrl =  uploadService.uploadToMinio(f2,Upload.FileType.贴纸.getType() +"/t/"+fileName2,false);
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
                        Upload.FileType.贴纸.getType(),
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
                        Upload.FileType.贴纸.getType(),
                        ".webp",
                        null
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            data.put("width", w);
            data.put("height", h);
            return success(data);
        } catch (Exception e) {
            log.error("上传贴纸文件异常", e);
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
            FileUtils.deleteQuietly(new File(originWebp));
            FileUtils.deleteQuietly(new File(destWebp));
        }
    }
    @Override
    public Result<Object> saveStickerBatch(Integer userId,String admin,MultipartFile multipartFile, Integer stickerId) {
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/zip");
        if (!types.contains(contentType)) {
            return fail("格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String zipFileName = stickerId +"/"+uuid + suffix;
        try {
            SysConfig sysConfig = sysConfigService.get();
            //物理路径
            String targetPhysicalPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.贴纸.getType();
            //保存原图到本地
            String targetZipFilePath = targetPhysicalPath+"/" + zipFileName;
            File targetZipFile = new File(targetZipFilePath);
            if (!targetZipFile.exists()) {
                targetZipFile.mkdirs();
            }
            //存储压缩文件
            multipartFile.transferTo(targetZipFile);

            //解压zip
            File[] files = ZipUtil.unzipWebp(targetZipFilePath);
            if(files==null||files.length==0){
                return fail("子文件为空");
            }
            StickerItem stickerItem;
            List<StickerItem> stickerItems = new ArrayList<>();
            String originUrl,thumbnailUrl;
            //目标文件夹
            targetPhysicalPath = targetPhysicalPath +"/" + stickerId;
            int errorCount=0,okCount=0;
            for (File webp : files) {
                String uuid2 = UUIDTool.getUUID();
                String suffix2 = ToolFile.getExtension(webp.getName());
                String fileName = uuid2 + suffix2;
                FileInputStream f1=null,f2=null;
                File originFile = null,thumbnailFile = null;
                try {
                    BufferedImage image = ImageIO.read(webp);
                    int width = image.getWidth();
                    int height = image.getHeight();
                    //目标原图
                    String originFilePath = targetPhysicalPath + "/o/" + fileName;
                    File tempFile = new File(originFilePath);
                    if (!tempFile.getParentFile().exists()) {
                        tempFile.getParentFile().mkdirs();
                    }
                    //目标缩略图
                    String thumbnailFilePath = targetPhysicalPath + "/t/" + fileName;
                    tempFile = new File(thumbnailFilePath);
                    if (!tempFile.getParentFile().exists()) {
                        tempFile.getParentFile().mkdirs();
                    }
                    originFile = new File(originFilePath);
                    if (originFile.exists()) {
                        originFile.delete();
                    }
                    try (FileChannel from = new FileInputStream(webp).getChannel();
                         FileChannel to = new FileOutputStream(originFile).getChannel()) {
                        long size = from.size();
                        for (long left = size; left > 0; ) {
                            log.info("position:{},left:{}", size - left, left);
                            left -= from.transferTo((size - left), left, to);
                        }
                    } catch (Exception e) {
                        throw new BusinessException("转存异常");
                    }
                    //保存缩略图到本地
                    thumbnailFile = new File(thumbnailFilePath);
                    if (thumbnailFile.exists()) {
                        thumbnailFile.delete();
                    }
                    ImageUtils.createThumbnailsScale(originFile, ConstantFileSize.sticker_thumbnail_size, thumbnailFilePath);

                    f1 = new FileInputStream(originFilePath);
                    f2 = new FileInputStream(thumbnailFilePath);

                    if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                        AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                        String basePath = sysConfig.getOssBasePath();
                        if (StringUtils.isBlank(basePath)) {
                            basePath = "";
                        }
                        originUrl = aliyunOss.uploadLocalFile(basePath + originFilePath.replace(baseConfig.getBaseUploadPath(), "").substring(1), f1);
                        thumbnailUrl = aliyunOss.uploadLocalFile(basePath + thumbnailFilePath.replace(baseConfig.getBaseUploadPath(), "").substring(1), f2);
                    } else {
                        originUrl =  uploadService.uploadToMinio(f1,originFilePath.replace(baseConfig.getBaseUploadPath(), "").substring(1),false);
                        thumbnailUrl =  uploadService.uploadToMinio(f2, thumbnailFilePath.replace(baseConfig.getBaseUploadPath(), "").substring(1),false);
                    }
                    okCount++;
                    stickerItem = new StickerItem();
                    stickerItem.setStickerId(stickerId);
                    stickerItem.setOrigin(originUrl);
                    stickerItem.setThumb(thumbnailUrl);
                    stickerItem.setTsCreate(getTs());
                    stickerItem.setWidth(width);
                    stickerItem.setHeight(height);
                    stickerItems.add(stickerItem);
                }catch (Exception e){
                    log.error("静态贴纸导入异常，name="+webp.getName(),e);
                    errorCount ++;
                }finally {
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
                    FileUtils.deleteQuietly(originFile);
                    FileUtils.deleteQuietly(thumbnailFile);
                }
            }
            stickerItemService.saveBatch(stickerItems);
            //删除解压的同名文件夹
            FileUtils.deleteQuietly(new File(targetZipFilePath.substring(0,targetZipFilePath.lastIndexOf("."))));
            //删除压缩文件
            FileUtils.deleteQuietly(targetZipFile);
            return success(Kv.by("ok",okCount).set("error",errorCount));
        } catch (Exception e) {
            log.error("上传贴纸文件异常", e);
            return fail();
        }
    }
    //动态贴纸包，保存zip
    @Override
    public Result<Object> saveStickerAnimatedPack(Integer userId,String admin,MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.sticker_animated_zip_file_length) {
            return fail("文件大小限制("+ ConstantFileSize.sticker_animated_zip_file_length /1024/1024+")M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/zip");
        if (!types.contains(contentType)) {
            return fail("文件格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = "temp/"+uuid + suffix;
        Kv data = Kv.create();
        FileInputStream f1 = null;
        SysConfig sysConfig = sysConfigService.get();
        File originFile = null;
        try {
            String lottie;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动画贴纸包.getType();
            //保存上传文件到本地
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
            f1 = new FileInputStream(originFilePath);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                lottie = aliyunOss.uploadLocalFile(basePath + Upload.FileType.动画贴纸包.getType() +"/"+fileName, originFile);
            }else{
                lottie =  uploadService.uploadToMinio(f1,Upload.FileType.动画贴纸包.getType() +"/"+fileName,false);
            }
            if(!isEmpty(lottie)){
                uploadService.addUpload(admin,
                        userId,
                        contentType,
                        lottie,
                        DigestUtils.md5Hex(f1),
                        uuid + ".zip",
                        lottie,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.动画贴纸包.getType(),
                        ".zip",
                        null
                );
            }

            data.put("f", lottie);
            return success(data);
        } catch (Exception e) {
            log.error("上传贴纸文件异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(originFile);
        }
    }
    //动态贴纸，保存zip
    @Override
    public Result<Object> saveStickerAnimated(Integer userId,String admin,MultipartFile multipartFile, Integer stickerId) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.sticker_animated_item_file_length) {
            return fail("文件大小限制("+ ConstantFileSize.sticker_animated_item_file_length /1024/1024+")M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/json");
        if (!types.contains(contentType)) {
            return fail("文件格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = stickerId +"/"+uuid + suffix;
        Kv data = Kv.create();
        FileInputStream f1 = null;
        SysConfig sysConfig = sysConfigService.get();
        File originFile = null;
        try {
            String lottie;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动画贴纸.getType();
            //保存上传文件到本地
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
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(originFile)).getAsJsonObject();

            int width = jsonObject.get("w").getAsInt();
            int height = jsonObject.get("h").getAsInt();
            f1 = new FileInputStream(originFilePath);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                lottie = aliyunOss.uploadLocalFile(basePath + Upload.FileType.动画贴纸.getType() +"/"+fileName, originFile);
            }else{
                lottie =  uploadService.uploadToMinio(f1,Upload.FileType.动画贴纸.getType() +"/"+fileName,false);
            }
            if(!isEmpty(lottie)){
                uploadService.addUpload(admin,
                        userId,
                        contentType,
                        lottie,
                        DigestUtils.md5Hex(f1),
                        uuid + ".zip",
                        lottie,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.动画贴纸.getType(),
                        ".zip",
                        null
                );
            }

            data.put("f", lottie);
            data.put("width", width);
            data.put("height", height);
            return success(data);
        } catch (Exception e) {
            log.error("上传贴纸文件异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(originFile);
        }
    }
    //上传压缩文件,子文件列表均为json
    @Override
    public Result<Object> saveStickerAnimatedBatch(Integer userId,String admin,MultipartFile multipartFile, Integer stickerId) {
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/zip");
        if (!types.contains(contentType)) {
            return fail("文件格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = stickerId +"/"+uuid + suffix;
        try {
            SysConfig sysConfig = sysConfigService.get();
            //物理路径
            String targetPhysicalPath = baseConfig.getBaseUploadPath() + File.separator + Upload.FileType.动画贴纸.getType();
            //保存zip文件到本地
            String zipFilePath = targetPhysicalPath + File.separator+"tgs_zip"+File.separator + fileName;
            String targetDir = targetPhysicalPath +File.separator+ stickerId+File.separator;
            File tempZipFile = new File(zipFilePath);
            if (!tempZipFile.exists()) {
                tempZipFile.mkdirs();
            }
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                zipFile.delete();
            }
            multipartFile.transferTo(zipFile);
            //解压zip
            List<File> animateds = ZipUtil.unzipAnimated(zipFilePath,targetDir);
            if(animateds.isEmpty()){
                return fail("文件列表为空");
            }
            String lottie;
            StickerItem stickerItem;
            List<StickerItem> stickerItems = new ArrayList<>();
            targetPhysicalPath = targetPhysicalPath +File.separator + stickerId;
            int okCount = 0,errorCount = 0;
            for (File animatedJson : animateds) {
                FileInputStream f1 = null;
                try(FileReader reader = new FileReader(animatedJson)) {
                    JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                    int width = jsonObject.get("w").getAsInt();
                    int height = jsonObject.get("h").getAsInt();
                    f1 = new FileInputStream(animatedJson);
                    if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                        AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                        String basePath = sysConfig.getOssBasePath();
                        if (StringUtils.isBlank(basePath)) {
                            basePath = "";
                        }
                        lottie = aliyunOss.uploadLocalFile(basePath + animatedJson.getAbsolutePath().replace(baseConfig.getBaseUploadPath(), "").substring(1), animatedJson);
                    } else{
                        lottie =  uploadService.uploadToMinio(f1,animatedJson.getAbsolutePath().replaceAll("\\\\","/").replace(baseConfig.getBaseUploadPath(), "").substring(1),false);
                    }
                    okCount++;
                    stickerItem = new StickerItem();
                    stickerItem.setStickerId(stickerId);
                    stickerItem.setLottie(lottie);
                    stickerItem.setZipName(animatedJson.getName());
                    stickerItem.setTsCreate(getTs());
                    stickerItem.setWidth(width);
                    stickerItem.setHeight(height);
                    stickerItems.add(stickerItem);
                }catch (Exception e){
                    log.error("批量导入动态贴纸异常", e);
                    errorCount ++;
                }finally {
                    assert f1!=null;
                    try {
                        f1.close();
                    } catch (Exception e) {
                    }
                }
            }
            FileUtils.deleteQuietly(zipFile);
            FileUtils.deleteDirectory(new File(targetPhysicalPath));
            FileUtils.deleteQuietly(new File(zipFile.getAbsolutePath().substring(0,zipFile.getAbsolutePath().lastIndexOf("."))));
            stickerItemService.saveBatch(stickerItems);
            return success(Kv.by("ok",okCount).set("error",errorCount));
        }catch (Exception e){
            log.error("批量导入动画贴纸异常", e);
            return fail();
        }
    }
}
