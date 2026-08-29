package com.suvin.nepia.global.common;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 공통적으로 사용되는 생성일과 변경일 필드를 관리하는 추상 클래스
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@Getter
public abstract class BaseTime {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * {@code INSERT} 실행 전, 생성 및 변경 일시를 현재 시점의 일시 정보로 주입한다.
     *
     * @param dateTime  데이터 생성 시점의 일시 정보
     */
    public void preInsert(LocalDateTime dateTime) {
        this.createdAt = dateTime;
        this.updatedAt = dateTime;
    }

    /**
     * {@code UPDATE} 실행 전, 변경 일시를 현재 시점의 일시 정보로 주입한다.
     *
     * @param dateTime  데이터 변경 시점의 일시 정보
     */
    public void preUpdate(LocalDateTime dateTime) {
        this.updatedAt = dateTime;
    }
}