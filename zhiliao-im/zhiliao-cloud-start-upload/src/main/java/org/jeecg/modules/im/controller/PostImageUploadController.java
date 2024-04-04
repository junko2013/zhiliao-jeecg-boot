package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.UploadImageService;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 朋友圈图片上传
 */
@Controller
@RequestMapping("/u/postImg")
@Slf4j
public class PostImageUploadController extends BaseUploadCtrl {

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
            return fail("请选择要上传的图片");
        }
        try {
            return uploadImageService.savePostImg(getCurrentUserId(),getAdmin(),multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("图片上传失败", e);
            return fail("上传失败，请重试");
        }
    }
}
