package com.futchas.volume

import com.fasterxml.jackson.annotation.JsonProperty

class HistoricalVolumes {

    var values: List<Double> = ArrayList()

    @JsonProperty("Data")
    private fun getValues(data: List<Map<String, Double?>>) {
        this.values = data
                .filter { val volume = it ["volume"]; volume is Double && volume > 0.0 }
                .map { it["volume"] as Double }
    }

}
