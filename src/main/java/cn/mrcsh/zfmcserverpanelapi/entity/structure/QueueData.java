package cn.mrcsh.zfmcserverpanelapi.entity.structure;

import lombok.Data;

@Data
public class QueueData<T> {
    private String minute;
    private T data;
}
