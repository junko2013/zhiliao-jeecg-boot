package org.jeecg.modules.im.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.base.component.BaseConfig;
import org.jeecg.modules.im.base.constant.ConstantFileSize;
import org.jeecg.modules.im.base.util.*;
import org.jeecg.modules.im.base.util.oss.AliyunOss;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.entity.Upload;
import org.jeecg.modules.im.mapper.UploadMapper;
import org.jeecg.modules.im.service.IGifService;
import org.jeecg.modules.im.service.ISysConfigService;
import org.jeecg.modules.im.service.IUploadGifService;
import org.jeecg.modules.im.service.IUploadService;
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
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * <p>
 * 动图上传 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Service
@Slf4j
public class UploadGifServiceImpl extends BaseServiceImpl<UploadMapper, Upload> implements IUploadGifService {
    @Resource
    private ISysConfigService ISysConfigService;
    @Resource
    private IGifService IGifService;
    @Resource
    private IUploadService IUploadService;
    @Autowired
    private BaseConfig baseConfig;

    @Override
    public Result<Object> saveGif(Integer userId,String admin,MultipartFile multipartFile, Integer gifAlbumId,Integer width) {
        long size = multipartFile.getSize();
        if (size >= ConstantFileSize.gif_file_length) {
            return fail("图片大小限制"+(ConstantFileSize.gif_file_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }

        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = gifAlbumId +"/"+uuid + suffix;
        String fileName2 = gifAlbumId +"/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        SysConfig sysConfig = ISysConfigService.get();
        File originFile,thumbnailFile;
        String originWebp=null,destWebp=null;
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动图.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/o/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.getParentFile().exists()) {
                tempFile.mkdirs();
            }
            String thumbnailFilePath = targetPath + "/t/" + fileName;
            tempFile = new File(thumbnailFilePath);
            if (!tempFile.getParentFile().exists()) {
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
            ImageUtils.createThumbnailsScale(originFile, width!=null?width: ConstantFileSize.gif_thumbnail_size, thumbnailFilePath);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/o/" + fileName2;
            File destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            String destThumbPath = targetPath + "/t/" + fileName2;
            File destThumbFile = new File(destThumbPath);
            if (!destThumbFile.exists()) {
                destThumbFile.getParentFile().mkdirs();
            }

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
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.动图.getType()+"/"+gifAlbumId+"/o/"+uuid+".webp", f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.动图.getType() +"/"+gifAlbumId+"/t/"+uuid+".webp", f2);
            } else {
                originUrl = IUploadService.uploadToMinio(f1,Upload.FileType.动图.getType() +"/"+gifAlbumId+"/o/"+uuid+".webp",false);
                thumbnailUrl = IUploadService.uploadToMinio(f2,Upload.FileType.动图.getType() +"/"+gifAlbumId+"/t/"+uuid+".webp",false);
            }
            if(!isEmpty(originUrl)){
                IUploadService.addUpload(admin,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        originWebp,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.动图.getType(),
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
                        destWebp,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.动图.getType(),
                        ".webp",
                        null
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            data.put("width", image.getWidth());
            data.put("height", image.getHeight());
            return success(data);
        } catch (Exception e) {
            log.error("上传gif动图异常", e);
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
            try{
                FileUtils.deleteQuietly(new File(originWebp));
                FileUtils.deleteQuietly(new File(destWebp));
            }catch (Exception e){}

        }
    }

    @Override
    public Result<Object> saveGifBatch(Integer userId,String admin,MultipartFile multipartFile, Integer gifAlbumId) {
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/zip");
        types.add("application/x-zip-compressed");
        if (!types.contains(contentType)) {
            return fail("文件格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = gifAlbumId +"/"+uuid + suffix;

        File zipFile = null;
        List<Gif> gifs = null;
        try {
            SysConfig sysConfig = ISysConfigService.get();
            //物理路径
            String targetPhysicalPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动图.getType();
            String zipFilePath = targetPhysicalPath + "/" + fileName;
            File tempZipFile = new File(zipFilePath);
            if (!tempZipFile.getParentFile().exists()) {
                tempZipFile.getParentFile().mkdirs();
            }
            zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                zipFile.delete();
            }
            //保存zip文件到本地
            multipartFile.transferTo(zipFile);
            //解压zip
            List<File> files = ZipUtil.unzipGif(zipFilePath);
            if(files.size()==0){
                return fail("文件数量为0");
            }
            Gif gif;
            gifs = new ArrayList<>();
            targetPhysicalPath = targetPhysicalPath + "/" + gifAlbumId;
            String gifFileName,targetGifPath,targetGifName,thumbnailGifPath,thumbnailGifName,destGif,destThumbGif,uuid2,gifMd5;
            FileInputStream fis;
            int okCount=0,errorCount=0;
            for (File g : files) {
                BufferedImage image = ImageIO.read(g);
                int width = image.getWidth();
                int height = image.getHeight();
                fis = new FileInputStream(g);
                gifMd5 = DigestUtils.md5Hex(fis);
//                if(gifService.findByMd5(gifMd5)!=null){
//                    try {
//                        fis.close();
//                    } catch (Exception e) {
//                    }
//                    continue;
//                }
                uuid2 = UUIDTool.getUUID();
                gifFileName = uuid2 +".gif";
                targetGifName = targetPhysicalPath + "/o/" + gifFileName;
                targetGifPath = targetPhysicalPath + "/o/" + uuid2;
                File tempFile = new File(targetGifPath);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                thumbnailGifName = targetPhysicalPath + "/t/" + gifFileName;
                thumbnailGifPath = targetPhysicalPath + "/t/" + uuid2;
                tempFile = new File(thumbnailGifPath);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                FileInputStream f1 = null,f2 = null;
                File destFile,destThumbFile;
                try (FileChannel from = fis.getChannel();
                     FileChannel to = new FileOutputStream(targetGifName).getChannel()){
                    long size = from.size();
                    for (long left = size; left > 0; ) {
                        left -= from.transferTo((size - left), left, to);
                    }
                    //保存缩略图到本地
                    ImageUtils.createThumbnailsScale(new File(targetGifName), ConstantFileSize.gif_thumbnail_size, thumbnailGifName);
                    String gifFileName2 = UUIDTool.getUUID()+".webp";
                    //webp 压缩后的图和缩略图
                    String destPath = targetPhysicalPath + "/o/" + gifFileName2;
                    destFile = new File(destPath);
                    if (!destFile.exists()) {
                        destFile.getParentFile().mkdirs();
                    }
                    String destThumbPath = targetPhysicalPath + "/t/" + gifFileName2;
                    destThumbFile = new File(destThumbPath);
                    if (!destThumbFile.exists()) {
                        destThumbFile.getParentFile().mkdirs();
                    }
                    //原图和缩略图都转成webp,转换成功后删除原图
                    if(!ImageUtils.convertToWebP(targetGifName,destPath,0)){
                        return fail("上传失败");
                    }
                    if(!ImageUtils.convertToWebP(thumbnailGifName,destThumbPath,0)){
                        return fail("上传失败");
                    }

                    f1 = new FileInputStream(destPath);
                    FileUtils.deleteQuietly(new File(targetGifPath));
                    f2 = new FileInputStream(destThumbPath);
                    FileUtils.deleteQuietly(new File(thumbnailGifPath));

                    if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                        AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                        String basePath = sysConfig.getOssBasePath();
                        if (StringUtils.isBlank(basePath)) {
                            basePath = "";
                        }
                        destGif = aliyunOss.uploadLocalFile(basePath + destPath.replace(baseConfig.getBaseUploadPath(),"").substring(1), f1);
                        destThumbGif = aliyunOss.uploadLocalFile(basePath + destThumbPath.replace(baseConfig.getBaseUploadPath(),"").substring(1), f2);
                    }else{
                        destGif = IUploadService.uploadToMinio(f1,destPath.replace(baseConfig.getBaseUploadPath(),"").substring(1),false);
                        destThumbGif = IUploadService.uploadToMinio(f2,destThumbPath.replace(baseConfig.getBaseUploadPath(),"").substring(1),false);
                    }
                    okCount++;
                    gif = new Gif();
                    gif.setAlbumId(gifAlbumId);
                    gif.setOrigin(destGif);
                    gif.setThumb(destThumbGif);
                    gif.setTsCreate(getDate());
                    gif.setMd5(gifMd5);
                    gif.setHeight(height);
                    gif.setWidth(width);
                    gifs.add(gif);
                }catch (Exception e){
                    log.error("gif导入转存异常，name"+g.getName(),e);
                    errorCount ++;
                }finally {
                    fis.close();
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
                }
            }
            return success(Kv.by("ok",okCount).set("error",errorCount));
        } catch (Exception e) {
            log.error("批量导入gif动图异常", e);
            return fail();
        } finally {
            if(gifs!=null&&!gifs.isEmpty()){
                IGifService.saveBatch(gifs);
            }
            if(zipFile!=null){
                FileUtils.deleteQuietly(zipFile);
                FileUtils.deleteQuietly(new File(zipFile.getAbsolutePath().substring(0,zipFile.getAbsolutePath().lastIndexOf("."))));
                //删掉临时文件夹
                FileUtils.deleteQuietly(new File(baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动图.getType()+"/"+gifAlbumId));
            }
        }
    }
}
