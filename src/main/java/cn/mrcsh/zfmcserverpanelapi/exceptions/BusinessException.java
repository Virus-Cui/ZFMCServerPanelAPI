package cn.mrcsh.zfmcserverpanelapi.exceptions;

import cn.mrcsh.zfmcserverpanelapi.entity.enums.ErrorCode;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException{

    private ErrorCode errorCode;

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(ErrorCode errorCode){
        super();
        this.errorCode = errorCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }
}
