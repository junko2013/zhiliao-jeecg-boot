package org.jeecg.modules.im.base.exception;
//ip 拒绝访问
public class IpDenyException extends Exception{
    public IpDenyException() {
        super();
    }

    public IpDenyException(String message) {
        super(message);
    }

    public IpDenyException(String message, Throwable cause) {
        super(message, cause);
    }
}
