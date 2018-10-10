package com.futchas.indicator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RSITest {

    private val loopRange: IntRange = (1..20)

    @Test
    fun `Average loss is 0 - RSI is 100`() {
        val priceList = mutableListOf<Double>()
        loopRange.forEach { i ->
            priceList.add(20.0 + i)
        }
        rsiAssertions(priceList, 100.0)
    }

    @Test
    fun `Average gain is 0 - RSI is 0`() {
        val priceList = mutableListOf<Double>()
        loopRange.forEach { i ->
            priceList.add(20.0 - i)
        }
        rsiAssertions(priceList, 0.0)
    }

    @Test
    fun `No price change mean RSI is 50`() {
        val priceList = mutableListOf<Double>()
        loopRange.forEach {
            priceList.add(140.0)
//            when {
//                i % 2 == 1 -> priceList.add(140.0 + i)
//                i % 2 == 0 -> priceList.add(140.0 - i)
//            }
        }
        rsiAssertions(priceList, 50.0)
    }

    @Test
    fun `Average loss and gain is equal mean RSI is 50`() {
        val priceList = mutableListOf<Double>()
        loopRange.forEach {
            priceList.add(140.0)
        }

        priceList[4] = 130.0
        priceList[8] = 150.0

        rsiAssertions(priceList, 50.0)
    }

    @Test
    fun `Gains are 3 times higher than average loss - RSI is 75`() {
        val priceList = mutableListOf<Double>()

        val loopRange = 0..6
        loopRange.forEach { i ->
            priceList.add(100.0 + i * 3)
        }
        loopRange.forEach { i ->
            priceList.add(118.0 - i)
        }

        rsiAssertions(priceList, 75.0)
    }

    @Test
    fun `Losses are 4 times higher than average gains - RSI is 20`() {
        val priceList = mutableListOf<Double>()

        val loopRange = 0..6
        loopRange.forEach { i ->
            priceList.add(100.0 + i)
        }
        loopRange.forEach { i ->
            priceList.add(106.0 - i * 4)
        }

        rsiAssertions(priceList, 20.0)
    }


    private fun rsiAssertions(priceList: MutableList<Double>, expectedRSI: Double) {
        val rsiLevel = RSI().calculate(priceList)
        assertThat(rsiLevel).isGreaterThanOrEqualTo(0.0)
        assertThat(rsiLevel).isLessThanOrEqualTo(100.0)
        assertThat(rsiLevel).isEqualTo(expectedRSI)
    }
}