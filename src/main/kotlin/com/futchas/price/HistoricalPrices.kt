package com.futchas.price

import com.fasterxml.jackson.annotation.JsonProperty

class HistoricalPrices {

    var closePrices: List<Double> = ArrayList()

    @JsonProperty("Data")
    private fun getPrices(data: List<Map<String, Double>>) {
        this.closePrices = data.map { priceMap ->
            priceMap["close"] as Double
        }
    }
}
