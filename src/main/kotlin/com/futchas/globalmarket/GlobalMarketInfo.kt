package com.futchas.globalmarket

import com.fasterxml.jackson.annotation.JsonProperty

class GlobalMarketInfo(var totalMarketCap: Double, var totalVolume24h: Double,
                       var btcDominance: Double, var amountCryptoCurrencies: Int) {

    @JsonProperty("data")
    private fun getGlobalMarket(data: Map<String, Any>) {
        this.btcDominance = data["bitcoin_percentage_of_market_cap"] as Double
        this.amountCryptoCurrencies = data["active_cryptocurrencies"] as Int

        val quotes = data["quotes"] as Map<*, *>
        val total = quotes["USD"] as Map<*, *>
        this.totalMarketCap = total["total_market_cap"] as Double
        this.totalVolume24h = total["total_volume_24h"] as Double
    }

    override fun toString(): String {
        return "GlobalMarketInfo(totalMarketCap=$totalMarketCap, totalVolume24h=$totalVolume24h, btcDominance=$btcDominance, amountCryptoCurrencies=$amountCryptoCurrencies)"
    }

}
