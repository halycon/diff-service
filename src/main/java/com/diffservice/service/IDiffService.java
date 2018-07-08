package com.diffservice.service;

import com.diffservice.model.service.DiffResponse;
import com.diffservice.model.service.DiffSide;

import java.math.BigInteger;

public interface IDiffService<T> {
    DiffResponse saveDiffData(BigInteger id, T diffData, DiffSide diffSide);
    DiffResponse getDiffById(BigInteger id);
}
