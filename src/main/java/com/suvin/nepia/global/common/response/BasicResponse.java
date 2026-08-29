package com.suvin.nepia.global.common.response;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 성공 및 실패 응답 클래스가 공통으로 가져야 할 기본 속성들을 정의한 추상 클래스
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@Getter
public abstract class BasicResponse {

    private final LocalDateTime responseAt;

    private final int code;

    private final String message;

    protected BasicResponse(int code, String message) {
        this.responseAt = LocalDateTime.now();
        this.code = code;
        this.message = message;
    }
}