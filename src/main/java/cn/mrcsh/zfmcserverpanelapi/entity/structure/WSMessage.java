package cn.mrcsh.zfmcserverpanelapi.entity.structure;

import lombok.Data;

@Data
public class WSMessage {
    private Long time = System.currentTimeMillis();
    private Object data;
    private Integer type;
}
