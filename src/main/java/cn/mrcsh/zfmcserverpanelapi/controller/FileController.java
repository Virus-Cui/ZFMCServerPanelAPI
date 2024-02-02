package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
import cn.mrcsh.zfmcserverpanelapi.entity.dto.PathDTO;
import cn.mrcsh.zfmcserverpanelapi.entity.dto.UnZipFileDTO;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ErrorCode;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Chunk;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.FileVo;
import cn.mrcsh.zfmcserverpanelapi.manager.RuntimeManager;
import cn.mrcsh.zfmcserverpanelapi.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController extends ABaseController {

    @Autowired
    private RuntimeManager runtimeManager;

    @PostMapping("/uploadChunk/{containerId}")
    @APISupervisory("文件接口")
    public synchronized response uploadChunk(@PathVariable String containerId, Chunk chunk) {
        runtimeManager.uploadChunk(chunk, containerId);
        return success(null, ErrorCode.UPLOAD_SUCCESS);
    }

    @PostMapping("/unzip")
//    @SaCheckLogin
    @APISupervisory("文件接口")
    public response unzip(UnZipFileDTO unZipFileDTO) {
        boolean b = runtimeManager.unzipFile(unZipFileDTO.getContainerId(), unZipFileDTO.getFileName());
        return success(b);
    }

    @PostMapping("/files")
    public response getFilesFromPath(PathDTO dto) throws UnsupportedEncodingException {
        List<FileVo> children = FileUtils.getChildren(dto.getPath());
        return success(children);
    }

    @GetMapping("/roots")
    public response getRoots(){
        return success(FileUtils.getRoots());
    }

    @PostMapping("/previousFile")
    public response getPreviousFile(PathDTO pathDTO){
        File file = new File(pathDTO.getPath());
        File parentFile = file.getParentFile();
        if(parentFile == null){
            List<FileVo> roots = FileUtils.getRoots();
            return success(roots);
        }
        List<FileVo> children = FileUtils.getChildren(parentFile.getPath());
        return success(children);
    }
}
