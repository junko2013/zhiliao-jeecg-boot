package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.UploadImageService;
import org.jeecg.modules.im.service.UploadService;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 后台上传广告图片文件上传
 */
@RestController
@RequestMapping("/u/adver")
@Slf4j
public class AdverUploadController extends BaseUploadCtrl {

    @Resource
    private UploadImageService uploadImageService;


    @PostMapping({"","/"})
    public Result<Object> index() {
        return success();
    }

    @PostMapping("/upload")
    public @ResponseBody
    Result<Object> index(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return uploadImageService.saveAdver(getAdmin(),multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败", e);
            return fail("上传失败，请重试");
        }
    }
}
