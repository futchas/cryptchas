package com.futchas

import com.fasterxml.jackson.databind.node.ObjectNode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate


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




