package com.example.dummywebfluxproject.student.service

import com.example.dummywebfluxproject.student.model.StudentDetails
import com.example.dummywebfluxproject.student.repository.Standard
import com.example.dummywebfluxproject.student.repository.Student
import com.example.dummywebfluxproject.student.repository.StudentRepository
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.test

class StudentServiceTest {

    private val repository = mockk<StudentRepository>()
    private val studentService = StudentService(repository)

    @Test
    fun `should add a student in repository`() {
        val newStudent = StudentDetails(1, "Manisha", 5, Standard.FIRST)
        val student = Student(1, "Manisha", 5, Standard.FIRST)
        every { repository.save(any()) }returns Mono.just(student)

        studentService.add(newStudent)
            .test()
            .expectNext(true)
            .verifyComplete()

        verify(exactly = 1) { repository.save(student) }
    }

    @Test
    fun `should update details of student in repository`() {
        val rollNo = 1
        val existingStudent = Student(rollNo, "Manisha", 5, Standard.FIRST)
        val studentDetails = StudentDetails(rollNo = rollNo, age = 6, standard = Standard.SECOND)
        val updatedStudent = Student(rollNo, "Manisha", 6, Standard.SECOND)
        every { repository.findByRollNo(any()) }returns Mono.just(existingStudent)
        every { repository.save(any()) }returns Mono.just(updatedStudent)

        studentService.update(studentDetails)
            .test()
            .expectNext(updatedStudent)
            .verifyComplete()

        verifySequence {
            repository.findByRollNo(rollNo)
            repository.save(updatedStudent)
        }
    }

    @Test
    fun `should throw exception when student is not found on update call`() {
        val rollNo = 1
        val studentDetails = StudentDetails(rollNo = rollNo, age = 6, standard = Standard.SECOND)
        every { repository.findByRollNo(any()) }returns Mono.empty()

        studentService.update(studentDetails)
            .test()
            .consumeErrorWith {
                it as Exception
                it.message shouldBe "Student with given roll number Not Found"
            }
            .verify()

        verify {
            repository.findByRollNo(rollNo)
        }
    }

    @Test
    fun `should get details of student in repository`() {
        val rollNo = 1
        val existingStudent = Student(rollNo, "Manisha", 5, Standard.FIRST)
        every { repository.findByRollNo(any()) }returns Mono.just(existingStudent)

        studentService.get(rollNo)
            .test()
            .expectNext(existingStudent)
            .verifyComplete()

        verify {
            repository.findByRollNo(rollNo)
        }
    }

    @Test
    fun `should throw exception when student is not found on get call`() {
        every { repository.findByRollNo(any()) }returns Mono.empty()

        val rollNo = 1
        studentService.get(rollNo)
            .test()
            .consumeErrorWith {
                it as Exception
                it.message shouldBe "Student with given roll number Not Found"
            }
            .verify()

        verify {
            repository.findByRollNo(rollNo)
        }
    }

    @Test
    fun `should delete student in repository`() {
        val rollNo = 1
        val existingStudent = Student(rollNo, "Manisha", 5, Standard.FIRST)
        every { repository.findByRollNo(any()) }returns Mono.just(existingStudent)
        every { repository.delete(any()) }returns Mono.empty()

        studentService.delete(rollNo)
            .test()
            .expectNext(existingStudent)
            .verifyComplete()

        verify {
            repository.findByRollNo(rollNo)
            repository.delete(existingStudent)
        }
    }

    @Test
    fun `should throw exception when student is not found on delete call`() {
        every { repository.findByRollNo(any()) }returns Mono.empty()

        val rollNo = 1
        studentService.delete(rollNo)
            .test()
            .consumeErrorWith {
                it as Exception
                it.message shouldBe "Student with given roll number Not Found"
            }
            .verify()

        verify {
            repository.findByRollNo(rollNo)
        }
    }
}