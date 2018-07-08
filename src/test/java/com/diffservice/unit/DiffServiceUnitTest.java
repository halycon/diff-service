package com.diffservice.unit;

import com.diffservice.model.repository.DiffData;
import com.diffservice.model.service.DiffSide;
import com.diffservice.service.impl.v1.DiffService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

public class DiffServiceUnitTest {

    private DiffService diffService;

    private DiffData diffData;

    @Before
    public void init(){

        diffService = new DiffService();
        diffData = new DiffData(BigInteger.ONE,"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.left);
        diffData.setRightdata("dGVzdCB2b2xrYW4gdGVzdA==");
    }

    @Test
    public void validateBase64EncodedData_randomData_ThrowsError(){
        try {
            diffService.validateBase64EncodedData("dGVrZSB2b2xrbW4gdHBydA124");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Last unit does not have enough valid bits");
        }

    }

    @Test
    public void findDifferences_correctData_NoError(){
        Assert.assertThat(diffService.findDifferences(diffData).getMessage(),
                Matchers.is("data differences found :  (offset : 3 length : 3) (offset : 12 length : 1) (offset : 17 length : 3)"));
    }

}
