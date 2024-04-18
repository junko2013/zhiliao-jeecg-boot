package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.IUploadImageService;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 应用字体预览上传
 */
@RestController
@RequestMapping("/u/font_preview")
@Slf4j
public class FontPreviewUploadController extends BaseUploadCtrl {

    @Resource
    private IUploadImageService IUploadImageService;


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
            return IUploadImageService.saveFontPreview(getAdmin(),multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败", e);
            return fail("上传失败，请重试");
        }
    }
}
