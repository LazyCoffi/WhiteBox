package com.cheng.whiteboxmodel.utils;


import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class RestResponse {

    private final Integer code;
    private final String msg;
    private final Object data;
    private final LocalDateTime timestamp = LocalDateTime.now();

    /* 常用静态字段 */
    public static final RestResponse OK = new RestResponse(HttpStatus.OK);
    public static final RestResponse FAIL = new RestResponse(HttpStatus.INTERNAL_SERVER_ERROR);

    /* 常用静态方法 */
    public static RestResponse ok(Object data) {
        return new RestResponse(HttpStatus.OK, data);
    }

    public static RestResponse fail(String msg) {
        return new RestResponse(500, msg);
    }

    /* 构造方法 */
    public RestResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RestResponse(Integer code, String msg) {
        this(code, msg, null);
    }

    public RestResponse(HttpStatus httpStatus) {
        this(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public RestResponse(HttpStatus httpStatus, Object data) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), data);
    }

    /* Getter */
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Resullt{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}

