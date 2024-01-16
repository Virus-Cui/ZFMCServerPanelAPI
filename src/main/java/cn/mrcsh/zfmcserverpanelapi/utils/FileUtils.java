package cn.mrcsh.zfmcserverpanelapi.utils;

import cn.hutool.core.io.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static String getFileSuffix(File file){
        String name = file.getName();
        return name.substring(name.lastIndexOf("."));
    }

    public static String getFileName(File file){
        String name = file.getName();
        return name.substring(0, name.lastIndexOf("."));
    }

    public static String getFileSuffix(String file){
        return file.substring(file.lastIndexOf("."));
    }

    public static String getFileName(String file){
        return file.substring(0, file.lastIndexOf("."));
    }

    public static void saveToPath(MultipartFile multipartFile, File folder){
        try {
            if(!folder.exists()){
                folder.mkdirs();
            }
            multipartFile.transferTo(new File(folder, multipartFile.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
