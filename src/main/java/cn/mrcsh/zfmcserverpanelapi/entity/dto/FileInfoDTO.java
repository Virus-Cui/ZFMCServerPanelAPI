package cn.mrcsh.zfmcserverpanelapi.entity.dto;

import lombok.Data;

@Data
public class FileInfoDTO {
    private String fileFolder;
    private String fileName;
    private String[] fileContent;
}
