package cn.mrcsh.zfmcserverpanelapi.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ContainerStatus;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.PageVo;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import cn.mrcsh.zfmcserverpanelapi.mapper.ContainerMapper;
import cn.mrcsh.zfmcserverpanelapi.service.ContainerService;
import cn.mrcsh.zfmcserverpanelapi.service.SystemSettingsService;
import cn.mrcsh.zfmcserverpanelapi.utils.FileUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerMapper containerMapper;

    @Autowired
    private ContainerManager containerManager;

    @Autowired
    private SystemSettingsService settingsService;

    @Override
    public Container createNewContainer(Container container) {
        container.setContainerId(IdUtil.getSnowflakeNextIdStr());
        if(container.getWorkdir() == null){
            container.setWorkdir(settingsService.getSettings().getDataDir()+ "/" +container.getContainerId());
        }
        containerMapper.insert(container);
        return container;
    }

    @Override
    public void startContainer(String id) {
        try {
            Container container = containerMapper.selectById(id);
            containerManager.exec(container);
        } catch (Exception e) {
            log.error("创建容器时发生错误:", e);
        }

    }

    @Override
    public PageVo<Container> getAllContainer(Integer current, String containerName) {
        QueryWrapper<Container> wrapper = new QueryWrapper<>();
        wrapper
                .like("container_name", "%"+containerName+"%");
        Page<Container> page = new Page<>(current, 10);
        containerMapper.selectPage(page, wrapper);
        PageVo<Container> pageVo = new PageVo<>();
        pageVo.setCurrentPage(current);
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        List<Container> records = page.getRecords();
        records.forEach(container -> container.setStatus(ContainerStatus.STOP));
        LinkedHashMap<String, Container> runningContainer = containerManager.getRunningContainer();
        for (Map.Entry<String, Container> entry : runningContainer.entrySet()) {
            for (Container record : records) {
                if(entry.getKey().equals(record.getContainerId())){
                    record.setStatus(entry.getValue().getStatus());
                    record.setPid(entry.getValue().getPid());
                }
            }
        }
        pageVo.setPageData(records);
        return pageVo;
    }


}
