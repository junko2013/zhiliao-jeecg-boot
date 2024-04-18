package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.IUploadAvatarService;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 群组头像上传
 */
@RestController
@RequestMapping("/u/mucAvatar")
@Slf4j
public class MucAvatarUploadController extends BaseUploadCtrl {

    @Resource
    private IUploadAvatarService IUploadAvatarService;


    @GetMapping("/test")
    public String test(){
        return "uploadMucAvatar";
    }

    @PostMapping("/upload")
    public @ResponseBody
    Result<Object> index(@RequestParam("file") MultipartFile multipartFile, String mucId) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return IUploadAvatarService.saveMucAvatar(getCurrentUserId(),getAdmin(),multipartFile,mucId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败", e);
            return fail("上传失败，请重试");
        }
    }
}
