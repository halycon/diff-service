package com.diffservice.model.service;

import java.io.Serializable;

public class DiffRequest implements Serializable {

    private static final long serialVersionUID = 515221881737318279L;
    private String data;

    public DiffRequest(){

    }

    public DiffRequest(String data){
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
