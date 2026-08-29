package com.suvin.nepia.global.common.response;

import lombok.Getter;

/**
 * API 요청의 정상적인 처리 결과에 따른 데이터를 클라이언트에게 반환하기 위한 성공 응답 클래스
 *
 * @param <T> 요청 처리 결과 데이터의 타입
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@Getter
public class ApiResponse<T> extends BasicResponse {

    private final T data;

    private ApiResponse(ApiEnum apiEnum, T data) {
        super(apiEnum.getCode(), apiEnum.getMessage());
        this.data = data;
    }

    /**
     * 성공 응답 객체를 생성한다.
     *
     * @param apiEnum   성공 응답 코드 및 메시지 등의 정보를 담은 열거형 상수
     * @param data      클라이언트에게 전달될 결과 데이터
     * @return          생성된 성공 응답 객체 반환
     * @param <T>       결과 데이터 타입
     */
    public static <T> ApiResponse<T> success(ApiEnum apiEnum, T data) {
        return new ApiResponse<>(apiEnum, data);
    }
}