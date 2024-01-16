package cn.mrcsh.zfmcserverpanelapi.manager;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.SystemSettings;
import cn.mrcsh.zfmcserverpanelapi.service.SystemSettingsService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
public class FileManager {

    private String SYSTEM_TYPE = "";
    private final String WINDOWS_DEFAULT_DATA_DIR = "C:/ZFMCPanel/instanceData";
    private final String UNIX_DEFAULT_DATA_DIR = "/opt/ZFMCPanel/instanceData";

    @Autowired
    private SystemSettingsService settingsService;

    @PostConstruct
    public void initSystemType() {
        SYSTEM_TYPE = System.getProperty("os.name");
        log.info("初始化系统信息: {}", SYSTEM_TYPE);
        SystemSettings settings = settingsService.getSettings();
        if(settings == null){
            settings = new SystemSettings();
        }
        if (settings.getDataDir() == null || settings.getDataDir().isEmpty()) {
            if (SYSTEM_TYPE.toUpperCase(Locale.ROOT).contains("WIN")) {
                settings.setDataDir(WINDOWS_DEFAULT_DATA_DIR);
            } else {
                settings.setDataDir(UNIX_DEFAULT_DATA_DIR);
            }
        }
        settingsService.saveSettings(settings);
    }


}
