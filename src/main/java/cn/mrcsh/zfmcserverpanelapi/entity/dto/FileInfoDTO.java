package cn.mrcsh.zfmcserverpanelapi.entity.dto;

import lombok.Data;

@Data
public class FileInfoDTO {
    private String filePath;
    private String[] fileContent;
}
