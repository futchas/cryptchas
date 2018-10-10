package com.futchas.price

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


//@ExtendWith(SpringExtension::class)
//@SpringBootTest
internal class HistoricalPricesServiceTest {

    private lateinit var pricesService: HistoricalPricesService

    @BeforeEach
    fun setUp() {
        pricesService = HistoricalPricesService()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `Get Significant Price Change - above theshold`() {
        val priceList = listOf(205.0, 200.0, 224.0)
        val message = pricesService.getSignificantPriceChange(priceList)
        println(message)
        assertThat(message).isNotBlank()
    }

    @Test
    fun `Get Significant Price Change - below threshold`() {
        val priceList = listOf(204.0, 200.0, 203.0)
        val message = pricesService.getSignificantPriceChange(priceList)
        println(message)
        assertThat(message).isBlank()
    }



}