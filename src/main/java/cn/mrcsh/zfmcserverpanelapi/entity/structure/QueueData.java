package cn.mrcsh.zfmcserverpanelapi.entity.structure;

import lombok.Data;

@Data
public class QueueData<T> {
    private Integer minute;
    private T data;
}
