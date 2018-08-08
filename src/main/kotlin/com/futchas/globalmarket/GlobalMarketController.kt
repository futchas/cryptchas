package com.futchas.globalmarket

import com.futchas.CryptoApiProvider
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class GlobalMarketController (private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(GlobalMarketController::class.java)

    @GetMapping("/global-market")
    fun globalMarket() : GlobalMarket {
        val globalMarket = restTemplate.getForObject("${CryptoApiProvider.COINMARKETCAP.value}/global/", GlobalMarket::class.java)
                ?: throw RuntimeException("Historical Prices Service is null! Data from API couldn't be retrieved")

        logger.info("globalmarket $globalMarket")
        return globalMarket
    }

}