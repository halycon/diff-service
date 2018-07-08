package com.diffservice.repository.impl;

import com.diffservice.model.repository.DiffData;
import com.diffservice.model.service.DiffSide;
import com.diffservice.repository.IDiffRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("DiffServiceRepository")
public class DiffServiceRepository implements IDiffRepository<BigInteger,DiffData,DiffSide> {

    private static Map<BigInteger, DiffData> map = new ConcurrentHashMap<>();

    @Override
    public void save(BigInteger id, String diffData, DiffSide diffSide) throws IllegalStateException{
        switch (diffSide){
            case left:
                map.computeIfAbsent(id, k -> new DiffData(id,diffData,diffSide)).setLeftdata(diffData);
                break;
            case right:
                map.computeIfAbsent(id, k -> new DiffData(id,diffData,diffSide)).setRightdata(diffData);
                break;
        }
    }

    @Override
    public DiffData findById(BigInteger id) {
        return map.get(id);
    }
}
