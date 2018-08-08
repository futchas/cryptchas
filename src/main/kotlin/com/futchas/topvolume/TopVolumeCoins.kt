package com.futchas.topvolume

import com.fasterxml.jackson.annotation.JsonProperty
import com.futchas.Currency

class TopVolumeCoins {

    var topCoins: List<Currency>  = ArrayList()

    @JsonProperty("Data")
    private fun getTopVolumes(data: List<Map<String, Any>>) {
//        this.addAll(data.map { coin -> Currency(coin["FULLNAME"] as String, coin["VOLUME24HOURTO"] as Double) })
        topCoins = data.map { coin -> Currency(coin["FULLNAME"] as String, coin["VOLUME24HOURTO"] as Double) }
}

}
