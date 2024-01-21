package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.Chunk;
import cn.mrcsh.zfmcserverpanelapi.manager.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController extends ABaseController{

    @Autowired
    private FileManager fileManager;

    @PostMapping("/uploadChunk/{containerId}")
    public synchronized response uploadChunk(@PathVariable String containerId, Chunk chunk){
        fileManager.uploadChunk(chunk, containerId);
        return success();
    }
}
