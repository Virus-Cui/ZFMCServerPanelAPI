package cn.mrcsh.zfmcserverpanelapi.utils;

import cn.mrcsh.zfmcserverpanelapi.entity.enums.FileType;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileUtils {
    public static String getFileSuffix(File file) {
        String name = file.getName();
        return name.substring(name.lastIndexOf(".")==-1?0:name.lastIndexOf("."));
    }

    public static String getFileName(File file) {
        String name = file.getName();
        return name.substring(0, name.lastIndexOf("."));
    }

    public static String getFileSuffix(String file) {
        return file.substring(file.lastIndexOf("."));
    }

    public static String getFileName(String file) {
        return file.substring(0, file.lastIndexOf("."));
    }

    public static void saveToPath(MultipartFile multipartFile, File folder) {
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }
            multipartFile.transferTo(new File(folder, multipartFile.getOriginalFilename() + "-" + System.currentTimeMillis() + ".tmp"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveToPath(MultipartFile multipartFile, File folder, String fileName) {
        try {
            if (!folder.exists()) {
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
        File targetFile = new File(toFilePath + "/" + fileName);
        RandomAccessFile writeFile = null;
        try {
            if (!targetFile.exists() && targetFile.isDirectory()) {
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

    public static List<FileVo> getChildren(String parentPath) {
        File file = new File(parentPath);
        List<FileVo> list = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                FileVo fileVo = new FileVo();
                fileVo.setFileName(f.getName());
                fileVo.setFilePermissions(getPermission(f));
                fileVo.setFileSize(f.length());
                fileVo.setFilePath(f.getPath());
                if(f.isDirectory()){
                    fileVo.setFileType(FileType.FOLDER);
                }else {
                    fileVo.setFileType(FileType.getFileType4FileSuffix(getFileSuffix(f)));
                }
                list.add(fileVo);
            }
        }
        return list;
    }

    public static List<FileVo> getRoots(){
        File[] files = File.listRoots();
        List<FileVo> fileVos = new ArrayList<>();
        for (File file : files) {
            FileVo fileVo = new FileVo();
            fileVo.setFileName(file.getPath().replaceAll(":\\\\",""));
            fileVo.setFileSize(file.length());
            fileVo.setFileType(FileType.FOLDER);
            fileVo.setFilePermissions(getPermission(file));
            fileVo.setFilePath(file.getPath());
            fileVos.add(fileVo);
        }
        return fileVos;
    }

    public static String getPermission(File file){
        List<String> filePermission = new ArrayList<>();
        if (file.canExecute()) {
            filePermission.add("执行");
        } else {
            filePermission.add("-");
        }
        if (file.canRead()) {
            filePermission.add("读取");
        } else {
            filePermission.add("-");
        }
        if (file.canWrite()) {
            filePermission.add("写入");
        } else {
            filePermission.add("-");
        }
        return String.join("/",filePermission);
    }

    public static String readFile2Text(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()){
            return null;
        }
        StringBuffer result = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s = reader.readLine()) != null){
            result.append(s).append("\n");
        }
        reader.close();
        return result.toString();
    }


    public static void saveFileContent(String fileFolder, String fileName, String[] fileContent) throws IOException {
        File file = new File(fileFolder, fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String s : fileContent) {
            writer.write(s);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
}
