package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.Command;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.PageVo;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import cn.mrcsh.zfmcserverpanelapi.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/container")
@CrossOrigin
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
    public response execCmd(@PathVariable String id, @RequestBody Command command){
        containerManager.getContainerByContainerId(id).sendCommand(command.getCmd()==null?"":command.getCmd());
        return success();
    }

    @GetMapping("/all/{currentPage}")
    public response allContainer(@PathVariable Integer currentPage,String containerName){
        PageVo<Container> pageVo = containerService.getAllContainer(currentPage,containerName);
        return success(pageVo);
    }
}
