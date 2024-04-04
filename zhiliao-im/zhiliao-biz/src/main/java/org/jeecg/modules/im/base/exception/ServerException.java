package org.jeecg.modules.im.base.exception;
//服务器异常
public class ServerException extends Exception{
    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
