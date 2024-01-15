package cn.mrcsh.zfmcserverpanelapi.manager;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileManager {

    private String SYSTEM_TYPE = "";

    @PostConstruct
    public void initSystemType(){
        SYSTEM_TYPE = System.getProperty("os.name");
        log.info("初始化系统信息: {}",SYSTEM_TYPE);
    }



}
