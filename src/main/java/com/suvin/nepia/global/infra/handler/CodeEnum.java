package com.suvin.nepia.global.infra.handler;

/**
 * 데이터베이스 테이블의 코드 컬럼과 매핑될 열거형의 공통 규약을 정의한 인터페이스
 *
 * @author PARK SU BIN
 * @version 1.0
 *
 * @see CodeEnumTypeHandler
 */
public interface CodeEnum {

    /**
     * 매핑될 문자열 타입의 코드값을 반환한다.
     */
    String getCode();

    /**
     * 화면에 표시될 코드값에 대한 라벨을 반환한다.
     */
    String getLabel();
}