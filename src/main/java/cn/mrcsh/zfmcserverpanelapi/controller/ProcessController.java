package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
import cn.mrcsh.zfmcserverpanelapi.config.Constance;
import cn.mrcsh.zfmcserverpanelapi.entity.dto.ContainerDTO;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ErrorCode;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.FileType;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Command;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.ContainerVo;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.PageVo;
import cn.mrcsh.zfmcserverpanelapi.manager.BeanManager;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import cn.mrcsh.zfmcserverpanelapi.manager.FileManager;
import cn.mrcsh.zfmcserverpanelapi.service.ContainerService;
import cn.mrcsh.zfmcserverpanelapi.service.SystemSettingsService;
import cn.mrcsh.zfmcserverpanelapi.utils.BeanUtils;
import cn.mrcsh.zfmcserverpanelapi.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/container")
@CrossOrigin
@Slf4j
public class ProcessController extends ABaseController {

    @Autowired
    private ContainerManager containerManager;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private SystemSettingsService settingsService;

    @Autowired
    private FileManager fileManager;

    @PostMapping("/createNewContainer")
    @APISupervisory("container")
    public response createNewInstance(@RequestBody ContainerDTO containerDto) throws IllegalAccessException {
        Container container = new Container();
        BeanUtils.copyProperty(containerDto, container);
        Container newContainer = containerService.createNewContainer(container);
        Constance.containerCreateTempMap.put(newContainer.getContainerId(), containerDto);
        return success(newContainer);
    }

    @GetMapping("/start/{id}")
    @APISupervisory("container")
    public response start(@PathVariable String id) {
        containerService.startContainer(id);
        return success();
    }

    @GetMapping("/stop/{id}")
    @APISupervisory("container")
    public response stop(@PathVariable String id) {
        containerService.stopContainer(id);
        return success();
    }

    @PostMapping("/cmd/{id}")
    @APISupervisory("container")
    public response execCmd(@PathVariable String id, @RequestBody Command command) {
        containerManager.getContainerByContainerId(id).sendCommand(command.getCmd() == null ? "" : command.getCmd());
        return success();
    }

    @GetMapping("/all/{currentPage}")
    @SaCheckLogin
    @APISupervisory("container")
    public response allContainer(@PathVariable Integer currentPage, String containerName) {
        PageVo<Container> pageVo = containerService.getAllContainer(currentPage, containerName);
        return success(pageVo);
    }

    @GetMapping("/dis/{id}")
    @APISupervisory("container")
    public void dis(@PathVariable String id) {
        containerManager.getContainerByContainerId(id).shutdown();
    }

    @GetMapping("/del/{containerId}")
    @APISupervisory("container")
    public response deleteContainer(@PathVariable String containerId) {
        containerService.deleteContainerByContainerId(containerId);
        return success();
    }

    @GetMapping("/one/{id}")
    @APISupervisory("container")
    public response one(@PathVariable String id) {
        ContainerVo container = containerService.getOne(id);
        return success(container);
    }

    @PostMapping("/delBatch")
    @APISupervisory("container")
    public response delBatch(@RequestBody List<String> ids) {
        containerService.deleteBatch(ids);
        return success();
    }

    @PostMapping("/update")
    @APISupervisory("container")
    public response update(@RequestBody ContainerDTO containerDTO) throws IllegalAccessException {
        Container container = new Container();
        BeanUtils.copyProperty(containerDTO, container);
        containerService.update(container);
        return success();
    }

    @PostMapping("/startBatch")
    @Async
    @APISupervisory("container")
    public void startBatch(@RequestBody List<String> ids) {
        containerService.startBatch(ids);
    }

    @PostMapping("/stopBatch")
    @APISupervisory("container")
    public void stopBatch(@RequestBody List<String> ids) {
        containerService.stopBatch(ids);
    }
}
