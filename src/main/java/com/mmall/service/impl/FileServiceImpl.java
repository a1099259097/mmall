package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.FileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(String path, MultipartFile file) {

        String fileName = file.getOriginalFilename();
        String ExtensionFileName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString();

        File targetDir = new File(path);
        if (!targetDir.isDirectory()) {
            targetDir.setWritable(true);
            targetDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //upload fuwuqi
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();

        } catch (IOException e) {
            logger.error("upload error", e);
            return null;
        }
        return targetFile.getName();
    }
}
