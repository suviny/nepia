package com.suvin.nepia.global.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.suvin.nepia.global.config.properties.NepiaEnv;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

/**
 * 데이터의 JSON 변환 처리를 위해 구현한 커스텀 설정 클래스
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties(NepiaEnv.class)
public class JacksonConfig {

    private final String dateFormat;

    private final String dateTimeFormat;

    public JacksonConfig(NepiaEnv env) {
        this.dateFormat = env.format().date();
        this.dateTimeFormat = env.format().dateTime();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                .modules(new JavaTimeModule())
                /* 자바 객체 및 데이터 직렬화 설정 */
                .serializers(
                    new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)),
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)))
                /* 클라이언트로부터 전달된 JSON 데이터의 역직렬화 설정 */
                .deserializers(
                    new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)),
                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)))
                /* 날짜 데이터의 타임 스탬프 변환 설정 비활성화 */
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}