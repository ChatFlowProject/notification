package com.example.notification.config;

import com.example.notification.common.BaseException;
import com.example.notification.common.BaseResponseStatus;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        // 로그로 에러 정보 출력
        log.error("Feign error occurred. MethodKey: {}, StatusCode: {}, Reason: {}",
                methodKey, status, response.reason());

        // 상태 코드에 따른 커스텀 예외 처리
        switch (status) {
            case 400:
                return new BaseException(BaseResponseStatus.BAD_ACCESS_TOKEN);
            case 401:
            case 403:
                return new BaseException(BaseResponseStatus.UNAUTHORIZED_CLIENT);
            case 404:
                return new BaseException(BaseResponseStatus.NOT_FOUND); // 추가 처리
            case 500:
                return new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR); // 추가 처리
            default:
                // 예상치 못한 상태 코드에 대한 기본 처리
                return new Exception("Unexpected error occurred: " + response.reason());
        }
    }
}

