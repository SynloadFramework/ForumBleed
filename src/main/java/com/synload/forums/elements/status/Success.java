package com.synload.forums.elements.status;

import com.synload.framework.handlers.Response;
import com.synload.framework.ws.WSHandler;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class Success extends Response {
    public String message;
    public int code;
    public Success(int code, String message){
        this.setCode(code);
        this.setMessage(message);
        this.setCallEvent("success");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
