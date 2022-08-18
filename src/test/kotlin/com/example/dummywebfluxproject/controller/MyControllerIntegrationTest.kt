package com.example.dummywebfluxproject.controller

import com.example.dummywebfluxproject.model.EncodeRequest
import com.example.dummywebfluxproject.model.EncodeResponse
import com.example.dummywebfluxproject.util.CustomMockDispatcher
import io.kotlintest.shouldBe
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyControllerIntegrationTest(@Autowired private val client: WebTestClient) {
    @Test
    fun `should encode`() {
        client.post().uri("/dummy/encode")
            .bodyValue(EncodeRequest("Manisha"))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(EncodeResponse::class.java)
            .returnResult().responseBody shouldBe EncodeResponse("TWFuaXNoYQ==")
    }
}