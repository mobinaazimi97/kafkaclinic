package com.mftplus.patient.config;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() >= 500) {
                return new RetryableException(
                        response.status(),
                        "Server error: " + response.reason(),
                        response.request().httpMethod(),
                        (Long) null,
                        response.request()
                );
            }
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}