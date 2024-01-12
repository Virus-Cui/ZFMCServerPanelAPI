package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.mrcsh.zfmcserverpanelapi.entity.enums.ErrorCode;
import lombok.Data;

public class ABaseController {
    @Data
    public class response{
        private Integer code;
        private String msg;
        private Object data;
    }

    public response success(Object data){
        response response = new response();
        response.setCode(2000);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    public response success(){
        response response = new response();
        response.setData(null);
        response.setMsg("success");
        response.setCode(2000);
        return response;
    }

    public response error(ErrorCode errorCode){
        response response = new response();
        response.setCode(errorCode.getCode());
        response.setMsg(errorCode.getMsg());
        response.setData(null);
        return response;
    }


}
