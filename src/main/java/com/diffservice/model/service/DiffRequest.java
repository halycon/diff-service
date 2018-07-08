package com.diffservice.model.service;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class DiffRequest implements Serializable {

    private static final long serialVersionUID = 515221881737318279L;
    private String data;

    public DiffRequest(){

    }

    public DiffRequest(String data){
        this.data = data;
    }

    @ApiModelProperty(value = "a valid base64 encoded binary data should be provided.")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
