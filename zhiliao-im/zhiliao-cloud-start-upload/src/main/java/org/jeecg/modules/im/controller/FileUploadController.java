package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.IUploadFileService;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 普通文件上传
 */
@RestController
@RequestMapping("/u/file")
@Slf4j
public class FileUploadController extends BaseUploadCtrl {

    @Resource
    private IUploadFileService IUploadFileService;


    @PostMapping("/upload/{module}")
    public @ResponseBody
    Result<Object> index(@RequestParam("file") MultipartFile multipartFile,@PathVariable("module")String module) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return IUploadFileService.saveFile(getCurrentUserId(),getAdmin(),module,multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败", e);
            return fail("上传失败，请重试");
        }
    }
}
