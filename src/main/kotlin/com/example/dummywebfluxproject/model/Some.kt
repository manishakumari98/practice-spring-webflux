package com.example.dummywebfluxproject.model

import com.example.dummywebfluxproject.others.Base64Encoded

data class Some(
    @Base64Encoded
    val text: Body
)

data class Body(
    val body: String
)
