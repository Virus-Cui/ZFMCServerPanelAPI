package cn.mrcsh.zfmcserverpanelapi.entity.enums;

public enum WSMessageType {
    LOG(0,"日志"),
    NOTIFY(1,"通知"),
    STATUS(2,"状态"),
    UPDATE_CHART(3, "更新图表");
    private Integer code;
    private String desc;

    WSMessageType(Integer code, String desc) {
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
