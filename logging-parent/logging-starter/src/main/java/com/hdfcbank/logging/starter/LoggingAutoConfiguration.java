package com.hdfcbank.logging.starter;


import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnClass(RestTemplate.class)
public class LoggingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CorrelationFilter correlationFilter() {
        return new CorrelationFilter();
    }

    @Bean
    public RestTemplateCustomizer loggingRestTemplateCustomizer() {
        return restTemplate -> restTemplate.getInterceptors().add(new InterfaceLoggingInterceptor());
    }

    @Bean
    @ConditionalOnClass(WebClient.class)
    @ConditionalOnMissingBean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().filter(new WebClientLoggingFilter());
    }
}
