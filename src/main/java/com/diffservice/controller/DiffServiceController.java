package com.diffservice.controller;

import com.diffservice.model.service.DiffRequest;
import com.diffservice.model.service.DiffResponse;
import com.diffservice.model.service.DiffSide;
import com.diffservice.service.IDiffService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;

@RestController
public class DiffServiceController {

    @Resource(name = "DiffServiceV1")
    private IDiffService<String> diffServiceV1;

    @RequestMapping(value = "/v1/diff/{id}/{diffSide}", method = {RequestMethod.PUT, RequestMethod.POST} , produces = "application/json")
    private DiffResponse saveDiffData(@PathVariable BigInteger id, @PathVariable DiffSide diffSide, @RequestBody DiffRequest diffRequest){
        return diffServiceV1.saveDiffData(id,diffRequest.getData(),diffSide);
    }

    @RequestMapping(value = "/v1/diff/{id}", method = {RequestMethod.GET} , produces = "application/json")
    private DiffResponse getDiffById(@PathVariable BigInteger id){
        return diffServiceV1.getDiffById(id);
    }

    @RequestMapping(value = "/status", method = {RequestMethod.GET} , produces = "application/json")
    private String status(){
        return "{ \"status\" : \"up\" }";
    }

}
