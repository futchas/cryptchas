package com.futchas.globalmarket

import com.fasterxml.jackson.annotation.JsonProperty

class GlobalMarketCap(var marketCapFirst11mHigh: Double, var marketCapFirst11mAverage: Double, var marketCapFirst11mLow: Double,
                      var marketCapLast1mHigh: Double, var marketCapLast1mAverage: Double, var marketCapLast1mLow: Double) {

    @JsonProperty("market_cap_by_available_supply")
    fun getMarketCaps(globalMarketVolumeHistory: ArrayList<ArrayList<Number>>) {

        val marketCaps1y = globalMarketVolumeHistory.map { it[1] as Double }
        val marketCapsFirst11m = marketCaps1y.subList(0, 335)
        val marketCapsLast1m = marketCaps1y.subList(335, marketCaps1y.size)

        marketCapFirst11mAverage = marketCapsFirst11m.average()
        marketCapFirst11mHigh = marketCapsFirst11m.max() ?: throw RuntimeException("Couldn't get highest yearly market cap!")
        marketCapFirst11mLow = marketCapsFirst11m.min() ?: throw RuntimeException("Couldn't get lowest yearly market cap!")

        marketCapLast1mAverage = marketCapsLast1m.average()
        marketCapLast1mHigh = marketCapsLast1m.max() ?: throw RuntimeException("Couldn't get highest monthly market cap!")
        marketCapLast1mLow = marketCapsLast1m.min() ?: throw RuntimeException("Couldn't get lowest yearly market cap!")


//        val marketCapLast1mAverage = globalMarketVolumeHistory.map { it[1] as Double }.average()
//        val sumByDouble = globalMarketVolumeHistory.sumByDouble { it[1] as Double }
//        this.totalMarketCap = globalMarketVolumeHistory.first()[1] as Double
    }

}