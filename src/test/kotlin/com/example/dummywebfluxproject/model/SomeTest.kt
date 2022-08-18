package com.example.dummywebfluxproject.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test

class SomeTest {

    @Test
    fun name() {
        val some = Some(Body("Manisha"))
        val deserialise = jacksonObjectMapper().writeValueAsString(some)

        println(deserialise)

        val serialize = jacksonObjectMapper().readValue(deserialise, Some::class.java)

        println(serialize)
    }
}