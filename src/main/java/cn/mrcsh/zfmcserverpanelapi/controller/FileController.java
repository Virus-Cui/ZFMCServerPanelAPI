package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
import cn.mrcsh.zfmcserverpanelapi.entity.dto.UnZipFileDTO;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ErrorCode;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Chunk;
import cn.mrcsh.zfmcserverpanelapi.manager.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController extends ABaseController {

    @Autowired
    private FileManager fileManager;

    @PostMapping("/uploadChunk/{containerId}")
    @APISupervisory("文件接口")
    public synchronized response uploadChunk(@PathVariable String containerId, Chunk chunk) {
        fileManager.uploadChunk(chunk, containerId);
        return success(null, ErrorCode.UPLOAD_SUCCESS);
    }

    @PostMapping("/unzip")
//    @SaCheckLogin
    @APISupervisory("文件接口")
    public response unzip(UnZipFileDTO unZipFileDTO) {
        boolean b = fileManager.unzipFile(unZipFileDTO.getContainerId(), unZipFileDTO.getFileName());
        return success(b);
    }
}
