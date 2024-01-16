package cn.mrcsh.zfmcserverpanelapi.service;

public interface MapService {
    String get(String key);
    void set(String key, String value);
    void del(String key);
}
