package com.futchas.topvolume

import com.futchas.CryptoApiProvider
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


@RestController
class TopVolumeCoinsController(private val restTemplate: RestTemplate,
                               val messagingTemplate: SimpMessagingTemplate,
                               val scheduledExecutorService: ScheduledExecutorService) {

    private val logger = LoggerFactory.getLogger(TopVolumeCoinsController::class.java)

    @MessageMapping("/top-volume-coins")
//    @SendTo(sendEndpoint)
//    @GetMapping("/top-volume-coins")
    fun topVolumeCoins(volumeRank: String) {
        if (volumeRank.toInt() > 0)
            scheduledExecutorService.scheduleAtFixedRate(sendUpdateToClient(volumeRank.toInt()), 1, 5, TimeUnit.SECONDS)
    }

    private fun sendUpdateToClient(volumeRank: Int): Runnable {
        return Runnable {
            val topVolumeCoins = restTemplate.getForObject("${CryptoApiProvider.CRYPTO_COMPARE.value}/top/volumes?tsym=BTC", TopVolumeCoins::class.java)
                    ?: throw RuntimeException("Top volume coin Service is null! Data from API couldn't be retrieved")

            logger.info(topVolumeCoins.topCoins.toString())
            val currency = topVolumeCoins.topCoins[volumeRank - 1]

            val message = "Volume of ${currency.name} is ${currency.volume}"
            messagingTemplate.convertAndSend("/notifier/updates", message)
        }
    }


//    private fun printVolumes(topVolumeCoins: TopVolumeCoins) {
//
//        println("\nThe coins with the highest volume in the last 24 hours:")
//        topVolumeCoins.topCoins.forEach { coin ->
//            println("Volume of ${coin.name} is ${coin.volume}")
//        }
//
//    }

}


