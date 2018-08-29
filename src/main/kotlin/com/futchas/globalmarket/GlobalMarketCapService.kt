package com.futchas.globalmarket

import org.springframework.stereotype.Service

@Service
class GlobalMarketCapService {

    fun getTrend(marketCap: GlobalMarketCap): String {

        val monthlyHigh = marketCap.marketCapLast1mHigh
        val monthlyLow = marketCap.marketCapLast1mLow
        val monthlyAverage = marketCap.marketCapLast1mAverage
        val yearlyHigh = marketCap.marketCapFirst11mHigh
        val yearlyLow = marketCap.marketCapFirst11mLow
        val yearlyAverage = marketCap.marketCapFirst11mAverage

        val diffHigh = format(monthlyHigh - yearlyHigh)
        val diffHighPercent = percentageFormat(monthlyHigh, yearlyHigh)
        val diffLow = format(monthlyLow - yearlyLow)
        val diffLowPercent = percentageFormat(monthlyLow, yearlyLow)
        val diffAverage = format(monthlyAverage - yearlyAverage)
        val diffAveragePercent = percentageFormat(monthlyAverage, yearlyAverage)

        return "Global market cap change:\nHigh $diffHigh($diffHighPercent%) " +
                "Low $diffLow($diffLowPercent%) Average $diffAverage($diffAveragePercent%)"
    }

    private fun format(value : Double) : String = "%.0f".format(value)

    private fun percentageFormat(recent : Double, longterm: Double) : String =
            "%.2f".format((recent - longterm) / maxOf(recent, longterm) * 100)

}
