package com.suvin.nepia.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 응답 본문 구성시 사용될 성공 및 에러 응답에 대한 HTTP 상태 코드 및 커스텀 응답 코드와 메시지 등을 관리하는 열거형
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum ApiEnum {

    /**
     * 공통 성공 응답 코드
     */
    OK (100000, HttpStatus.OK, "요청이 정상적으로 처리되었습니다."),
    CREATED (101000, HttpStatus.CREATED, "새로운 리소스가 생성되었습니다."),

    /**
     * 공통 에러 응답 코드
     */
    INVALID_REQUEST_PARAMETER (-800100, HttpStatus.BAD_REQUEST, "요청 파라미터가 유효하지 않습니다."),
    UNSUPPORTED_HTTP_METHOD (-805100, HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드 요청입니다."),
    INTERNAL_SERVER_ERROR (-900000, HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버에 오류가 발생했습니다."),

    /**
     * 사용자 도메인 에러 응답 코드
     */
    AUTHENTICATION_FAILED (-801310, HttpStatus.UNAUTHORIZED, "사용자 인증에 실패했습니다."),
    ACCESS_DENIED (-803310, HttpStatus.FORBIDDEN, "요청에 대한 접근 권한이 없습니다."),
    USER_NOT_FOUND (-804210, HttpStatus.NOT_FOUND, "요청하신 사용자를 찾을 수 없습니다.")
    ;

    private final int code;

    private final HttpStatus status;

    private final String message;

    /**
     * HTTP 상태 코드에 따른 성공 응답 코드의 열거형 상수를 반환한다.
     *
     * @param status    HTTP 상태 코드
     * @return          전달된 상태 코드가 {@code CREATED}인 경우 해당 상수를, 그렇지 않은 경우 기본 성공 응답인 {@code OK} 상수를 반환
     */
    public static ApiEnum resolve(HttpStatus status) {
        if (status == HttpStatus.CREATED) {
            return ApiEnum.CREATED;
        }
        return ApiEnum.OK;
    }
}