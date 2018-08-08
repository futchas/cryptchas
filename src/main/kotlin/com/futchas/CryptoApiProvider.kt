package com.futchas

enum internal class CryptoApiProvider(val value: String) {
    CRYPTO_COMPARE("https://min-api.cryptocompare.com/data"),
    COINMARKETCAP("https://api.coinmarketcap.com/v2")
}