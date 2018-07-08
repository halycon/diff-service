package com.diffservice.model.service;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiffResponse extends Response {

    private static final long serialVersionUID = -6946077069486750192L;
    private String message;

    public DiffResponse(String message,boolean success){
        super();
        if(success){
            this.setSuccess(success);
            this.message = message;
        }else
            this.setErrorMessage(message);

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
