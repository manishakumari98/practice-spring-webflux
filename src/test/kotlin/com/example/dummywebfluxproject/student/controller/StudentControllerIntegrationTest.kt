package com.example.dummywebfluxproject.student.controller

import com.example.dummywebfluxproject.student.model.StudentDetails
import com.example.dummywebfluxproject.student.repository.Standard
import com.example.dummywebfluxproject.student.repository.Student
import com.example.dummywebfluxproject.student.repository.StudentRepository
import io.kotlintest.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.kotlin.test.test

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerIntegrationTest(@Autowired private val client: WebTestClient) {

    @Autowired
    private lateinit var repository: StudentRepository

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll().block()
    }

    @AfterEach
    internal fun tearDown() {
        repository.deleteAll().block()
    }

    @Test
    fun `should add the student in repository`() {
        val studentDetails = StudentDetails(1, "Someone", 5, Standard.FIRST)
        val student = Student(1, "Someone", 5, Standard.FIRST)
        client.post().uri("/student/add")
            .bodyValue(studentDetails)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(Boolean::class.java)
            .returnResult().responseBody shouldBe true

        repository.findAll()
            .test()
            .expectNext(student)
            .verifyComplete()
    }

    @Test
    fun `should update the student details in repository`() {
        val rollNo = 1
        val studentDetails = StudentDetails(rollNo = rollNo, age = 6, standard = Standard.SECOND)
        val student = Student(rollNo, "Manisha", 5, Standard.FIRST)
        val updatedStudent = Student(rollNo, "Manisha", 6, Standard.SECOND)

        repository.save(student).block()

        client.post().uri("/student/update")
            .bodyValue(studentDetails)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(Student::class.java)
            .returnResult().responseBody shouldBe updatedStudent

        repository.findAll()
            .test()
            .expectNext(updatedStudent)
            .verifyComplete()
    }

    @Test
    fun `should get the student details for given roll no in repository`() {
        val rollNo = 1
        val student = Student(rollNo, "Anchor", 5, Standard.FIRST)

        repository.save(student).block()

        client.get().uri("/student/$rollNo")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(Student::class.java)
            .returnResult().responseBody shouldBe student

        repository.findAll()
            .test()
            .expectNext(student)
            .verifyComplete()
    }

    @Test
    fun `should delete the student with given roll no in repository`() {
        val rollNo = 1
        val student = Student(rollNo, "YiJin", 5, Standard.FIRST)

        repository.save(student).block()

        client.get().uri("/student/delete/$rollNo")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(Student::class.java)
            .returnResult().responseBody shouldBe student

        repository.findAll()
            .test()
            .expectNextCount(0)
            .verifyComplete()
    }
}