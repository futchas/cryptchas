package com.futchas.price

import com.futchas.CryptoApiProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


@RestController
class HistoricalPricesController(private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(javaClass::class.java)

    @Autowired
    lateinit var messagingTemplate: SimpMessagingTemplate

    @Autowired
    lateinit var scheduledExecutorService: ScheduledExecutorService

    @Autowired
    lateinit var service: HistoricalPricesService

    private fun sendUpdateToClient(): Runnable {
        return Runnable {
            val btcToUsdGlobalPrices = restTemplate.getForObject("${CryptoApiProvider.CRYPTO_COMPARE.value}/histominute?fsym=BTC&tsym=USD&limit=10", HistoricalPrices::class.java)
                    ?: throw RuntimeException("Historical Prices Service is null! Data from API couldn't be retrieved")
//            val btcToUsdBinancePrices = restTemplate.getForObject("${CryptoApiProvider.CRYPTO_COMPARE.value}/data/histominute?fsym=BTC&tsym=USDT&limit=10&e=Binance", HistoricalPrices::class.java)

            logger.info(btcToUsdGlobalPrices.closePrices.toString())

            val message = service.getMessage(btcToUsdGlobalPrices)

            if (message != "")
                messagingTemplate.convertAndSend("/notifier/updates", message)
        }
    }


    @MessageMapping("/prices")
//    @GetMapping("/prices")
    fun prices() {
        scheduledExecutorService.scheduleAtFixedRate(sendUpdateToClient(), 1, 10, TimeUnit.SECONDS)
    }

}


