package com.example.notification.config;

import com.example.notification.common.BaseException;
import com.example.notification.common.BaseResponseStatus;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        BaseResponseStatus status = mapToStatus(response.status()); // 응답 코드 매핑
        return new BaseException(status);
    }

    private BaseResponseStatus mapToStatus(int statusCode) {
        switch (statusCode) {
            case 400: return BaseResponseStatus.INTERNAL_SERVER_ERROR1;
            case 500: return BaseResponseStatus.INTERNAL_SERVER_ERROR1;
            default: return BaseResponseStatus.INTERNAL_SERVER_ERROR1;
        }
    }
}