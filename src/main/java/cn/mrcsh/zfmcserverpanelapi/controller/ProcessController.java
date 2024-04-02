package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
import cn.mrcsh.zfmcserverpanelapi.config.Constance;
import cn.mrcsh.zfmcserverpanelapi.entity.dto.ContainerDTO;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Command;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.ContainerVo;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.PageVo;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import cn.mrcsh.zfmcserverpanelapi.manager.RuntimeManager;
import cn.mrcsh.zfmcserverpanelapi.service.ContainerService;
import cn.mrcsh.zfmcserverpanelapi.service.SystemSettingsService;
import cn.mrcsh.zfmcserverpanelapi.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

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
    private RuntimeManager runtimeManager;

    @PostMapping("/createNewContainer")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public response createNewInstance(@RequestBody ContainerDTO containerDto) throws IllegalAccessException {
        Container container = new Container();
        BeanUtils.copyProperty(containerDto, container);
        Container newContainer = containerService.createNewContainer(container);
        Constance.containerCreateTempMap.put(newContainer.getContainerId(), containerDto);
        return success(newContainer);
    }

    @GetMapping("/start/{id}")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public response start(@PathVariable String id) {
        containerService.startContainer(id);
        return success();
    }

    @GetMapping("/stop/{id}")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public response stop(@PathVariable String id) {
        containerService.stopContainer(id);
        return success();
    }

    @PostMapping("/cmd/{id}")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public response execCmd(@PathVariable String id, @RequestBody Command command) {
        containerManager.getContainerByContainerId(id).sendCommand(command.getCmd() == null ? "" : command.getCmd());
        return success();
    }

    @GetMapping("/all/{currentPage}")
    @SaCheckLogin
    @APISupervisory("实例接口")
    public response allContainer(@PathVariable Integer currentPage, String containerName) {
        PageVo<Container> pageVo = containerService.getAllContainer(currentPage, containerName);
        return success(pageVo);
    }

    @GetMapping("/dis/{id}")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public void dis(@PathVariable String id) {
        containerManager.getContainerByContainerId(id).shutdown();
    }

    @GetMapping("/del/{containerId}")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public response deleteContainer(@PathVariable String containerId) {
        containerService.deleteContainerByContainerId(containerId);
        return success();
    }

    @GetMapping("/one/{id}")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public response one(@PathVariable String id) {
        ContainerVo container = containerService.getOne(id);
        return success(container);
    }

    @PostMapping("/delBatch")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public response delBatch(@RequestBody List<String> ids) {
        containerService.deleteBatch(ids);
        return success();
    }

    @PostMapping("/update")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public response update(@RequestBody ContainerDTO containerDTO) throws IllegalAccessException {
        Container container = new Container();
        BeanUtils.copyProperty(containerDTO, container);
        containerService.update(container);
        return success();
    }

    @PostMapping("/startBatch")
    @Async
    @APISupervisory("实例接口")
    @SaCheckLogin
    public void startBatch(@RequestBody List<String> ids) {
        containerService.startBatch(ids);
    }

    @PostMapping("/stopBatch")
    @APISupervisory("实例接口")
    @SaCheckLogin
    public void stopBatch(@RequestBody List<String> ids) {
        containerService.stopBatch(ids);
    }
}
