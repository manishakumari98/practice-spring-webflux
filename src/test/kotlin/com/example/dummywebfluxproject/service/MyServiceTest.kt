package com.example.dummywebfluxproject.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import reactor.kotlin.test.test

internal class MyServiceTest {

    @Test
    fun `should encode`() {
        MyService().encode("Manisha")
            .test()
            .expectNext("TWFuaXNoYQ==")
            .verifyComplete()
    }
}