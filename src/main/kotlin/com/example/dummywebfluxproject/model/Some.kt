package com.example.dummywebfluxproject.model

import com.example.dummywebfluxproject.others.Base64Encoded
import com.example.dummywebfluxproject.others.Base64Encoded2

data class Some(
    @Base64Encoded
    val text: Body
)

data class Body(
    val body: String
)

data class Some2(
    @Base64Encoded2(mapIn = "body")
    val text: Body2
)

data class Body2(
    val body: String
)
