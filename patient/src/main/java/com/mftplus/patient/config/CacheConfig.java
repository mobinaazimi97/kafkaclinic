package com.mftplus.patient.config;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    public CacheConfig() {
//        log.info("CacheConfig is caching----!!!!");
    }
}