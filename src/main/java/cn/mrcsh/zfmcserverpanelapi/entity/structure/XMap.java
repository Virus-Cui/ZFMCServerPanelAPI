package cn.mrcsh.zfmcserverpanelapi.entity.structure;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_map")
@Data
public class XMap {
    private String xKey;
    private String xValue;
}
