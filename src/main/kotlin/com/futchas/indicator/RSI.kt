package com.futchas.indicator

import org.slf4j.LoggerFactory

class RSI {

    private val logger = LoggerFactory.getLogger(RSI::class.java)

    fun calculate (closePrices: List<Double>): Double {
        val period = 14
        val priceListSize = closePrices.size
        val lastPriceIndex = priceListSize - 1
        val firstPriceIndex = priceListSize - period
        if (firstPriceIndex < 0) {
            val message = "Not enough price data available to calculate the RSI! Current size is only $priceListSize and should be at least 15"
            logger.error(message)
            throw RuntimeException(message)
        }

        var sumGains = 0.0
        var sumLoss = 0.0
        for (currentPriceIndex in firstPriceIndex..lastPriceIndex - 1) {
            val priceDiff = closePrices[currentPriceIndex + 1] - closePrices[currentPriceIndex]
            when {
                priceDiff > 0.0 -> {
                    sumGains += priceDiff
                    println("$currentPriceIndex sumGains: $sumGains")
                }
                else -> {
                    sumLoss += priceDiff
                    println("$currentPriceIndex sumLoss: $sumLoss")
                }
            }
        }

        val averageGain = sumGains / period
        val averageLoss = Math.abs(sumLoss) / period

        println("averageGain: $averageGain, averageLoss: $averageLoss")

        val rsi: Double
        when {
            averageLoss == averageGain -> rsi = 50.0
            averageGain == 0.0 -> rsi = 0.0
            averageLoss == 0.0 -> rsi = 100.0
            else -> {
                val rs = averageGain / averageLoss
                rsi = 100 - (100 / (1 + rs))
            }
        }
        println("RSI: $rsi")
        return rsi

    }

}