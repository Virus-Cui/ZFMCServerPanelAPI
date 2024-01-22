package cn.mrcsh.zfmcserverpanelapi.entity.enums;

public enum ErrorCode {

    FILE_TYPE_IS_NOT_EXISTS(4001, "文件格式不正确"),
    NOT_LOGIN(5001, "没有登录"),
    SERVER_ERROR(5000, "服务器错误");
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
