package com.diffservice.integration;

import com.diffservice.model.service.DiffResponse;
import com.diffservice.model.service.DiffSide;
import com.diffservice.service.IDiffService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffServiceIntegrationTest{

    @Resource(name = "DiffServiceV1")
    private IDiffService<String> diffService;

    @Before
    public void init(){
        diffService.saveDiffData(BigInteger.valueOf(1),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.left);
        diffService.saveDiffData(BigInteger.valueOf(1),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.right);

        diffService.saveDiffData(BigInteger.valueOf(2),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.left);
        diffService.saveDiffData(BigInteger.valueOf(3),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.right);

        diffService.saveDiffData(BigInteger.valueOf(4),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.left);
        diffService.saveDiffData(BigInteger.valueOf(4),"aGFzdGEgbcSxc8SxbiBrYXJkZcWfaW0gdm9sa2Fu",DiffSide.right);

        diffService.saveDiffData(BigInteger.valueOf(6),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.left);
        diffService.saveDiffData(BigInteger.valueOf(6),"dGVzdCB2b2xrYW4gdGVzdA==",DiffSide.right);
    }

    @Test
    public void getDiffById_Id5_ReturnsNoDataPresent() {
        Assert.assertThat(diffService.getDiffById(BigInteger.valueOf(5)).getErrorMessage(),
                Matchers.is("No data is present"));
    }

    @Test
    public void getDiffById_Id1_ReturnsEqualsResponse() {
        Assert.assertThat(diffService.getDiffById(BigInteger.valueOf(1)).getMessage(),
                Matchers.is("Left data is equal to right data"));
    }

    @Test
    public void getDiffById_Id2_ReturnsRightDataNotExist() {
        Assert.assertThat(diffService.getDiffById(BigInteger.valueOf(2)).getErrorMessage(),
                Matchers.is("Right data is not present"));
    }

    @Test
    public void getDiffById_Id3_ReturnsLeftDataNotExist() {
        Assert.assertThat(diffService.getDiffById(BigInteger.valueOf(3)).getErrorMessage(),
                Matchers.is("Left data is not present"));
    }

    @Test
    public void getDiffById_Id4_ReturnsSizesDontMatch() {
        Assert.assertThat(diffService.getDiffById(BigInteger.valueOf(4)).getMessage(),
                Matchers.is("Left data size is not equal to right data size"));
    }

    @Test
    public void getDiffById_Id6_ReturnsOffSetsAndLengths() {
        Assert.assertThat(diffService.getDiffById(BigInteger.valueOf(6)).getMessage(),
                Matchers.is("data differences found :  (offset : 3 length : 3) (offset : 12 length : 1) (offset : 17 length : 3)"));
    }

    @Test
    public void saveDiffData_Id7Left_ReturnsSuccess() {
        Assert.assertThat(diffService.saveDiffData(BigInteger.valueOf(7),
                "dGVrZSB2b2xrbW4gdHBydA==",DiffSide.left).getMessage(),
                Matchers.is("encoded data is stored id : 7 side : left"));
    }

    @Test
    public void saveDiffData_Id8Left_ReturnsNotABase64EncodingError() {
        Assert.assertThat(diffService.saveDiffData(BigInteger.valueOf(8),
                "dGVrZSB2b2xrbW4gdHBydA124",DiffSide.left).getErrorMessage(),
                Matchers.is("error : Last unit does not have enough valid bits"));
    }


    @Test
    public void getDiffById_ConcurrencyTest_Id6_ReturnsDataLockedErrorOrDataDifference() {
        int threadCount = 500;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        final CountDownLatch finished = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    DiffResponse response = diffService.getDiffById(BigInteger.valueOf(6));
                    String responseMessage = response.getMessage()!=null ? response.getMessage() : response.getErrorMessage();
                    Assert.assertThat(responseMessage,
                            Matchers.anyOf(Matchers.is("data differences found :  (offset : 3 length : 3) (offset : 12 length : 1) (offset : 17 length : 3)"),
                                    Matchers.is("Data is locked and being processed right now!")));
                }
                finally {
                    finished.countDown();
                }
            });
        }

    }


}
