package cn.mrcsh.zfmcserverpanelapi.entity.vo;

import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

@Data
public class EchartsVo {

    private List<String> xAxis;
    private List<String> types;
    private List<DataStructure> data;

    @Data
    public static class DataStructure {
        private String name;
        private String type;
        private String stack = "Total";
        private Queue<Integer> data;
    }
}
