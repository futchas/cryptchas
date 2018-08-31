package com.futchas.globalmarket

import com.futchas.CryptoApiProvider
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class GlobalMarketInfoController(private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(GlobalMarketInfoController::class.java)

    @MessageMapping("/global-market-info")
//    @GetMapping("/global-market-info")
    @SendTo("/notifier/info")
    fun globalMarket(): GlobalMarketInfo {
        val globalMarket = restTemplate.getForObject("${CryptoApiProvider.COINMARKETCAP.value}/global/", GlobalMarketInfo::class.java)
                ?: throw RuntimeException("Global Market Service is null! Data from API couldn't be retrieved")

        logger.info("Global market info: $globalMarket")
        return globalMarket
    }

}