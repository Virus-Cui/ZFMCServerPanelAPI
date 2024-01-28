package cn.mrcsh.zfmcserverpanelapi.entity.structure;

import cn.mrcsh.zfmcserverpanelapi.entity.enums.ContainerStatus;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.WSMessageType;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Queue;

@Data
@Slf4j
@TableName("t_container")
public class Container {
    // 运行对象
    @TableId
    private String containerId;
    // 进程名称
    private String containerName;
    // 守护进程对象
    @TableField(exist = false)
    private Process process;
    // 日志流
    @TableField(exist = false)
    private InputStream inputStream;
    // 命令流
    @TableField(exist = false)
    private OutputStream outputStream;
    // 错误流
    @TableField(exist = false)
    private InputStream errorStream;
    // 工作目录
    private String workdir;
    // 启动命令
    private String cmd;
    // 停止命令
    private String stopCmd;
    // 守护进程PID
    @TableField(exist = false)
    private Long pid;
    // 进程状态
    @TableField(exist = false)
    private ContainerStatus status;
    // 日志
    private String oldlog;
    @TableField(exist = false)
    private String lastType;
    @TableField(exist = false)
    private Queue<String> queue;

    public void initStream() {
        this.setErrorStream(process.getErrorStream());
        this.setOutputStream(process.getOutputStream());
        this.setInputStream(process.getInputStream());
    }

    public void shutdown() {
        log.info("正在关闭实例:{}",containerId);
        if(!this.getStopCmd().equals("^C")){
            sendCommand(this.getStopCmd());
            return;
        }
        this.process.destroyForcibly();
        try {
            if (errorStream != null) errorStream.close();
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
        } catch (Exception e) {
            log.error("关闭流失败，请注意文件占用情况");
        }
    }

    public void sendCommand(String cmd)  {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"gbk"));
            writer.write(cmd);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            log.error("输入流已关闭:{}",containerId);
        }
    }
}
