package com.futchas.globalmarket

import com.futchas.CryptoApiProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


@RestController
class GlobalMarketCapController(private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(GlobalMarketCapController::class.java)

    @Autowired
    lateinit var messagingTemplate: SimpMessagingTemplate

    @Autowired
    lateinit var scheduledExecutorService: ScheduledExecutorService

    @Autowired
    lateinit var service: GlobalMarketCapService

    //    @GetMapping("/global-market-cap")
    @MessageMapping("/global-market-cap")
    fun globalMarket() {
        scheduledExecutorService.scheduleAtFixedRate(sendUpdateToClient(), 0, 10, TimeUnit.SECONDS)
    }

    private fun sendUpdateToClient(): Runnable {
        return Runnable {

            val now = Instant.now()
            val nowEpoch = epochWith3TrailingZeros(now)
            val lastYearEpoch = epochWith3TrailingZeros(now.minus(365, ChronoUnit.DAYS))

            logger.info("Call ${CryptoApiProvider.COINMARKETCAP.value}/global/marketcap-total/$lastYearEpoch/$nowEpoch/")

            val marketCap = restTemplate.getForObject("${CryptoApiProvider.COINMARKETCAP_UNOFFICIAL.value}/global/marketcap-total/$lastYearEpoch/$nowEpoch/", GlobalMarketCap::class.java)

            if(marketCap != null) {
                val message = service.compareMonthlyToYearlyTrend(marketCap)
                logger.info(message)
                messagingTemplate.convertAndSend("/notifier/updates", message)
            }

        }
    }

    /**
     * For some reason the coinmarketcap endpoint (non official API) needs 3 trailing zeros
     * e.g. https://graphs2.coinmarketcap.com/global/marketcap-total/1502976348000/1534512348000/
     */
    fun epochWith3TrailingZeros(timeInstant: Instant): String {
        return "${timeInstant.toEpochMilli() / 1000}000"
    }

}