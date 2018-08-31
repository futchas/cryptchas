package com.futchas

enum class CryptoApiProvider(val value: String) {
    COINMARKETCAP("https://graphs2.coinmarketcap.com"),
    CRYPTO_COMPARE("https://min-api.cryptocompare.com/data")
}