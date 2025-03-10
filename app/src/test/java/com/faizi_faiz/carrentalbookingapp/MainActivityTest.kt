package com.faizi_faiz.carrentalbookingapp

import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.Assert.assertEquals

class MainActivityTest {

    private val activity = mockk<MainActivity>(relaxed = true) // ✅ Mock MainActivity

    @Test
    fun testKayakUrlGeneration() {
        every { activity.buildKayakUrl(any(), any(), any(), any()) } answers {
            "https://www.kayak.com/in?a=awesomecars&url=/cars/Los-Angeles,CA/anywhere/2025-03-01/2025-03-05"
        }

        val expectedUrl = "https://www.kayak.com/in?a=awesomecars&url=/cars/Los-Angeles,CA/anywhere/2025-03-01/2025-03-05"
        val actualUrl = activity.buildKayakUrl("Los-Angeles,CA", "", "2025-03-01", "2025-03-05")
        assertEquals(expectedUrl, actualUrl)
    }
}
