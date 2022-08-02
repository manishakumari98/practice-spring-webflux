package com.example.dummywebfluxproject.controller

import com.example.dummywebfluxproject.model.EncodeRequest
import com.example.dummywebfluxproject.model.EncodeResponse
import com.example.dummywebfluxproject.service.MyService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.test.test

internal class MyControllerTest {

    private val service = mockk<MyService>()
    private val myController = MyController(service)

    @Test
    fun `should encode`() {
        val encodedText = "TWFuaXNoYQ=="
        every { service.encode(any()) } returns encodedText.toMono()

        myController.encode(EncodeRequest("Manisha"))
            .test()
            .expectNext(EncodeResponse(encodedText))
            .verifyComplete()
    }
}