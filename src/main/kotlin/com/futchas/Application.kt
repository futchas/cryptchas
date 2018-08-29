package com.futchas

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java)
}

@SpringBootApplication
@EnableScheduling
class Application {

//    @Bean
//    fun logger(injectionPoint: InjectionPoint) : Logger {
//        return LoggerFactory.getLogger(injectionPoint.methodParameter.containingClass)
//    }
}




