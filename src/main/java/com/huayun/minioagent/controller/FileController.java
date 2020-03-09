package com.huayun.minioagent.controller;

import com.huayun.minioagent.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    // 列出所有存储桶
    @GetMapping("getAllBuckets")
    public ArrayList getAllBuckets() {
        return fileService.getAllBuckets();
    }

    // 列出存储桶下的文件列表
    @GetMapping("getFiles")
    public ArrayList getFiles(String bucketName) {
        return fileService.getFileLists(bucketName);
    }

    // 单文件上传对象到存储桶
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam String bucketName,
                             @RequestParam String objectName,
                             @RequestParam MultipartFile file) {
        return fileService.uploadFile(bucketName, objectName, file);
    }

    // 多文件上传对象到存储桶
    @RequestMapping(value = "/multifileupload", method = RequestMethod.POST)
    public String multiFileUpload(@RequestParam String bucketName,
                                  @RequestParam String filePath,
                                  @RequestParam MultipartFile[] files) {
        return fileService.multiFileUpload(bucketName, filePath, files);
    }

    // 下载文件并保存到本地
    @RequestMapping(value = "/downloadfile", method = RequestMethod.POST)
    public String downloadFile(
            @RequestParam String bucketName,
            @RequestParam String objectName,
            @RequestParam String filePath) {
        return fileService.downloadFile(bucketName, objectName, filePath);
    }

    // 删除存储桶中的文件对象
    @RequestMapping(value = "/deletefile", method = RequestMethod.DELETE)
    public String deleteFile(@RequestParam String bucketName, @RequestParam String objectName) {
        return fileService.deleteFile(bucketName, objectName);
    }

    // 删除存储桶
    @RequestMapping(value = "/deletebucket", method = RequestMethod.DELETE)
    public String deleteBucket(@RequestParam String bucketName) {
        return fileService.deleteBucket(bucketName);
    }
}
