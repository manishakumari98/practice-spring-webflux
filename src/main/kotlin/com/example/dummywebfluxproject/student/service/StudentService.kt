package com.example.dummywebfluxproject.student.service

import com.example.dummywebfluxproject.student.model.StudentDetails
import com.example.dummywebfluxproject.student.repository.Student
import com.example.dummywebfluxproject.student.repository.StudentRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Service
class StudentService(private val repository: StudentRepository) {

    fun add(newStudent: StudentDetails): Mono<Boolean> {
        val student = Student(newStudent.rollNo, newStudent.name!!, newStudent.age!!, newStudent.standard!!)
        return repository.save(student)
            .flatMap { true.toMono() }
    }

    fun update(studentDetails: StudentDetails): Mono<Student> {
        return repository.findByRollNo(studentDetails.rollNo)
            .switchIfEmpty { throw Exception("Student with given roll number Not Found") }
            .map { it.updateDetails(studentDetails) }.flatMap { student ->
                repository.save(student)
                    .flatMap { it.toMono() }
            }
    }

    fun get(rollNo: Int): Mono<Student> {
        return repository.findByRollNo(rollNo)
            .switchIfEmpty { throw Exception("Student with given roll number Not Found") }
            .flatMap { it.toMono() }
    }

    fun delete(rollNo: Int): Mono<Student> {
        return repository.findByRollNo(rollNo)
            .switchIfEmpty { throw Exception("Student with given roll number Not Found") }
            .flatMap { studentToBeDeleted ->
                repository.delete(studentToBeDeleted)
                    .flatMap { studentToBeDeleted.toMono() }
                    .switchIfEmpty{ studentToBeDeleted.toMono() }
            }
    }
}
