package cn.mrcsh.zfmcserverpanelapi.entity.dto;

import lombok.Data;

@Data
public class ContainerDTO {
    private String containerId;
    private String containerName;
    // 工作目录
    private String workdir;
    // 启动命令
    private String cmd;
    // 停止命令
    private String stopCmd;
    private boolean autoStart;
}
