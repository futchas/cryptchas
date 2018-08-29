package com.futchas

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ScheduledExecutorService

@RestController
class Cryptchas {

    private val logger = LoggerFactory.getLogger(Cryptchas::class.java)

    @Autowired
    lateinit var scheduledExecutorService: ScheduledExecutorService

    @MessageMapping("/disconnect")
    fun stopScheduler() {
        logger.info("Global scheduler disabled!")
        scheduledExecutorService.shutdown()
    }
}