package com.diffservice.repository;

import com.diffservice.model.repository.DiffData;
import com.diffservice.model.service.DiffSide;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffRepositoryTest {

    @Resource(name = "DiffServiceRepository")
    private IDiffRepository<BigInteger,DiffData,DiffSide> diffRepository;

    @Before
    public void init(){
        diffRepository.save(BigInteger.valueOf(11),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.left);
        diffRepository.save(BigInteger.valueOf(21),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.left);

    }

    @Test
    public void getById_Id10_ReturnsNoDataPresent() {
        Assert.assertThat(diffRepository.findById(BigInteger.TEN),
                Matchers.nullValue());
    }

    @Test
    public void getById_Id11_ReturnsDataPresent() {
        Assert.assertThat(diffRepository.findById(BigInteger.valueOf(11)),
                Matchers.notNullValue());
    }

    @Test
    public void save_Id22_NoException() {
        diffRepository.save(BigInteger.valueOf(22),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.right);
        Assert.assertThat(diffRepository.findById(BigInteger.valueOf(22)),
                Matchers.notNullValue());

    }

    @Test
    public void save_Id21_Exception() {

        try {
            diffRepository.findById(BigInteger.valueOf(21)).setWriteLock(true);
            diffRepository.save(BigInteger.valueOf(21),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.right);
        } catch (Exception e) {
            diffRepository.findById(BigInteger.valueOf(21)).setWriteLock(false);
            Assertions.assertThat(e).isInstanceOf(IllegalStateException.class)
                    .hasMessage("Cannot modify rightdata. It is being processed right now!");
        }

    }

    @Test
    public void save_Id22_DifferentAfterUpdate() {


        diffRepository.save(BigInteger.valueOf(22),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.right);
        diffRepository.save(BigInteger.valueOf(22),"dGVrZSB2b2xrbW4gdHBydA==",DiffSide.left);
        String rightdata1 = diffRepository.findById(BigInteger.valueOf(21)).getRightdata();
        diffRepository.save(BigInteger.valueOf(22),"dGVzdCB2b2xrYW4gdGVzdA==",DiffSide.right);
        String rightdata2 = diffRepository.findById(BigInteger.valueOf(21)).getRightdata();
        Assert.assertEquals(rightdata1,rightdata2);

    }
}
