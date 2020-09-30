package yc.com.rthttplibrary.exception;

/**
 * Created by suns  on 2020/7/25 09:39.
 */

public class ApiException extends RuntimeException {
    private String errorMsg;
    private int errorCode;

    public ApiException(int code, String msg) {
        super(msg);
        this.errorCode = code;
        this.errorMsg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
