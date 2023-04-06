package com.krekerok.blogapp.service.impl;

import com.cloudinary.Cloudinary;
import com.krekerok.blogapp.exception.FileUploadException;
import com.krekerok.blogapp.service.CloudinaryService;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        try {
            String url = cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                    Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
            return url;
        } catch (IOException e) {
            throw new FileUploadException("Problems with file uploading");
        }
    }
}
