package com.example.dummywebfluxproject.controller


import com.example.dummywebfluxproject.model.EncodeRequest
import com.example.dummywebfluxproject.model.EncodeResponse
import com.example.dummywebfluxproject.service.MyService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/dummy")
class MyController(private val service: MyService) {

    @PostMapping("/encode")
    fun encode(@RequestBody request: EncodeRequest): Mono<EncodeResponse> {
        return service.encode(request.plainText).map { EncodeResponse(it) }
    }
}

