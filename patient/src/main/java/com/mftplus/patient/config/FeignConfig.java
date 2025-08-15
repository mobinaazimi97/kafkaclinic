package com.mftplus.patient.config;

import feign.Client;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

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

    //Manage the SSL For Appointment Endpoints :
    @Bean
    public Client feignClient() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        javax.net.ssl.HostnameVerifier hostnameVerifier = (hostname, session) -> true;

        return new Client.Default(socketFactory, hostnameVerifier);
    }
}