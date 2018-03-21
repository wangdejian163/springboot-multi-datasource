package com.bonc.casefile.policestation.exception;

import java.io.Serializable;

/**
 * <p>
 * 错误消息实体
 * </p>
 *
 * @author wangdejian
 * @since 2018/3/21
 */
public class ErrorInfo implements Serializable {
    private static final long serialVersionUID = 6001966082338217014L;

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
