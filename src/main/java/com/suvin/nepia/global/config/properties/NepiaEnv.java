package com.suvin.nepia.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code application.yml} 설정 파일에 정의된 {@code app.*} 속성의 값을 자바 객체로 매핑 및 관리하기 위한 레코드 클래스
 *
 * @author 박 수 빈
 * @version 1.0
 */
@ConfigurationProperties(prefix = "app")
public record NepiaEnv(
    Api api,
    Format format
) {
    public record Api(
        String prefix
    ) {}

    public record Format(
        String date,
        String dateTime
    ) {}
}