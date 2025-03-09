package com.dripify.config;

import com.dripify.web.interceptor.ModelAttributeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ModelAttributeInterceptor modelAttributeInterceptor;

    public WebConfig(ModelAttributeInterceptor modelAttributeInterceptor) {
        this.modelAttributeInterceptor = modelAttributeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(modelAttributeInterceptor);
    }


}
