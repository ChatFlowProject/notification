package com.example.notification.common;


import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    // 모든 요청 성공 1000
    SUCCESS(true, 1000, "요청이 성공하였습니다."),

    // 실패 - 40000 (위치 바꾸지 마시오)
    FAIL(false, 40000, "요청에 실패하였습니다."),

    // 유저 기능 - 2000
    UNAUTHORIZED_CLIENT(false, 2001, "인증이 안된 유저입니다."),
    BAD_ACCESS_TOKEN(false, 2002, "유요하지 않는 토큰입니다."),
    NOT_FOUND(false, 2003, "해당 데이터가 없습니다."),
    INTERNAL_SERVER_ERROR(false, 2004, "서버 에러입니다.");


    private final boolean isSuccess;
    private final Integer code;
    private final String message;

    BaseResponseStatus(Boolean isSuccess, Integer code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
