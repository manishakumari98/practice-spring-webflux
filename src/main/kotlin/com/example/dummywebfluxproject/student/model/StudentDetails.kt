package com.example.dummywebfluxproject.student.model

import com.example.dummywebfluxproject.student.repository.Standard

data class StudentDetails(
    val rollNo: Int,
    val name: String? = null,
    val age: Int? = null,
    val standard: Standard? = null
)
