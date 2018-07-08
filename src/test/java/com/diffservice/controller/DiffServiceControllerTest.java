package com.diffservice.controller;

import com.diffservice.model.service.DiffRequest;
import com.diffservice.model.service.DiffResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiffServiceControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void v1_diff_41_left_requestsuccessful(){

        ResponseEntity<DiffResponse> response = testRestTemplate.
                postForEntity("/v1/diff/41/left",prepareHttpEntity(new DiffRequest("dGVrZSB2b2xrbW4gdHBydA==")),
                        DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("encoded data is stored id : 41 side : left",response.getBody().getMessage());
    }

    @Test
    public void v1_diff_41_right_responseStoreSuccessful(){
        ResponseEntity<DiffResponse> response = testRestTemplate.
                postForEntity("/v1/diff/41/right",prepareHttpEntity(new DiffRequest("dGVrZSB2b2xrbW4gdHBydA==")),
                        DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("encoded data is stored id : 41 side : right",response.getBody().getMessage());
    }

    @Test
    public void v1_diff_42_right_responseReturnsBase64RelatedExceptionMessage(){
        ResponseEntity<DiffResponse> response = testRestTemplate.
                postForEntity("/v1/diff/42/right",prepareHttpEntity(new DiffRequest("dGVrZSB2b2xrbW4gdHBydA124")),
                        DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("error : Last unit does not have enough valid bits",response.getBody().getErrorMessage());
    }

    @Test
    public void v1_diff_42_responseReturnsBase64RelatedExceptionMessage(){
        ResponseEntity<DiffResponse> response = testRestTemplate.
                postForEntity("/v1/diff/41/right",prepareHttpEntity(new DiffRequest("dGVrZSB2b2xrbW4gdHBydA124")),
                        DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("error : Last unit does not have enough valid bits",response.getBody().getErrorMessage());
    }

    @Test
    public void v1_diff_43_responseReturnsNoDataPresent() {
        ResponseEntity<DiffResponse> response = testRestTemplate.
                getForEntity("/v1/diff/43",DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("No data is present",response.getBody().getErrorMessage());
    }

    @Test
    public void v1_diff_41_responseReturnsEqualsResponse() {
        ResponseEntity<DiffResponse> response = testRestTemplate.
                getForEntity("/v1/diff/41",DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Left data is equal to right data",response.getBody().getMessage());
    }

    @Test
    public void v1_diff_44_responseReturnsRightDataNotExist() {
        testRestTemplate.postForEntity("/v1/diff/44/left",prepareHttpEntity(new DiffRequest("dGVrZSB2b2xrbW4gdHBydA==")),
                        DiffResponse.class);

        ResponseEntity<DiffResponse> response = testRestTemplate.
                getForEntity("/v1/diff/44",DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Right data is not present",response.getBody().getErrorMessage());
    }

    @Test
    public void v1_diff_45_responseReturnsLeftDataNotExist() {
        testRestTemplate.postForEntity("/v1/diff/45/right",prepareHttpEntity(new DiffRequest("dGVrZSB2b2xrbW4gdHBydA==")),
                DiffResponse.class);

        ResponseEntity<DiffResponse> response = testRestTemplate.
                getForEntity("/v1/diff/45",DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Left data is not present",response.getBody().getErrorMessage());

    }

    @Test
    public void v1_diff_46_responseReturnsSizesDontMatch() {
        testRestTemplate.postForEntity("/v1/diff/46/right",prepareHttpEntity(
                new DiffRequest("dGVrZSB2b2xrbW4gdHBydA==")),
                DiffResponse.class);

        testRestTemplate.postForEntity("/v1/diff/46/left",prepareHttpEntity(
                new DiffRequest("aGFzdGEgbcSxc8SxbiBrYXJkZcWfaW0gdm9sa2Fu")),
                DiffResponse.class);

        ResponseEntity<DiffResponse> response = testRestTemplate.
                getForEntity("/v1/diff/46",DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Left data size is not equal to right data size",response.getBody().getMessage());

    }

    @Test
    public void v1_diff_47_responseReturnsOffSetsAndLengths() {
        testRestTemplate.postForEntity("/v1/diff/47/right",prepareHttpEntity(
                new DiffRequest("dGVrZSB2b2xrbW4gdHBydA==")),
                DiffResponse.class);

        testRestTemplate.postForEntity("/v1/diff/47/left",prepareHttpEntity(
                new DiffRequest("dGVzdCB2b2xrYW4gdGVzdA==")),
                DiffResponse.class);

        ResponseEntity<DiffResponse> response = testRestTemplate.
                getForEntity("/v1/diff/47",DiffResponse.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("data differences found :  (offset : 3 length : 3) (offset : 12 length : 1) (offset : 17 length : 3)",
                response.getBody().getMessage());

    }


    @Test
    public void v1_diff_48_responseReturnsDataLockedErrorOrDataDifference() {
        testRestTemplate.postForEntity("/v1/diff/48/right",prepareHttpEntity(
                new DiffRequest("dGVrZSB2b2xrbW4gdHBydA==")),
                DiffResponse.class);

        testRestTemplate.postForEntity("/v1/diff/48/left",prepareHttpEntity(
                new DiffRequest("dGVzdCB2b2xrYW4gdGVzdA==")),
                DiffResponse.class);

        int threadCount = 200;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        final CountDownLatch finished = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    ResponseEntity<DiffResponse> response = testRestTemplate.
                            getForEntity("/v1/diff/48",DiffResponse.class);
                    String responseMessage = response.getBody().getMessage()!=null ? response.getBody().getMessage()
                            : response.getBody().getErrorMessage();

                    assertEquals(HttpStatus.OK,response.getStatusCode());
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


    private HttpEntity<DiffRequest> prepareHttpEntity(DiffRequest diffRequest){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return new HttpEntity<>(diffRequest, headers);
    }

}
