package com.suvin.nepia.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code application.yml} 설정 파일에 정의된 {@code app.*}속성값을 자바 객체로 매핑 및 변환 후 보관하기 위한 레코드
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@ConfigurationProperties(prefix = "app")
public record AppEnv(
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