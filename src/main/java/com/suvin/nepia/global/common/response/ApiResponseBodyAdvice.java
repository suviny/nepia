package com.suvin.nepia.global.common.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * 컨트롤러에서 {@link ResponseEntity} 사용해 반환되는 응답 데이터를 가로채 {@link ApiResponse}로 래핑을 수행하는 응답 전처리 클래스
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@RestControllerAdvice(basePackages = "com.suvin.nepia.domain")
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.hasMethodAnnotation(ExceptionHandler.class)) {
            return false;
        }
        return ResponseEntity.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        ApiEnum apiEnum = ApiEnum.resolve(getHttpStatus(response));
        if (ObjectUtils.isEmpty(body)) {
            // 컨트롤러 메소드의 반환타입을 가져온다.
            Type type = returnType.getGenericParameterType();
            if (type instanceof ParameterizedType parameterizedType) {
                // 내부 실제 제네릭 타입(T)을 가져온다.
                type = parameterizedType.getActualTypeArguments()[0];
            }
            body = getDefaultBody(type);
        }
        return ApiResponse.success(apiEnum, body);
    }

    private HttpStatus getHttpStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse serverHttpResponse) {
            int status = serverHttpResponse.getServletResponse().getStatus();
            HttpStatus resolved = HttpStatus.resolve(status);
            if (resolved != null) {
                return resolved;
            }
        }
        return HttpStatus.OK;
    }

    private Object getDefaultBody(Type actualType) {
        if (actualType instanceof ParameterizedType parameterizedType) {
            // 제네릭 정보(T<?>)를 제외한 원본 타입(T)을 가져온다.
            actualType = parameterizedType.getRawType();
        }
        boolean isList = actualType instanceof Class<?> clazz && List.class.isAssignableFrom(clazz);
        return isList ? Collections.emptyList() : Collections.emptyMap();
    }
}