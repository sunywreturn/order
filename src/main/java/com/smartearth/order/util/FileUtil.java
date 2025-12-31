package com.smartearth.order.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    private static String filePath;

    @Value("${upload.file.path}")
    public void setFilePath(String filePath) {
        FileUtil.filePath = filePath;
        File uploadFileDir = new File(PROJECT_PATH+ File.separator + filePath);
        if(!uploadFileDir.exists()){
            uploadFileDir.mkdir();
        }
    }

    public static String save(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString().replace("-", "") + file.getOriginalFilename();
        file.transferTo(new File(PROJECT_PATH + File.separator + filePath + File.separator + fileName));
        return fileName;
    }

}
