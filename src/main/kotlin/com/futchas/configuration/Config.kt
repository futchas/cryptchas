package com.futchas.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import java.util.concurrent.*


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
        return Executors.newScheduledThreadPool(10)
    }

//    @Bean
//    @Scope("prototype")
//    fun logger(injectionPoint: InjectionPoint) : Logger {
//        return LoggerFactory.getLogger(injectionPoint.methodParameter.containingClass)
//    }
}