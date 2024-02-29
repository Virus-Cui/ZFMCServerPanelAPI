package cn.mrcsh.zfmcserverpanelapi.entity.vo;

import lombok.Data;

@Data
public class SysInfoVo {
    private String SysType;
    private String runUser;
    private String memTotal;
    private String jdkVersion;
    private String dashboardVersion;
    private String sysArch;
    private String javaHome;
}
