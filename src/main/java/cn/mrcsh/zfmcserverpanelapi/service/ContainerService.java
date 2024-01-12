package cn.mrcsh.zfmcserverpanelapi.service;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;

public interface ContainerService {
    void createNewContainer(Container container);

    void startContainer(String id);
}
