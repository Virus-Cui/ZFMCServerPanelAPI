package cn.mrcsh.zfmcserverpanelapi.manager;

import cn.hutool.core.util.ZipUtil;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Chunk;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.SystemSettings;
import cn.mrcsh.zfmcserverpanelapi.mapper.ContainerMapper;
import cn.mrcsh.zfmcserverpanelapi.service.ContainerService;
import cn.mrcsh.zfmcserverpanelapi.service.SystemSettingsService;
import cn.mrcsh.zfmcserverpanelapi.task.DatabaseInitialize;
import cn.mrcsh.zfmcserverpanelapi.utils.FileUtils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Locale;

@Component
@Slf4j
public class RuntimeManager {

    @Autowired
    private ContainerMapper containerMapper;

    @Autowired
    private DatabaseInitialize databaseInitialize;

    @Getter
    private String SYSTEM_TYPE = "";
    private final String WINDOWS_DEFAULT_DATA_DIR = "C:/ZFMCPanel/instanceData";
    private final String UNIX_DEFAULT_DATA_DIR = "/opt/ZFMCPanel/instanceData";

    @Autowired
    private SystemSettingsService settingsService;

    @PostConstruct()
    public void initSystemType() {
        databaseInitialize.initDatabase();
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

    public void uploadChunk(Chunk chunk, String containerId) {
        log.info("切片:{}",chunk.getChunkNumber());
        Integer currentChunk = chunk.getChunkNumber();
        Container container = containerMapper.selectById(containerId);
        String tempPath = container.getWorkdir()+"/"+chunk.getFileName()+".temp";
        if(currentChunk == 0){
            try {
                File file = new File(tempPath);
                file.mkdirs();
            }catch (Exception e){
                log.error("创建临时文件夹失败");
            }
        }

        MultipartFile chunkFile = chunk.getFile();
        FileUtils.saveToPath(chunkFile, new File(tempPath),String.valueOf(chunk.getChunkNumber()));

        if(currentChunk >= chunk.getTotalChunk()-1){
            // 合并
            FileUtils.union(tempPath, chunk.getPath(), chunk.getFileName(), true);
        }
    }

    public boolean unzipFile(String containerId, @RequestBody String fileName) {
        String workdir = containerMapper.selectById(containerId).getWorkdir();
        File zipFile = new File(workdir, fileName);
        if(!zipFile.exists()){
            return false;
        }
        ZipUtil.unzip(zipFile, new File(workdir));
        return true;
    }
}
