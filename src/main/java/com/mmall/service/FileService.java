package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String upload(String path, MultipartFile file);
}
