package cn.mrcsh.zfmcserverpanelapi.entity.enums;

public enum ErrorCode {

    FILE_TYPE_IS_NOT_EXISTS(4001, "文件格式不正确");
    private Integer code;
    private String msg;

    ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
