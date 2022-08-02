package com.example.dummywebfluxproject.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

@Service
class MyService {

    fun encode(plainText: String): Mono<String> {
        return Base64.getEncoder().encodeToString(plainText.toByteArray()).toMono()
    }
}
