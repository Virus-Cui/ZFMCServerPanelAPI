package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
import cn.mrcsh.zfmcserverpanelapi.entity.dto.FileInfoDTO;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController extends ABaseController {

    @Autowired
    private RuntimeManager runtimeManager;

    @PostMapping("/uploadChunk/{containerId}")
    @APISupervisory("文件接口")
    @SaCheckLogin
    public synchronized response uploadChunk(@PathVariable String containerId, Chunk chunk) {
        runtimeManager.uploadChunk(chunk, containerId);
        return success(null, ErrorCode.UPLOAD_SUCCESS);
    }

    @PostMapping("/unzip")
    @SaCheckLogin
    @APISupervisory("文件接口")
    public response unzip(UnZipFileDTO unZipFileDTO) {
        boolean b = runtimeManager.unzipFile(unZipFileDTO.getContainerId(), unZipFileDTO.getFileName());
        return success(b);
    }

    @PostMapping("/files")
    @APISupervisory("文件接口")
    @SaCheckLogin
    public response getFilesFromPath(PathDTO dto) throws UnsupportedEncodingException {
        List<FileVo> children = FileUtils.getChildren(dto.getPath());
        return success(children);
    }

    @GetMapping("/roots")
    @APISupervisory("文件接口")
    @SaCheckLogin
    public response getRoots() {
        return success(FileUtils.getRoots());
    }

    @PostMapping("/previousFile")
    @APISupervisory("文件接口")
    @SaCheckLogin
    public response getPreviousFile(PathDTO pathDTO) {
        File file = new File(pathDTO.getPath());
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            List<FileVo> roots = FileUtils.getRoots();
            return success(roots);
        }
        List<FileVo> children = FileUtils.getChildren(parentFile.getPath());
        return success(children);
    }

    @PostMapping("/readFile")
    @APISupervisory("文件接口")
    @SaCheckLogin
    public response readFile(PathDTO pathDTO) throws IOException {
        String s = FileUtils.readFile2Text(pathDTO.getPath());
        return success(s);
    }

    @PostMapping("/updateFile")
    @APISupervisory("文件接口")
    @SaCheckLogin
    public response updateFile(FileInfoDTO fileInfoDTO) throws IOException {
        FileUtils.saveFileContent(fileInfoDTO.getFilePath(),fileInfoDTO.getFileContent());
        return success();
    }
}
