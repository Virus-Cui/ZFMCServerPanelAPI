package cn.mrcsh.zfmcserverpanelapi.service;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.ContainerVo;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.PageVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContainerService {
    Container createNewContainer(Container container);

    void startContainer(String id);

    PageVo<Container> getAllContainer(Integer current,String containerName);


    void deleteContainerByContainerId(String containerId);

    void stopContainer(String id);

    ContainerVo getOne(String id);

    void deleteBatch(List<String> ids);
}
