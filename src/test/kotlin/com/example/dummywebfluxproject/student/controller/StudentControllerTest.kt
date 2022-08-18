package com.example.dummywebfluxproject.student.controller

import com.example.dummywebfluxproject.student.model.StudentDetails
import com.example.dummywebfluxproject.student.repository.Standard
import com.example.dummywebfluxproject.student.repository.Student
import com.example.dummywebfluxproject.student.service.StudentService
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.test.test

class StudentControllerTest {

    private val service = mockk<StudentService>()
    private val studentController = StudentController(service)

    @Test
    fun `should add student`() {
        every { service.add(any()) }returns Mono.just(true)

        studentController.add(mockk())
            .test()
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    fun `should throw exception on error on add call`() {
        every { service.add(any()) }returns Mono.error(Exception("some error"))

        studentController.add(mockk())
            .test()
            .consumeErrorWith {
                it as Exception
                it.message shouldBe "some error occurred while adding student"
            }
            .verify()
    }

    @Test
    fun `should update student details`() {
        val rollNo = 1
        val studentDetails = StudentDetails(rollNo = rollNo, age = 6, standard = Standard.SECOND)
        val updatedStudent = Student(rollNo, "Manisha", 6, Standard.SECOND)

        every { service.update(any()) }returns updatedStudent.toMono()

        studentController.update(studentDetails)
            .test()
            .expectNext(updatedStudent)
            .verifyComplete()
    }

    @Test
    fun `should throw exception if any error is thrown on update call`() {
        val rollNo = 1
        val studentDetails = StudentDetails(rollNo = rollNo, age = 6, standard = Standard.SECOND)

        every { service.update(any()) }returns Mono.error(Exception("some error"))

        studentController.update(studentDetails)
            .test()
            .consumeErrorWith {
                it as Exception
                it.message shouldBe "some error occurred while updating details of student"
            }
            .verify()
    }

    @Test
    fun `should get the student with the given roll no`() {
        val student = Student(1, "Manisha", 5, Standard.FIRST)

        every { service.get(any()) }returns student.toMono()
        studentController.get(1)
            .test()
            .expectNext(student)
            .verifyComplete()
    }

    @Test
    fun `should delete the student with the given roll no`() {
        val student = Student(1, "Manisha", 5, Standard.FIRST)

        every { service.delete(any()) }returns student.toMono()
        studentController.delete(1)
            .test()
            .expectNext(student)
            .verifyComplete()
    }

    @Test
    fun `should return dummy student when roll no is not found to delete`() {

        val errorMessage = "Student with given roll number Not Found"
        every { service.delete(any()) }returns Mono.error(Exception(errorMessage))

        studentController.delete(1)
            .test()
            .expectNext(Student(0, errorMessage, 0, Standard.NA))
            .verifyComplete()
    }

    @Test
    fun `should throw error thrown by service on delete call`() {

        val exception = Exception("Some error")
        every { service.delete(any()) }returns Mono.error(exception)

        studentController.delete(1)
            .test()
            .consumeErrorWith {
                it shouldBe exception
            }
            .verify()
    }
}