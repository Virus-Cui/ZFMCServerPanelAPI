package cn.mrcsh.zfmcserverpanelapi.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.mrcsh.zfmcserverpanelapi.controller.ABaseController;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ErrorCode;
import cn.mrcsh.zfmcserverpanelapi.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class XExceptionHandler extends ABaseController {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public response busExceptionHandler(HttpServletRequest req, BusinessException e){
        log.error("\n运行错误-业务错误 [原因: ${}] StackTrace ↓",e.getMessage(),e);
        return error(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public response AllExceptionHandler(HttpServletRequest req, Exception e){
        log.error("\n运行错误-未知错误 [原因: ${}] StackTrace ↓",e.getMessage(),e);
        if(e instanceof NotLoginException){
            return error(ErrorCode.NOT_LOGIN);
        }
        return error(ErrorCode.SERVER_ERROR);
    }
}
