package com.diffservice.service.impl.v1;

import com.diffservice.model.repository.DiffData;
import com.diffservice.model.service.DiffResponse;
import com.diffservice.model.service.DiffSide;
import com.diffservice.repository.IDiffRepository;
import com.diffservice.service.IDiffService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Base64;


@Service("DiffServiceV1")
public class DiffService implements IDiffService<String> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="DiffServiceRepository")
    private IDiffRepository<BigInteger,DiffData,DiffSide> diffRepository;

    @Override
    public DiffResponse saveDiffData(BigInteger id, String diffData, DiffSide diffSide){
        try {
            validateBase64EncodedData(diffData);

            diffRepository.save(id, diffData, diffSide);

            return new DiffResponse("encoded data is stored id : "+id+" side : "+diffSide.toString(),true);
        } catch (Exception e) {
            logger.error("error :: {}",e);
            return new DiffResponse("error : "+e.getMessage(),false);
        }
    }

    @Override
    public DiffResponse getDiffById(BigInteger id) {

        DiffData data = diffRepository.findById(id);

        if(data==null) {
            return new DiffResponse("No data is present", false);
        }
        if(data.isWriteLock())
            return new DiffResponse("Data is locked and being processed right now!", false);

        data.setWriteLock(true);
        if(Strings.isBlank(data.getLeftdata())) {
            data.setWriteLock(false);
            return new DiffResponse("Left data is not present", false);
        }
        if(Strings.isBlank(data.getRightdata())) {
            data.setWriteLock(false);
            return new DiffResponse("Right data is not present", false);
        }
        if(data.getLeftdata().equals(data.getRightdata())) {
            data.setWriteLock(false);
            return new DiffResponse("Left data is equal to right data", true);
        }
        if(data.getLeftdata().length() != data.getRightdata().length()) {
            data.setWriteLock(false);
            return new DiffResponse("Left data size is not equal to right data size", true);
        }
        else {
            DiffResponse response = findDifferences(data);
            data.setWriteLock(false);
            return response;
        }

    }

    public DiffResponse findDifferences(DiffData data){
        StringBuilder builder = new StringBuilder();
        long offset = 0;
        long diffLength = 0;
        boolean identical = true;

        builder.append("data differences found : ");
        for (int index = 0; index < data.getLeftdata().length(); index++) {

            if(data.getLeftdata().charAt(index) != data.getRightdata().charAt(index)){
                if(identical){
                    offset = index;
                    identical = false;
                }
                diffLength++;
            }else
            if(!identical) {
                builderAppendDifferences(builder,offset,diffLength);
                diffLength = 0;
                identical = true;
            }

        }

        if(!identical)
            builderAppendDifferences(builder,offset-1,diffLength);


        return new DiffResponse(builder.toString(),true);
    }

    public void validateBase64EncodedData(String data) throws IllegalArgumentException  {
        Base64.getDecoder().decode(data);
    }

    private StringBuilder builderAppendDifferences(StringBuilder builder,long offset, long diffLength){
        builder.append(" (offset : ");
        builder.append(offset);
        builder.append(" length : ");
        builder.append(diffLength);
        builder.append(")");
        return builder;
    }
}
