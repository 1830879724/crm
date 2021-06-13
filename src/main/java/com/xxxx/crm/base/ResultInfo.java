package com.xxxx.crm.base;

public class ResultInfo {
    private Integer code=200;//代表成功反之不成功
    private String msg="success";//代表成功反之不成功

    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
