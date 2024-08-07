package cn.mrcsh.zfmcserverpanelapi.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ContainerStatus;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.ContainerVo;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.PageVo;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import cn.mrcsh.zfmcserverpanelapi.manager.RuntimeManager;
import cn.mrcsh.zfmcserverpanelapi.mapper.ContainerMapper;
import cn.mrcsh.zfmcserverpanelapi.service.ContainerService;
import cn.mrcsh.zfmcserverpanelapi.service.SystemSettingsService;
import cn.mrcsh.zfmcserverpanelapi.utils.BeanUtils;
import cn.mrcsh.zfmcserverpanelapi.utils.FileUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
@Slf4j
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerMapper containerMapper;

    @Autowired
    private ContainerManager containerManager;

    @Autowired
    private SystemSettingsService settingsService;

    @Autowired
    private RuntimeManager runtimeManager;

    @Override
    public Container createNewContainer(Container container) {
        container.setContainerId(IdUtil.getSnowflakeNextIdStr());
        if (container.getWorkdir() == null) {
            container.setWorkdir(settingsService.getSettings().getDataDir() + "/" + container.getContainerId());
        }
        if (runtimeManager.getSYSTEM_TYPE().toUpperCase(Locale.ROOT).contains("WIN")) {
            container.setEncode("GBK");
        } else {
            container.setEncode("UTF-8");
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
                .like("container_name", "%" + containerName + "%");
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
                if (entry.getKey().equals(record.getContainerId())) {
                    record.setStatus(entry.getValue().getStatus());
                    record.setPid(entry.getValue().getPid());
                }
            }
        }
        pageVo.setPageData(records);
        return pageVo;
    }

    @Override
    public void deleteContainerByContainerId(String containerId) {
        containerMapper.deleteById(containerId);
    }

    @Override
    public void stopContainer(String id) {
        Container container = containerManager.getRunningContainer().get(id);
        container.setStatus(ContainerStatus.STOPING);
        if (container.getStopCmd().equals("^C")) {
            container.shutdown();
            return;
        }
        container.sendCommand(container.getStopCmd());
    }

    @Override
    public ContainerVo getOne(String id) {
        ContainerVo containerVo;
        try {
            Container container = containerManager.getRunningContainer().get(id);
            containerVo = new ContainerVo();
            if (container == null) {
                container = containerMapper.selectById(id);
                container.setStatus(ContainerStatus.STOP);
            }
            BeanUtils.copyProperty(container, containerVo);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return containerVo;
    }

    @Override
    public void deleteBatch(List<String> ids) {
        containerMapper.deleteBatchIds(ids);
    }

    @Override
    public void update(Container container) {
        Container con = containerManager.getRunningContainer().get(container.getContainerId());
        if(con != null){
            con.setStopCmd(container.getStopCmd());
            con.setAutoStart(container.isAutoStart());
            con.setCmd(container.getCmd());
            con.setContainerName(container.getContainerName());
        }
        containerMapper.updateById(container);
    }

    @Override
    public void startBatch(List<String> ids) {
        try {
            for (String id : ids) {
                containerManager.exec(containerMapper.selectById(id));
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            log.error("批量开启失败",e);
        }
    }

    @Override
    public void stopBatch(List<String> ids){
        for (String id : ids) {
            containerManager.getContainerByContainerId(id).shutdown();
        }

    }
}
