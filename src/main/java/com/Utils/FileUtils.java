package com.Utils;

import com.Entity.UpLoadType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {


    public static boolean upLoadPicture(MultipartFile picture, UpLoadType upLoadType, String fileName) throws IOException {
        if (picture.isEmpty()) {
            return false;
        } else {
            String downLoadDir = upLoadType == UpLoadType.avatar ? "avatar" : "picture";
            String path = ResourceUtils.getURL("classpath:").getPath() + "static/images/" + downLoadDir;
            String contentType = picture.getContentType();
            if ("image/jpeg".equals(contentType) || "image/jpg".equals(contentType)) {
                File file = new File(path, fileName);
                //完成文件的上传
                picture.transferTo(file);
                return true;
            }
            return false;
        }
    }
}
