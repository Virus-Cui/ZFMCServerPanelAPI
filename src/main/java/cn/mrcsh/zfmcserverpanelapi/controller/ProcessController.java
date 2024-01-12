package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import cn.mrcsh.zfmcserverpanelapi.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/container")
public class ProcessController extends ABaseController{

    @Autowired
    private ContainerManager containerManager;

    @Autowired
    private ContainerService containerService;

    @PostMapping("/createNewContainer")
    public response createNewInstance(@RequestBody Container container){
        containerService.createNewContainer(container);
        return success();
    }

    @GetMapping("/start/{id}")
    public response start(@PathVariable String id){
        containerService.startContainer(id);
        return success();
    }

    @PostMapping("/cmd/{id}")
    public response execCmd(@PathVariable String id, @RequestBody String cmd){
        containerManager.getContainerByContainerId(id).sendCommand(cmd);
        return success();
    }
}
