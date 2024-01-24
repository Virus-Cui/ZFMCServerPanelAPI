package cn.mrcsh.zfmcserverpanelapi.interceptors;

import cn.dev33.satoken.exception.NotLoginException;
import cn.mrcsh.zfmcserverpanelapi.controller.ABaseController;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionInterceptor extends ABaseController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public response exceptionHandler(Exception e){
        if(e instanceof NotLoginException){
            return error(ErrorCode.NOT_LOGIN);
        }
        if(e instanceof NoResourceFoundException){
            return error(ErrorCode.NOT_FOUNT);
        }
        log.error("错误",e);
        return error(ErrorCode.SERVER_ERROR);
    }
}
