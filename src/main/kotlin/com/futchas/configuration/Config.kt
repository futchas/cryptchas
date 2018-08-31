package com.futchas.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


@Configuration
class Config {
    @Bean
    fun restTemplate() = RestTemplate()

//    @Bean
//    fun taskScheduler(): TaskScheduler {
//        return ConcurrentTaskScheduler()
//    }

    @Bean
    fun scheduledExecutorService(): ScheduledExecutorService {
        return Executors.newScheduledThreadPool(50)
    }

}