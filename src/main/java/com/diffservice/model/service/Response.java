package com.diffservice.model.service;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class Response implements Serializable {

    private static final long serialVersionUID = -3937577411653411113L;

    private boolean success;
    private String errorMessage;

    @ApiModelProperty(value = "operation success status")
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @ApiModelProperty(value = "contains operation response error message" , allowEmptyValue = true)
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
