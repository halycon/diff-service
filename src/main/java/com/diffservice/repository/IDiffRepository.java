package com.diffservice.repository;

public interface IDiffRepository<T,V,H> {

    void save(T t,String s,H h) throws IllegalStateException;
    V findById(T t);

}
