package com.futchas.price

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class HistoricalPricesService {
    private val logger = LoggerFactory.getLogger(javaClass::class.java)

    fun getMessage(btcToUsdPrices: HistoricalPrices) : String {
        val percentageThreshold = 5

        val minPrice = btcToUsdPrices.closePrices.min()
        val maxPrice = btcToUsdPrices.closePrices.max()

        if(minPrice != null && maxPrice != null) {

            val priceDifference = maxPrice - minPrice
            val percentageDifference = priceDifference / minPrice * 100
            if (percentageDifference > percentageThreshold)
                return "In the last 10min the price changed $percentageDifference% ($priceDifference)"
        }

        logger.error("Can't get lowest or highest price!")
        return ""
    }

}
