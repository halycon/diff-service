package com.diffservice.controller;

import com.diffservice.model.service.DiffRequest;
import com.diffservice.model.service.DiffResponse;
import com.diffservice.model.service.DiffSide;
import com.diffservice.service.IDiffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;

@RestController
@Api(value = "/", description = "Diff Service Operations")
public class DiffServiceController {


    @Resource(name = "DiffServiceV1")
    private IDiffService<String> diffServiceV1;

    @ApiOperation(value = "Operation to store base64 encoded binary string with id and side values",
            notes = "Either 'left' or 'right' enum can be provided to {diffSide}",
            response = DiffResponse.class)
    @RequestMapping(value = "/v1/diff/{id}/{diffSide}", method = {RequestMethod.PUT, RequestMethod.POST} , produces = "application/json")
    private DiffResponse saveDiffData(@PathVariable BigInteger id, @PathVariable DiffSide diffSide, @RequestBody DiffRequest diffRequest){
        return diffServiceV1.saveDiffData(id,diffRequest.getData(),diffSide);
    }

    @ApiOperation(value = "Operation to apply diff by id on left and right side of data structure",
            response = DiffResponse.class)
    @RequestMapping(value = "/v1/diff/{id}", method = {RequestMethod.GET} , produces = "application/json")
    private DiffResponse getDiffById(@PathVariable BigInteger id){
        return diffServiceV1.getDiffById(id);
    }

    @ApiOperation(value = "Operation to get health check information of diff-service application")
    @RequestMapping(value = "/status", method = {RequestMethod.GET} , produces = "application/json")
    private String status(){
        return "{ \"status\" : \"up\" }";
    }

}
