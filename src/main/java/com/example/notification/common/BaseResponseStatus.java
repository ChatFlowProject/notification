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
    INTERNAL_SERVER_ERROR1(false, 2004, "서버 에러입니다."),

    // 알림 기능 - 3000
    MY_NOTI_READ_SUCCESS(false, 3000, "나의 알림을 조회하는데 성공했습니다."),
    NOTI_UPDATE_SUCCESS(false, 3001, "알림을 읽음 처리하는데 성공하였습니다.");


    private final boolean isSuccess;
    private final Integer code;
    private final String message;

    BaseResponseStatus(Boolean isSuccess, Integer code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
