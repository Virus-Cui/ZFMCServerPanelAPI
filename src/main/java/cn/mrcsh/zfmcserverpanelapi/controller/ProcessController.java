package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.mrcsh.zfmcserverpanelapi.config.Constance;
import cn.mrcsh.zfmcserverpanelapi.entity.dto.ContainerDTO;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ErrorCode;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.FileType;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Command;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.PageVo;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import cn.mrcsh.zfmcserverpanelapi.service.ContainerService;
import cn.mrcsh.zfmcserverpanelapi.service.SystemSettingsService;
import cn.mrcsh.zfmcserverpanelapi.utils.BeanUtils;
import cn.mrcsh.zfmcserverpanelapi.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/container")
@CrossOrigin
@Slf4j
public class ProcessController extends ABaseController{

    @Autowired
    private ContainerManager containerManager;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private SystemSettingsService settingsService;

    @PostMapping("/createNewContainer")
    public response createNewInstance(@RequestBody ContainerDTO containerDto) throws IllegalAccessException {
        Container container = new Container();
        BeanUtils.copyProperty(containerDto, container);
        Container newContainer = containerService.createNewContainer(container);
        Constance.containerCreateTempMap.put(newContainer.getContainerId(), containerDto);
        return success(newContainer);
    }

    @GetMapping("/start/{id}")
    public response start(@PathVariable String id){
        containerService.startContainer(id);
        return success();
    }

    @PostMapping("/cmd/{id}")
    public response execCmd(@PathVariable String id, @RequestBody Command command){
        containerManager.getContainerByContainerId(id).sendCommand(command.getCmd()==null?"":command.getCmd());
        return success();
    }

    @GetMapping("/all/{currentPage}")
    public response allContainer(@PathVariable Integer currentPage,String containerName){
        PageVo<Container> pageVo = containerService.getAllContainer(currentPage,containerName);
        return success(pageVo);
    }

    @GetMapping("/dis/{id}")
    public void dis(@PathVariable String id){
        containerManager.getContainerByContainerId(id).shutdown();
    }

    @PostMapping("/uploadFile/{type}/{containerId}")
    public response uploadFile(@PathVariable Integer type,@PathVariable String containerId, MultipartFile file){

        Integer include = FileType.isInclude(FileUtils.getFileSuffix(file.getOriginalFilename()));
        if(!include.equals(type)){
            return error(ErrorCode.FILE_TYPE_IS_NOT_EXISTS);
        }

        containerService.saveToDataDir(containerId, file);
        if (Constance.containerCreateTempMap.get(containerId).isAutoStart()) {
            start(containerId);
        }
        return success();
    }
}
