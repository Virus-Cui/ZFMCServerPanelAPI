package cn.mrcsh.zfmcserverpanelapi.entity.vo;

import cn.mrcsh.zfmcserverpanelapi.entity.enums.FileType;
import lombok.Data;

@Data
public class FileVo {
    private String fileName;
    private String filePath;
    private long fileSize;
    private String filePermissions;
    private FileType fileType;

}
