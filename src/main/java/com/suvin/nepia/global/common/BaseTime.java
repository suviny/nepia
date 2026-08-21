package com.suvin.nepia.global.common;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 데이터 생성 일시 및 변경 일시를 공통으로 관리하는 추상 클래스
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@Getter
public abstract class BaseTime {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * {@code INSERT} 실행 전, 생성 및 변경 일시를 현재 시점으로 주입한다.
     *
     * @param now   데이터 생성 시점의 일시 정보
     */
    public void beforeInsert(LocalDateTime now) {
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * {@code UPDATE} 실행 전, 변경 일시를 현재 시점으로 주입한다.
     *
     * @param now   데이터 변경 시점의 일시 정보
     */
    public void beforeUpdate(LocalDateTime now) {
        this.updatedAt = now;
    }
}