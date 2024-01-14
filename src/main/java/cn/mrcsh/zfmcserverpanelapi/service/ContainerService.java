package cn.mrcsh.zfmcserverpanelapi.service;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.PageVo;

import java.util.List;

public interface ContainerService {
    void createNewContainer(Container container);

    void startContainer(String id);

    PageVo<Container> getAllContainer(Integer current,String containerName);
}
