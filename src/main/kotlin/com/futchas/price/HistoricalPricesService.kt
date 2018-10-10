package com.futchas.price

import com.futchas.indicator.RSI
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class HistoricalPricesService {
    private val logger = LoggerFactory.getLogger(HistoricalPricesService::class.java)

    fun getSignificantChanges(closePrices: List<Double>): String {
        return """${getSignificantPriceChange(closePrices)}
                  ${getSignificantRSILevel(closePrices)}""".trimIndent()
    }

    private fun getSignificantPriceChange(closePrices: List<Double>): String {

        val percentageThreshold = 5
        val (priceDifference, percentageDifference) = PriceChangeCalculator().calculate(closePrices)

        logger.info("Price change $percentageDifference% ($priceDifference)")

        return if (percentageDifference > percentageThreshold)
            "In the last 10min the price changed $percentageDifference% ($priceDifference)"
        else ""
    }

    fun getSignificantRSILevel(closePrices: List<Double>): String {
        val rsi = RSI().calculate(closePrices)
        var significantMessage = ""
        when {
            rsi > 70 -> significantMessage = "RSI level increased to $rsi"
            rsi < 30 -> significantMessage = "RSI level decreased to $rsi"
        }

        return significantMessage
    }

}
