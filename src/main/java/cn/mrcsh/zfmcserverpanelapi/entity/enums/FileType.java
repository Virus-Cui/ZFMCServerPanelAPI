package cn.mrcsh.zfmcserverpanelapi.entity.enums;

import lombok.Getter;

import java.io.File;

@Getter
public enum FileType {
    ZIP(1,new String[]{".zip",".rar",".7z",".tar",".gz"}),
    JAR(2,new String[]{".jar"})
    ;
    private Integer type;
    private String[] suffix;

    FileType(Integer type, String[] suffix) {
        this.type = type;
        this.suffix = suffix;
    }

    public static Integer isInclude(String suffix){
        for (FileType value : FileType.values()) {
            for (String valueSuffix : value.getSuffix()) {
                if(valueSuffix.equals(suffix)){
                    return value.getType();
                }
            }
        }
        return -1;
    }

}
