package cn.mrcsh.zfmcserverpanelapi.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVo<T> {
    private long total;
    private long currentPage;
    private long pages;
    private List<T> pageData;
}
