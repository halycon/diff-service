package com.diffservice.model.service;

import java.io.Serializable;

public class Response implements Serializable {

    private static final long serialVersionUID = -3937577411653411113L;

    private boolean success;
    private String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
