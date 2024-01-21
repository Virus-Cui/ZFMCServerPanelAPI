package cn.mrcsh.zfmcserverpanelapi.entity.structure;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Chunk {
    // 文件名
    private String fileName;
    // 位置
    private String path;
    // 当前切片
    private Integer chunkNumber;
    // 切片大小
    private Long chunkSize;
    // 当前切片大小
    private Long currentChunkSize;
    // 总大小
    private Long totalSize;
    // 总切片
    private Integer totalChunk;
    // 文件
    private MultipartFile file;
}
