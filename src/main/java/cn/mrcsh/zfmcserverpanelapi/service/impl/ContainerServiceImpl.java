package cn.mrcsh.zfmcserverpanelapi.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import cn.mrcsh.zfmcserverpanelapi.mapper.ContainerMapper;
import cn.mrcsh.zfmcserverpanelapi.service.ContainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerMapper containerMapper;

    @Autowired
    private ContainerManager containerManager;

    @Override
    public void createNewContainer(Container container) {
        container.setContainerId(IdUtil.getSnowflakeNextIdStr());
        containerMapper.insert(container);
    }

    @Override
    public void startContainer(String id) {
        try {
            Container container = containerMapper.selectById(id);
            containerManager.exec(container);
        } catch (Exception e) {
            log.error("创建容器时发生错误:",e);
        }

    }
}
