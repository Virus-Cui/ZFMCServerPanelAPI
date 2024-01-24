package cn.mrcsh.zfmcserverpanelapi.entity.enums;

public enum ContainerStatus {
    RUNNING(1,"正在运行"),
    STOP(0,"停止"),
    STOPING(3,"正在停止"),
    STARTING(2,"正在启动")
    ;
    private Integer code;
    private String desc;

    ContainerStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
