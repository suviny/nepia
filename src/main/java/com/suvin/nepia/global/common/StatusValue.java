package com.suvin.nepia.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 프로젝트 전역에서 사용될 특정 도메인에 대한 상태값 처리를 위해 서로 다른 타입의 데이터들을 상응되는 의미별로 정의한 열거형
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum StatusValue {

    YES (1, "Y", true),
    NO (0, "N", false);

    private final int number;

    private final String string;
    
    private final boolean condition;

    /**
     * 전달된 정수에 해당하는 논리형 상태값을 반환한다.
     * 
     * @param source    논리형 상태값으로 변환하고자 하는 정수
     * @return          전달된 값이 {@code 1}일 경우 {@code true}, {@code 0}일 경우 {@code false} 반환
     */
    public static boolean booleanOf(int source) {
        for (StatusValue e : values()) {
            if (e.getNumber() == source) {
                return e.isCondition();
            }
        }
        throw new IllegalArgumentException(
                "No constant found for '" + source + "'.");
    }

    /**
     * 전달된 문자열에 해당하는 논리형 상태값을 반환한다.
     * 
     * @param source    논리형 상태값으로 변환하고자 하는 문자열
     * @return          전달된 값이 {@code "Y"}일 경우 {@code true}, {@code "N"}일 경우 {@code false} 반환
     */
    public static boolean booleanOf(String source) {
        for (StatusValue e : values()) {
            if (e.getString().equals(source)) {
                return e.isCondition();
            }
        }
        throw new IllegalArgumentException(
                "No constant found for '" + source + "'.");
    }
}