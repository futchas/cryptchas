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

//        val now = LocalDateTime.now()
//        var lastYear = now.minusYears(1)
//        val nowEpoch = now.atZone(ZoneId.systemDefault()).toEpochSecond()
//        val lastYearEpoch = lastYear.atZone(ZoneId.systemDefault()).toEpochSecond()
//        val timestampNow = System.currentTimeMillis() / 1000

    }

    private fun sendUpdateToClient(): Runnable {
        return Runnable {

            val now = Instant.now()
            val nowEpoch = epochWith3TrailingZeros(now)
            val lastYearEpoch = epochWith3TrailingZeros(now.minus(365, ChronoUnit.DAYS))

//        println("localdatetime " + LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())
//        println("currenttimemillis " + System.currentTimeMillis() / 1000)
//        println("Instant " + Instant.now().toEpochMilli() / 1000)

            println("urlglobal ${CryptoApiProvider.COINMARKETCAP.value}/global/marketcap-total/$lastYearEpoch/$nowEpoch/")
//        val globalMarket = restTemplate.getForObject("${CryptoApiProvider.COINMARKETCAP.value}/global/", GlobalMarket::class.java)
//                ?: throw RuntimeException("Historical Prices Service is null! Data from API couldn't be retrieved")
            val marketCap = restTemplate.getForObject("${CryptoApiProvider.COINMARKETCAP.value}/global/marketcap-total/$lastYearEpoch/$nowEpoch/", GlobalMarketCap::class.java)

            if(marketCap != null) {
                val message = service.getTrend(marketCap)
                logger.info(message)
                messagingTemplate.convertAndSend("/notifier/updates", message)
            }

        }
    }

    /**
     * For some reason the coinmarketcap endpoint (non official API) needs 3 trailing zeros
     */
    fun epochWith3TrailingZeros(timeInstant: Instant): String {
        return "${timeInstant.toEpochMilli() / 1000}000"
    }

}