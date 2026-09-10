package com.suvin.nepia.global.config;

import com.suvin.nepia.global.config.properties.NepiaEnv;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author 박 수 빈
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties(NepiaEnv.class)
public class WebConfig implements WebMvcConfigurer {

    private final String prefix;

    public WebConfig(NepiaEnv env) {
        this.prefix = env.api().prefix();
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(prefix, HandlerTypePredicate.forAnnotation(RestController.class))
                  .setPathMatcher(new AntPathMatcher())
                  .setUrlPathHelper(new UrlPathHelper());
    }
}