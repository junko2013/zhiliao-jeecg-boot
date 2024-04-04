package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.service.UploadImageService;
import org.jeecg.modules.im.service.UploadService;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 图片文件上传
 */
@RestController
@RequestMapping("/u/img")
@Slf4j
public class ImageUploadController extends BaseUploadCtrl {

    @Resource
    private UploadImageService uploadImageService;


    @PostMapping({"","/"})
    public Result<Object> index() {
        return success();
    }

    @PostMapping("/upload")
    public @ResponseBody
    Result<Object> index(@RequestParam("file") MultipartFile multipartFile, Integer w) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return uploadImageService.saveImg(getCurrentUserId(),getAdmin(),multipartFile,w);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败", e);
            return fail("上传失败，请重试");
        }
    }
}
