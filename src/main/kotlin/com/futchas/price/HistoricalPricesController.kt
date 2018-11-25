package com.futchas.price

import com.futchas.CryptoApiProvider
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


@RestController
class HistoricalPricesController(private val restTemplate: RestTemplate,
                                 val messagingTemplate: SimpMessagingTemplate,
                                 val scheduledExecutorService: ScheduledExecutorService,
                                 val service: HistoricalPricesService) {

    private val logger = LoggerFactory.getLogger(HistoricalPricesController::class.java)


    @MessageMapping("/prices")
//  // @GetMapping("/prices")
    fun prices() {
        scheduledExecutorService.scheduleAtFixedRate(sendUpdateToClient(), 1, 10, TimeUnit.SECONDS)
    }


    private fun sendUpdateToClient(): Runnable {
        return Runnable {
            // one value is 1 minute, limit=15 means 15min each minute 1 value
            val btcToUsdGlobalPrices = restTemplate.getForObject("${CryptoApiProvider.CRYPTO_COMPARE.value}/histominute?fsym=BTC&tsym=USD&limit=15", HistoricalPrices::class.java)
//                    ?: throw RuntimeException("Historical Prices Service is null! Data from API couldn't be retrieved")
//            val btcToUsdBinancePrices = restTemplate.getForObject("${CryptoApiProvider.CRYPTO_COMPARE.value}/data/histominute?fsym=BTC&tsym=USDT&limit=10&e=Binance", HistoricalPrices::class.java)

            val closePrices = btcToUsdGlobalPrices?.closePrices
            if (closePrices != null) {
                val message = service.getSignificantChanges(closePrices)

                if(message.isNotBlank()){
                    logger.info(message)
                    messagingTemplate.convertAndSend("/notifier/updates", message)
                }

            }
        }
    }


}


