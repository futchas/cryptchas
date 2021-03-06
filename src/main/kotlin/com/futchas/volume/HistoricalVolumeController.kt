package com.futchas.volume

import com.futchas.CryptoApiProvider
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


@RestController
class HistoricalVolumeController(private val restTemplate: RestTemplate,
                                 val messagingTemplate: SimpMessagingTemplate,
                                 val scheduledExecutorService: ScheduledExecutorService,
                                 val service: HistoricalVolumesService) {

    private val logger = LoggerFactory.getLogger(HistoricalVolumeController::class.java)

    @MessageMapping("/volumes")
//    @GetMapping("/volumes")
    fun volumes() {
        scheduledExecutorService.scheduleAtFixedRate(sendUpdateToClient(), 1, 5, TimeUnit.SECONDS)
    }

    private fun sendUpdateToClient(): Runnable {

        return Runnable {
            val globalVolumes1h = restTemplate.getForObject("${CryptoApiProvider.CRYPTO_COMPARE.value}/exchange/histohour?tsym=BTC&limit=12", HistoricalVolumes::class.java)
                    ?: throw RuntimeException("Historical Volume Service is null! Data from API couldn't be retrieved")
            val globalVolumes24h = restTemplate.getForObject("${CryptoApiProvider.CRYPTO_COMPARE.value}/exchange/histoday?tsym=BTC&limit=24", HistoricalVolumes::class.java)
                    ?: throw RuntimeException("Historical Volume Service is null! Data from API couldn't be retrieved")

            logger.info(globalVolumes1h.values.toString())
            logger.info(globalVolumes24h.values.toString())


            val volumeDifference = service.getVolumeDifference(globalVolumes1h, globalVolumes24h)

            val message: String = if (service.isUnusualVolume(volumeDifference))
                "Unusual volume difference at: $volumeDifference%"
            else
                "Volume difference at $volumeDifference%"

            messagingTemplate.convertAndSend("/notifier/updates", message)
        }
    }


}


