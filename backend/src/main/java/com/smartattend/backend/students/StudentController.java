package com.smartattend.backend.students;

import com.smartattend.backend.auth.UserAccount;
import com.smartattend.backend.auth.UserAccountRepository;
import com.smartattend.backend.classes.ClassEntity;
import com.smartattend.backend.classes.ClassRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final UserAccountRepository userAccountRepository;

    public StudentController(StudentRepository studentRepository,
                             ClassRepository classRepository,
                             UserAccountRepository userAccountRepository) {
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping
    public List<StudentResponse> listStudents(@RequestParam(name = "classId", required = false) Long classId) {
        List<Student> students = classId == null
            ? studentRepository.findAll()
            : studentRepository.findByClassEntityId(classId);
        return students.stream().map(StudentResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse createStudent(
        @Valid @RequestBody StudentRequest request,
        @RequestHeader(name = "X-User-Id", required = false) Long userId
    ) {
        ClassEntity classEntity = classRepository.findById(request.classId()).orElseThrow();
        UserAccount createdBy = userId == null ? null : userAccountRepository.findById(userId).orElse(null);
        Student saved = studentRepository.save(new Student(
            request.rollNo(),
            request.fullName(),
            request.email(),
            request.phone(),
            classEntity,
            createdBy
        ));
        return StudentResponse.from(saved);
    }

    @PutMapping("/{id}")
    public StudentResponse updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        Student student = studentRepository.findById(id).orElseThrow();
        ClassEntity classEntity = classRepository.findById(request.classId()).orElseThrow();
        student.setRollNo(request.rollNo());
        student.setFullName(request.fullName());
        student.setEmail(request.email());
        student.setPhone(request.phone());
        student.setClassEntity(classEntity);
        return StudentResponse.from(studentRepository.save(student));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

    public record StudentRequest(
        @NotBlank String rollNo,
        @NotBlank String fullName,
        Long classId,
        String email,
        String phone
    ) {}

    public record StudentResponse(
        Long id,
        String rollNo,
        String fullName,
        String email,
        String phone,
        Long classId,
        String className,
        Instant createdAt
    ) {
        public static StudentResponse from(Student student) {
            return new StudentResponse(
                student.getId(),
                student.getRollNo(),
                student.getFullName(),
                student.getEmail(),
                student.getPhone(),
                student.getClassEntity().getId(),
                student.getClassEntity().getName(),
                student.getCreatedAt()
            );
        }
    }
}
