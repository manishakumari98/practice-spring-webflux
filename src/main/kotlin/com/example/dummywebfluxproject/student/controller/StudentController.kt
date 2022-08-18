package com.example.dummywebfluxproject.student.controller


import com.example.dummywebfluxproject.student.model.StudentDetails
import com.example.dummywebfluxproject.student.repository.Standard
import com.example.dummywebfluxproject.student.repository.Student
import com.example.dummywebfluxproject.student.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/student")
class StudentController(private val service: StudentService) {

    @PostMapping("/add")
    fun add(@RequestBody newStudent: StudentDetails): Mono<Boolean> {
        return service.add(newStudent)
            .onErrorMap {
                Exception("some error occurred while adding student")
            }
    }

    @PostMapping("/update")
    fun update(@RequestBody studentDetails: StudentDetails): Mono<Student> {
        return service.update(studentDetails)
            .onErrorMap {
                Exception("some error occurred while updating details of student")
            }
    }

    @GetMapping("/{rollNo}")
    fun get(@PathVariable rollNo: Int): Mono<Student> {
        return service.get(rollNo)
    }

    @GetMapping("/delete/{rollNo}")
    fun delete(@PathVariable rollNo: Int): Mono<Student> {
        return service.delete(rollNo)
            .onErrorResume { error ->
                if( error is Exception && error.message == "Student with given roll number Not Found"){
                    return@onErrorResume Student(0, error.message!!, 0, Standard.NA).toMono()
                }
                throw error
            }
    }
}

