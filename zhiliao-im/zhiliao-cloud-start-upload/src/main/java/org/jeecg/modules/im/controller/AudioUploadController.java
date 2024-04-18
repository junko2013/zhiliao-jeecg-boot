package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.IUploadService;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 音频文件上传
 */
@Controller
@RequestMapping("/u/audio")
@Slf4j
public class AudioUploadController extends BaseUploadCtrl {

    @Resource
    private IUploadService IUploadService;


    @GetMapping("/test")
    public String test(){
        return "uploadAudio";
    }

    @PostMapping("/upload")
    public @ResponseBody
    Result<Object> index(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return IUploadService.saveAudio(getCurrentUserId(),getAdmin(),multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败", e);
            return fail("上传失败，请重试");
        }
    }
}
