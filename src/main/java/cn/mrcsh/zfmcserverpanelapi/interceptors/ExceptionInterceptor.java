package cn.mrcsh.zfmcserverpanelapi.interceptors;

import cn.dev33.satoken.exception.NotLoginException;
import cn.mrcsh.zfmcserverpanelapi.controller.ABaseController;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ErrorCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionInterceptor extends ABaseController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public response exceptionHandler(Exception e){
        if(e instanceof NotLoginException){
            return error(ErrorCode.NOT_LOGIN);
        }
        return error(ErrorCode.SERVER_ERROR);
    }
}
