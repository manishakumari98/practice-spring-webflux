package com.example.dummywebfluxproject.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class CustomMockDispatcher : Dispatcher() {
    private val responseMap = mutableMapOf<String, MockResponse>()
    fun mockResponse(requestPath: String, mockResponse: MockResponse) {
        if (responseMap.containsKey(requestPath))
            throw IllegalStateException("A mock has been setup already for the path: $requestPath")
        else responseMap[requestPath] = mockResponse
    }

    override fun dispatch(request: RecordedRequest): MockResponse {
        return responseMap[request.path] ?: MockResponse().setResponseCode(404)
    }
}
