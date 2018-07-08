package com.diffservice.model.repository;

import com.diffservice.model.service.DiffSide;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public class DiffData implements Serializable {

    private static final long serialVersionUID = -3320826542558409754L;

    private BigInteger id;

    private String leftdata;

    private String rightdata;

    private boolean writeLock = false;

    public DiffData(BigInteger id, String data, DiffSide side){
        this.id = id;
        switch (side){
            case left:
                this.leftdata = data;
                break;
            case right:
                this.rightdata = data;

                break;
            default:
                this.leftdata = data;
        }
    }

    public boolean isWriteLock() {
        return writeLock;
    }

    public void setWriteLock(boolean writeLock) {
        this.writeLock = writeLock;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getLeftdata() {
        return leftdata;
    }

    public void setLeftdata(String leftdata) throws IllegalStateException {
        if(!writeLock)
            this.leftdata = leftdata;
        else
            throw new IllegalStateException("Cannot modify leftdata. It is being processed right now!");
    }

    public String getRightdata() {
        return rightdata;
    }

    public void setRightdata(String rightdata) throws IllegalStateException{
        if(!writeLock)
            this.rightdata = rightdata;
        else
            throw new IllegalStateException("Cannot modify rightdata. It is being processed right now!");
    }

    @Override
    public String toString() {
        return "DiffData{" +
                "id=" + id.intValue() +
                ", leftdata='" + leftdata + '\'' +
                ", rightdata='" + rightdata + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiffData data = (DiffData) o;
        return Objects.equals(id, data.id) &&
                Objects.equals(leftdata, data.leftdata) &&
                Objects.equals(rightdata, data.rightdata);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, leftdata, rightdata);
    }

}
