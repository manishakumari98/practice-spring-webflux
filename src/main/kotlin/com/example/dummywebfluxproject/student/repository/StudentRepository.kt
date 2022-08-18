package com.example.dummywebfluxproject.student.repository

import com.example.dummywebfluxproject.student.model.StudentDetails
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface StudentRepository: ReactiveCrudRepository<Student, ObjectId> {
    fun findByRollNo(rollNo: Int): Mono<Student>
}

@Document("student")
data class Student(
    val rollNo: Int,
    var name: String,
    var age: Int,
    var standard: Standard
){
    lateinit var _id: String

    fun updateDetails(
        studentDetails: StudentDetails
    ): Student {
        this.age = studentDetails.age ?: this.age
        this.name = studentDetails.name ?: this.name
        this.standard = studentDetails.standard ?: this.standard

        return this
    }
}

enum class Standard {
    NA,
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    FIFTH,
    SIXTH,
    SEVENTH,
    EIGHTH,
    NINTH,
    TENTH,
    ELEVENTH,
    TWELFTH
}

