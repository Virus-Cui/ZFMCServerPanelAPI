package cn.mrcsh.zfmcserverpanelapi.utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Slf4j
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
            multipartFile.transferTo(new File(folder, multipartFile.getOriginalFilename()+"-"+System.currentTimeMillis()+".tmp"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveToPath(MultipartFile multipartFile, File folder, String fileName){
        try {
            if(!folder.exists()){
                folder.mkdirs();
            }
            multipartFile.transferTo(new File(folder, fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void union(String dirPath, String toFilePath, String fileName, Boolean delSource) {
        File dir = new File(dirPath);
        if (!dir.exists()) {

        }
        File[] fileList = dir.listFiles();
        File targetFile = new File(toFilePath+"/"+fileName);
        RandomAccessFile writeFile = null;
        try {
            if(!targetFile.exists() && targetFile.isDirectory()){
                targetFile.createNewFile();
            }
            writeFile = new RandomAccessFile(targetFile, "rw");
            byte[] b = new byte[1024 * 10];
            for (int i = 0; i < fileList.length; i++) {
                int len = -1;
                File chunkFile = new File(dirPath + "/" + i);
                RandomAccessFile readFile = null;
                try {
                    readFile = new RandomAccessFile(chunkFile, "r");
                    while ((len = readFile.read(b)) != -1) {
                        writeFile.write(b, 0, len);
                    }
                } catch (Exception e) {
                    log.error("合并分片失败");
                } finally {
                    readFile.close();
                }
            }
        } catch (Exception e) {
            log.error("合并文件:{}失败", fileName, e);
        } finally {
            if (null != writeFile) {
                try {
                    writeFile.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (delSource && dir.exists()) {
                try {
                    org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory(dir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 解压
     * @param file 解压目标文件
     */
    public static void unZip(File file, File toPath){

    }
}
