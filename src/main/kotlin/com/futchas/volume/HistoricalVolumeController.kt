package com.futchas.volume

import com.futchas.CryptoApiProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


@RestController
class HistoricalVolumeController (private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(HistoricalVolumeController::class.java)
    @Autowired
    lateinit var messagingTemplate: SimpMessagingTemplate

    @Autowired
    lateinit var scheduledExecutorService: ScheduledExecutorService

    @Autowired
    lateinit var service : HistoricalVolumesService

    private fun sendUpdateToClient(): Runnable {

        return Runnable {
            val globalVolumes1h = restTemplate.getForObject("${CryptoApiProvider.CRYPTO_COMPARE.value}/exchange/histohour?tsym=BTC&limit=12", HistoricalVolumes::class.java)
                    ?: throw RuntimeException("Historical Volume Service is null! Data from API couldn't be retrieved")
            val globalVolumes24h = restTemplate.getForObject("${CryptoApiProvider.CRYPTO_COMPARE.value}/exchange/histoday?tsym=BTC&limit=24", HistoricalVolumes::class.java)
                    ?: throw RuntimeException("Historical Volume Service is null! Data from API couldn't be retrieved")

            logger.info(globalVolumes1h.values.toString())
            logger.info(globalVolumes24h.values.toString())


            val volumeDifference = service.getVolumeDifference(globalVolumes1h, globalVolumes24h)

            val message : String = if(service.isUnusualVolume(volumeDifference))
                                        "Unusual volume difference at: $volumeDifference%"
                                    else
                                        "Volume difference at $volumeDifference%"

            messagingTemplate.convertAndSend("/topic/updates", message)
        }
    }

    @MessageMapping("/volumes")
//    @GetMapping("/volumes")
    fun volumes() {
//        scheduledFuture = taskScheduler.scheduleAtFixedRate(sendUpdateToClient(), 5000)
            scheduledExecutorService.scheduleAtFixedRate(sendUpdateToClient(), 1, 5, TimeUnit.SECONDS)
    }

}


