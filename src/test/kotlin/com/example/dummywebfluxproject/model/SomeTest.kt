package com.example.dummywebfluxproject.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test

class SomeTest {

    @Test
    fun `check Base64Encoded annotation`() {
        val some = Some(Body("Manisha"))
        val deserialise = jacksonObjectMapper().writeValueAsString(some)

        println(deserialise)

        val serialize = jacksonObjectMapper().readValue(deserialise, Some::class.java)

        println(serialize)
    }

    @Test
    fun `check Base64Encoded2 annotation`() {
        val some = Some2(Body2("Manisha"))
        val deserialise = jacksonObjectMapper().writeValueAsString(some)

        println(deserialise)

        val serialize = jacksonObjectMapper().readValue(deserialise, Some2::class.java)

        println(serialize)
    }
}