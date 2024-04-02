package cn.mrcsh.zfmcserverpanelapi.entity.vo;

import cn.mrcsh.zfmcserverpanelapi.entity.enums.ContainerStatus;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ContainerVo {
    // 运行对象
    private String containerId;
    // 进程名称
    private String containerName;
    // 工作目录
    private String workdir;
    // 启动命令
    private String cmd;
    // 停止命令
    private String stopCmd;
    private ContainerStatus status;
    private boolean autoStart;

}
