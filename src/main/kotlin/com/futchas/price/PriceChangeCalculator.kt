package com.futchas.price

import com.futchas.indicator.RSI
import org.slf4j.LoggerFactory

class PriceChangeCalculator {

    private val logger = LoggerFactory.getLogger(RSI::class.java)

    fun calculate(closePrices: List<Double>): Pair<Double, Double> {

        val minPrice = closePrices.min()
        val maxPrice = closePrices.max()

        if (minPrice != null && maxPrice != null) {

            val priceDifference = maxPrice - minPrice
            val percentageDifference = priceDifference / minPrice * 100

            return Pair(priceDifference, percentageDifference)
        }

        logger.error("Can't get lowest or highest price!")
        return Pair(-1.0, -1.0)
    }

}