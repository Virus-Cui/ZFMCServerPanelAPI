package cn.mrcsh.zfmcserverpanelapi.entity.enums;

import lombok.Getter;

import java.io.File;

@Getter
public enum FileType {
    ZIP(1,new String[]{".zip",".rar",".7z",".tar",".gz"}),
    JAR(2,new String[]{".jar"}),
    FOLDER(3, new String[]{"folder"}),
    CANREAD(4,new String[]{".xml",".java",".txt",".vue",".js",".ts",".properties",".yml",".yaml",".toml",".py",".cpp",".c",".h",".sh",".cmd",".bat",".json",".html",".css",".sql",".config",".ini",".log"}),
    IMAGE(5, new String[]{".jpg",".png",".webp",".ico",".jpeg",".tiff",".gif",".raw",".ARW",",svg",".pdf",".bmp"}),
    OTHER(0,new String[]{})
    ;
    private Integer type;
    private String[] suffix;

    FileType(Integer type, String[] suffix) {
        this.type = type;
        this.suffix = suffix;
    }

    public static FileType isInclude(String suffix){
        for (FileType value : FileType.values()) {
            for (String valueSuffix : value.getSuffix()) {
                if(valueSuffix.equals(suffix)){
                    return value;
                }
            }
        }
        return OTHER;
    }

    public static FileType getFileType4FileSuffix(String suffix){
        for (FileType value : FileType.values()) {
            for (String valueSuffix : value.getSuffix()) {
                if(valueSuffix.equals(suffix)){
                    return value;
                }
            }
        }
        return FileType.OTHER;
    }


}
